package com.chare.mcb;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Import({
	com.chare.mcb.ApplicationConfig.class,
	com.chare.mcb.ContextConfig.class,
	com.chare.infrastructure.EnvironmentConfig.class,
	com.chare.infrastructure.JndiDataSourceConfig.class,
	com.chare.mcb.repository.Config.class,
	com.chare.infrastructure.scheduler.Config.class,
	com.chare.mcb.infrastructure.Config.class,
	com.chare.infrastructure.management.Config.class,
	com.chare.service.audit.Config.class,
	//com.chare.mcb.service.AuditConfig.class,
	com.chare.mcb.service.Config.class,
	com.chare.mcb.www.Config.class

})
@EnableTransactionManagement
public class Config {}
