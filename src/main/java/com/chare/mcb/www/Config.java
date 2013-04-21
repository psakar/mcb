package com.chare.mcb.www;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	public static final String MODULE_NAME = "main";


	@Bean(name=MODULE_NAME)
	public MountList mountList() {
		return new com.chare.mcb.www.MountList();
	}

}
