# MQ Serie & Spring Boot POC in PubSub Mode

The purpose is to understand the behavior of a PubSub message communication using MQSerie as a broker. The application
is a Java App running with Spring Boot, exposing a REST API and communicating with MQ serie by sending messages to a
topic and receives messages from subscriptions

## Resources

* https://developer.ibm.com/learningpaths/ibm-mq-badge/mq-fundamentals/
*

## Starting MQ Serie in Docker

[Official IBM docs](https://developer.ibm.com/learningpaths/ibm-mq-badge/create-configure-queue-manager/)

```shell
docker pull icr.io/ibm-messaging/mq:latest
docker volume create qm1data
docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --volume qm1data:/mnt/mqm --publish 1414:1414 --publish 9443:9443 --detach --env MQ_APP_PASSWORD=passw0rd --name QM1 icr.io/ibm-messaging/mq:latest
```

### Accessing the IBM MQ console

1. Browse the URL https://localhost:9443/ibmmq/console
2. Accept the self-signed certificate
3. Login with admin/passw0rd

## Run the app

The application uses an HSQL in-memory database to store dummy entities. It communicates with IBM MQSerie using a
JMSTemplate (provided by Spring Boot AutoConfiguration). It exposes some endpoints to enable some "life-real" scenario,
like POSTing and PUTing resources, that triggers a message sent in MQ Serie.

To start the application:

```shell
./mvnw clean install
java -jar target/poc-mqserie-0.0.1-SNAPSHOT.jar
```

1. To create a Deal (No Messages sent) :

```shell
curl --location --request POST 'http://localhost:8080/deals' \
--header 'Content-Type: text/plain' \
--data-raw 'DealName 1'

> 844d1887-2a3f-4d4f-8896-05d35c1d10dc
```

2. To update a Deal name (send DealUpdated message to MQSerie) :

```shell
curl --location --request PUT 'http://localhost:8080/deals/844d1887-2a3f-4d4f-8896-05d35c1d10dc' \
--header 'Content-Type: text/plain' \
--data-raw 'New Deal Name'
```

3. To update a Deal jurisdiction (send DealJurisdictionUpdated message to MQSerie) :

```shell
curl --location --request PUT 'http://localhost:8080/deals/844d1887-2a3f-4d4f-8896-05d35c1d10dc/jurisdiction' \
--header 'Content-Type: application/json' \
--data-raw '["France", "Germany"]'
```

## Overview of Spring Boot & MQSerie in Pubsub

1. MQSerie supports (at least) 2 kind of messaging :
    1. Peer to Peer using Queue
    2. PubSub using Topics
2. Spring Boot automatically configures all resources needed to communicate using JMS with MQSerie in QUEUE
    1. `jmsTemplate.convertAndSend(destinationName, message)` sends a message to a queue. The QUEUE has to exist in MQ
       Serie
    2. `@JmsListener(destination = destinationName)` receives messages from a queue.

3. To enable PubSub, a custom bean has to be provided indicating the exchange are in
   PubSubMode `factory.setPubSubDomain(true);`:

```java
@Bean
@Primary
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
        }
```

    1. With this configuration, the @JmsListener and JmsTemplate are both in PubSub mode. Which means, they don't read from or write to Queue, but from and to Topics

4. To enable Java Object to JSON string conversion and vice-versa, we can provide a custom message converter (in this
   example, using jackson):

```java
@Bean // Serialize message content to json using TextMessage
public MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter converter=new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
        }

@Bean
@Primary
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        return factory;
        }
```

5. Before going deeper, let's clear a bit how MQSerie handles Topics.

#### Topics in MQ Series

[Link to doc](https://www.ibm.com/docs/en/ibm-mq/9.3?topic=components-topics)

The purpose of Topics is to enable a PubSub communication mode between services. 1 publisher publishes a message in a
Topic, and the message can be read by as many subscribers as there is on the same topic. Messages are published to a
Topic String (example: /dev/, /domain/subdomain1/...).

In my understanding, a Topic does not have any queue linked to it. It is simply a container concept, but it does not
seem to have lots of impact in the Queue manager structure (??). But the subscription are most important. Indeed, for
each subscription, a related queue is created (either automatically in a managed mode, or manually). But a queue must
exist. When a message is sent to a topic, the Queue Manager broadcasts the message to every queue of every available
subscriber.

Which means for me :

* No replay all messages from a Topic provided by default, because a topic does not store the received messages (or at
  least, the MQ console does not show them)
* We need to create the subscriber properly and permanently to make sure the queue still exists in case of an
  application crash, to make sure older messages are received when the application recovers.

An application subscribing to a topic can create the Subscription either in :

* Non durable mode -> Once the subscription stops being used, the Queue manager deletes the subscription and the Queue
* Durable mode -> The subscription and queue are still alive even when no one is listening


6. Spring Boot automatically creates **non-durable subscription**. Which means, if the application crashes, it won't
   receive messages sent during the down time.

7. To enable Durable subscription:
    1. change the `jmsListenerContainerFactory` bean `factory.setSubscriptionDurable(true);
    2. Add a clientId. A durable subscription must be linked to a clientId `factory.setClientId("asset-deal-updated");`

```java
@Bean
@Primary
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setSubscriptionDurable(true);
        factory.setClientId("asset-deal-updated");
        return factory;
        }
```

8. To create a subscription, annotate a method
   with `@JmsListener(destination = topicName, subscription =subscriptionName)`:

```java
@JmsListener(destination = "deal/jurisdiction/updated/", subscription = "E2ECC.FACILITY.DEAL.JURISDICTION.UPDATED")
public void onDealJurisdictionUpdated(DealJurisdictionUpdated dealJurisdictionUpdated){
        System.out.println("FacilitySubscribers.onDealJurisdictionUpdated");
        System.out.println("dealJurisdictionUpdated = "+dealJurisdictionUpdated);

        facilityService.updateFacilityJurisdiction(dealJurisdictionUpdated);
        }
```

In the MQ Console, in the "Subscription" panel, you should see a subscription created with a long
name:  `JMS:QM1:facility-deal-jurisdiction-updated:E2ECC.FACILITY.DEAL.JURISDICTION.UPDATED`
> I still don't know if we can change this or not
> If you stop listening the subscription, but keeps sending messages, you should see the queue cumulating messages. Once the subscription listens again, all unread messages are received

9. WEIRD: If your application creates multiple Durable Subscription, you need to use different client ID
    1. Provide 1 `jmsListenerContainerFactory` bean per durable subscription with a different client id each time
    2. In the `@JmsListener` annotation, provided the attribute `containerFactory="jmsListenerContainerFactory"` to
       indicate which factory to use to create the subscription Example with 3 durables subscriptions:

```java
@Bean
@Primary
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setSubscriptionDurable(true);
        factory.setClientId("asset-deal-updated");
        return factory;
        }

