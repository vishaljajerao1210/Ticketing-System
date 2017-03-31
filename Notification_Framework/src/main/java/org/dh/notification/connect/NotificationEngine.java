package org.dh.notification.connect;

import javax.sql.DataSource;

import org.dh.notification.NFConfiguation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

//@Configuration
public class NotificationEngine {
	
	
	NFConfiguation configuration;
	public DataSource appDataSource;
	public DataSource neDataSource;
	public LocalContainerEntityManagerFactoryBean transactionManager;
	
	@Value("${notification.datasource.driver-class-name}")
	private String dbDriverClass;

	@Value("${notification.datasource.url}")
	private String dbConnectionURL;

	@Value("${notification.datasource.username}")
	private String dbUsername;

	@Value("${notification.datasource.password}")
	private String dbPassword;	
	
	public NotificationEngine setAppDataSource(DataSource appDataSource){
		this.appDataSource =appDataSource;
		return this;
	}
	public NotificationEngine setTransactionManager(DataSourceTransactionManager transactionManager){
		this.setTransactionManager(transactionManager);
		return this;
	}
	public NotificationEngine setNotificationDataSource(DataSource neDataSource){
		this.neDataSource =neDataSource;
		return this;
	}
	
	public void  build(){
		configuration =new NFConfiguation();
		configuration.setAppDataSource();
		configuration.setNeDataSource();
		configuration.setTransactionManager(this.transactionManager);
	}
	
	/*@Autowired
	private  NotificationRegistry registry;
	
	public DataSource appDataSource;
	
	
	public NotificationEngine setAppDataSource(DataSource appDataSource){
		this.appDataSource =appDataSource;
		return this;
	}
	
	public NotificationEngine setTransactionManager(TransactionManager transactionManager){
		this.setTransactionManager(transactionManager);
		return this;
	}
	
	
	
	@Bean(name="ne_DataSource")
	private DataSource appDataSource(){
		isNull(this.appDataSource,true,"Application DataSource not be null,please specify the application Database to fetch the notification details");
		if(isNull(this.neDataSource))
			return DataSourceBuilder.create().build();
		else
			return this.neDataSource;
	}*/
}
