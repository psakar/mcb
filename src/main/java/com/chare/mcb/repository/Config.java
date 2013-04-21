package com.chare.mcb.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class Config {

	@Bean
	public CalendarRepository calendarRepository() {
		return new CalendarRepositoryImpl();
	}

	@Bean
	public CardRepository cardRepository() {
		return new CardRepositoryImpl();
	}

	@Bean
	public CardItemRepository cardItemRepository() {
		return new CardItemRepositoryImpl();
	}

	@Bean
	public CardTypeRepository cardTypeRepository() {
		return new CardTypeRepositoryImpl();
	}

	@Bean
	public FeeTypeRepository feeTypeRepository() {
		return new FeeTypeRepositoryImpl();
	}

	@Bean
	public PostingFileRepository postingFileRepository() {
		return new PostingFileRepositoryImpl();
	}

	@Bean
	public SettingRepository settingRepository() {
		return new SettingRepositoryImpl();
	}

	@Bean
	public StatementRepository statementRepository() {
		return new StatementRepositoryImpl();
	}

	@Bean
	public StatementItemRepository statementItemRepository() {
		return new StatementItemRepositoryImpl();
	}

	@Bean
	public UserRepository userRepository() {
		return new UserRepositoryImpl();
	}

	@Bean
	public UserItemRepository userItemRepository() {
		return new UserItemRepositoryImpl();
	}

	@Bean
	public TransferTypeRepository transferTypeRepository() {
		return new TransferTypeRepositoryImpl();
	}

}