@Bean
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory2(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setSubscriptionDurable(true);
        factory.setClientId("facility-deal-jurisdiction-updated");
        return factory;
        }

@Bean
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory3(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setSubscriptionDurable(true);
        factory.setClientId("asset-facility-jurisdiction-updated");
        return factory;
        }
```

```java
@JmsListener(destination = "deal/updated/", subscription = "E2ECC.ASSET.DEAL.UPDATED", containerFactory = "jmsListenerContainerFactory")
public void onDealUpdated(DealUpdated dealUpdated){
        System.out.println("AssetSubscribers.onDealUpdated");
        System.out.println("dealUpdated = "+dealUpdated);
        }

@JmsListener(destination = "deal/jurisdiction/updated/", subscription = "E2ECC.FACILITY.DEAL.JURISDICTION.UPDATED", containerFactory = "jmsListenerContainerFactory2")
public void onDealJurisdictionUpdated(DealJurisdictionUpdated dealJurisdictionUpdated){
        System.out.println("FacilitySubscribers.onDealJurisdictionUpdated");
        System.out.println("dealJurisdictionUpdated = "+dealJurisdictionUpdated);

        facilityService.updateFacilityJurisdiction(dealJurisdictionUpdated);
        }

@JmsListener(destination = "facility/jurisdiction/updated/", subscription = "E2ECC.ASSET.FACILITY.JURISDICTION.UPDATED", containerFactory = "jmsListenerContainerFactory3")
public void onFacilityJurisdictionUpdated(FacilityJurisdictionUpdated facilityJurisdictionUpdated){
        System.out.println("AssetSubscribers.onFacilityJurisdictionUpdated");
        System.out.println("facilityJurisdictionUpdated = "+facilityJurisdictionUpdated);
        }
```

10. Sharing a subscription. In my understandings, it must be done if your application has many instances (horizontal
    scalability). Simple change the `jmsListenerContainerFactory` and add `factory.setSubscriptionShared(true);` :

```java
@Bean
@Primary
public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory){
        DefaultJmsListenerContainerFactory factory=new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setSubscriptionDurable(true);
        factory.setClientId("asset-deal-updated");
        factory.setSubscriptionShared(true);
        return factory;
        }
