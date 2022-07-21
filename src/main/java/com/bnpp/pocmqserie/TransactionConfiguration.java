package com.bnpp.pocmqserie;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionConfiguration {

//    @Bean
//    @Primary
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory);
//        return transactionManager;
//    }

//    @Bean(initMethod = "init", destroyMethod = "close")
//    public AtomikosDataSourceBean orderDataSource() {
//        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
//         Configure the data source
//        return dataSource;
//    }
//
//    @Bean(initMethod = "init", destroyMethod = "close")
//    public UserTransactionManager userTransactionManager() throws SystemException {
//        UserTransactionManager userTransactionManager = new UserTransactionManager();
//        userTransactionManager.setTransactionTimeout(300);
//        userTransactionManager.setForceShutdown(true);
//        return userTransactionManager;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws SystemException {
//        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
//        jtaTransactionManager.setTransactionManager(userTransactionManager());
//        jtaTransactionManager.setUserTransaction(userTransactionManager());
//        return jtaTransactionManager;
//    }

}
