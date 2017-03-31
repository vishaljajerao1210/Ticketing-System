package com.ticketingSystem;



import java.util.HashMap;



import javax.persistence.EntityManager;

import javax.sql.DataSource;



import org.apache.commons.dbcp.BasicDataSource;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Primary;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.orm.jpa.JpaTransactionManager;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import org.springframework.transaction.PlatformTransactionManager;



/**

 * Configuration class for all database related beans

 * 

 * @author Pavan Andhukuri

 *

 */

@Configuration
@EnableJpaRepositories(basePackages = { "com.ticketingSystem" }, entityManagerFactoryRef = "trexEntityManagerFactory", transactionManagerRef = "trexTrasactionManager")

public class DatabaseConfiguration {



	private final Logger logger = LoggerFactory.getLogger(getClass());



	@Value("${spring.datasource.driver-class-name}")

	String driverClass;



	@Value("${spring.datasource.driver-class-name}")

	private String dbAppDriverClass;



	@Value("${spring.datasource.url}")

	private String dbAppConnectionURL;



	@Value("${spring.datasource.username}")

	private String dbAppUsername;



	@Value("${spring.datasource.password}")

	private String dbAppPassword;



	/**

	 * Creates a {@link DataSource} object from the provided details.

	 * 

	 * @return instance of {@link BasicDataSource}

	 */

	private BasicDataSource dataSource() {

		logger.info("Configuring data source for Application");

		logger.debug("Driver Class:{}\nNotification DB URL:{}\nNotification DB Username:{}", dbAppDriverClass,

				dbAppConnectionURL, dbAppUsername);



		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName(dbAppDriverClass);

		dataSource.setUrl(dbAppConnectionURL);

		dataSource.setUsername(dbAppUsername);

		dataSource.setPassword(dbAppPassword);

		return dataSource;

	}



	/**

	 * Creats a transaction manager over the default application database. The

	 * same transaction manager is used by all the database connections in the

	 * application.

	 * 

	 * @param trexEntityManager

	 * @return

	 */

	@Primary

	@Bean(name = "trexTrasactionManager")

	public PlatformTransactionManager dataSourceTransactionManager(

			@Qualifier("trexEntityManagerFactory") LocalContainerEntityManagerFactoryBean trexEntityManager) {

		JpaTransactionManager dataSourceTrasactionManager = new JpaTransactionManager();

		dataSourceTrasactionManager.setEntityManagerFactory(trexEntityManager.getObject());

		return dataSourceTrasactionManager;

	}



	/**

	 * Creates entity manger factory for trex.

	 * 

	 * @return

	 */

	@Primary

	@Bean(name = "trexEntityManagerFactory")

	@Autowired

	public LocalContainerEntityManagerFactoryBean setEntityManagerFactory() {

		LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();

		lef.setDataSource(dataSource());

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		vendorAdapter.setGenerateDdl(true);



		lef.setJpaVendorAdapter(vendorAdapter);

		lef.setPackagesToScan("com.ticketingSystem");

		lef.setPersistenceUnitName("trexPersistentUnit");

		HashMap<String, Object> properties = new HashMap<String, Object>();

		

		properties.put("hibernate.hbm2ddl.auto", "update");

		properties.put("hibernate.format_sql", "false");

		properties.put("hibernate.dialect","org.hibernate.dialect.MySQLDialect");

		properties.put("hibernate.show_sql", "true");

		//lef.setJpaPropertyMap(properties);

		lef.afterPropertiesSet();

		return lef;

	}



	/**

	 * Returns an instance of {@link EntityManager} from the entity manager

	 * factory.

	 * 

	 * @param trexEntityManagerFactory

	 * @return

	 */

	@Autowired

	@Primary

	@Bean(name = "trexEntityManager")

	public EntityManager getEntityManager(

			@Qualifier("trexEntityManagerFactory") LocalContainerEntityManagerFactoryBean trexEntityManagerFactory) {

		return trexEntityManagerFactory.getObject().createEntityManager();

	}

}