```

We covered all the things I've tested in the POC. In the end, I can create a subscription automatically or to connect to
an existing one when the application starts using Durable Subscription. If I have many applications running in parallel,
they all connect to the same subscriptions and share the messages. If you are not interested in all events, but we don't
care if we miss some, we can simply use Non durable Subscription (free useless resources)

11. Managing Eventual Consistency

When communicating with the database, a local transaction is created and ensure the data is fully stored. By adding a
message exchange with JMS, we need to message distribution to be part of the transaction. Which means:

* No messages are sent if the database rolls back
* No data are saved if the JMS sends an Exception

To achieve this, 2 possibilities

* We can use Distributed Transactions and 2PC (but heavy, even with Atomikos). I couldn't easily make it work, so I put
  it aside
* We can use a pattern called "Transactional Outbox" and write the message inside a table of the same datasource. This
  will ensure both the data and messages and stored in the DB and are part of the same local transaction. Then, use an
  async method to fetch pending messages to the JMS system

To do so, here is an example of a possible implementation.

1. We use a TransactionalEventBus, that stores the message in a DB instead of sending them directly. A Message is
   an `Activity`, which has a Direction (EGRESS: OUT, only one for the moment, can be deleted if we don't monitor the
   ingress messages), and the message stored as a Json string.

```java

@Service
public class TransactionalEventBus implements EventPublisher {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void publish(DomainEvent domainEvent) {
        try {
            Activity activity = new Activity(UUID.randomUUID().toString(), LocalDateTime.now(), domainEvent.getId(), ActivityDirection.EGRESS, objectMapper.writeValueAsString(domainEvent), domainEvent.getClass().toString());
            activityRepository.save(activity);
            System.out.println("Activity saved : " + activity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Activity> getPendingEgressActivities() {
        return activityRepository.findAllByStatusAndDirection(ActivityStatus.PENDING, ActivityDirection.EGRESS);
    }

    public void markAsHandled(Activity activity) {
        activity.markAsHandled();
    }
}
```

The entity `Activity` stored in the Activity table.
```java
@Entity
public class Activity {
    @Id
    private String id;
    private LocalDateTime occuredOn;
    private String eventId;
    private ActivityDirection direction;
    private String content;
    private String type;
    private ActivityStatus status;
    private LocalDateTime deliveredOn;

    public Activity(String id, LocalDateTime occuredOn, String eventId, ActivityDirection direction, String content, String type) {
        this.id = id;
        this.occuredOn = occuredOn;
        this.eventId = eventId;
        this.direction = direction;
        this.content = content;
        this.type = type;
        this.status = ActivityStatus.PENDING;
    }

    public Activity() {
    }

    public void markAsHandled() {
        this.status = ActivityStatus.HANDLED;
        this.deliveredOn = LocalDateTime.now();
    }
}
```

2. We use an async event publisher to publish messages every second (could be less, depending on the resources available). Could be also another solution depending on required propagation time
```java
@Service
public class AsyncJmsEventPublisher {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransactionalEventBus transactionalEventBus;

    @Async
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void send() {
        transactionalEventBus.getPendingEgressActivities().forEach(activity -> {
            emitMessage(activity.getType(), activity.getContent());
            transactionalEventBus.markAsHandled(activity);
        });

    }

    private void emitMessage(String type, String content) {
        try {
            if (type.toLowerCase().contains("facilityjurisdictionupdated")) {
                jmsTemplate.convertAndSend("facility/jurisdiction/updated/", objectMapper.readValue(content, FacilityJurisdictionUpdated.class));
            } else if (type.toLowerCase().contains("dealupdated")) {
                jmsTemplate.convertAndSend("deal/updated/", objectMapper.readValue(content, DealUpdated.class));
            } else if (type.toLowerCase().contains("dealjurisdictionupdated")) {
                jmsTemplate.convertAndSend("deal/jurisdiction/updated/", objectMapper.readValue(content, DealJurisdictionUpdated.class));
            } else {
                System.out.println("Does not know where to send event " + type);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("unable to send the message", e);
        }
    }
}
```
> The implementation is naive, but here, I suppose the sender knows what topic to use. It could be the responsability of the business service for instance.

Each time a message is sent, we mark it as "HANDLED" in the DB, to make sure we don't sent it again.

One last thing, this is an example of an "at least one delivery". Indeed, if the message is sent, but the activity not marked as HANDLED, then the application will try again to send the message... So the listener needs to handle duplicate message.

Things that can be checked deeper :
* ACK mode
