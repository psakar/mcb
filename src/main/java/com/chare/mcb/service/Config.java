package com.chare.mcb.service;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.chare.core.Titles.TitleSources;
import com.chare.infrastructure.EnvironmentConfig;
import com.chare.infrastructure.reportService.ReportServiceImpl.SystemParametersProvider;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.CardRepository;
import com.chare.mcb.repository.FeeTypeRepository;
import com.chare.mcb.repository.PostingFileRepository;
import com.chare.mcb.repository.SettingRepository;
import com.chare.mcb.repository.StatementRepository;
import com.chare.mcb.repository.TransferTypeRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.CardStatementExporter.DateFactory;
import com.chare.mcb.service.CardStatementGeneratorImpl.CardStatementLinesMerger;
import com.chare.mcb.service.CardStatementGeneratorImpl.StatementPeriodCalculator;
import com.chare.mcb.service.PostingFileExporterImpl.ReferenceGenerator;
import com.chare.mcb.service.SwiftBlocksParser.BodyParser;
import com.chare.mcb.service.SwiftBlocksParser.HeaderParser;
import com.chare.mcb.service.SwiftBlocksParser.TrailerParser;
import com.chare.mcb.service.reports.SystemParametersProviderImpl;
import com.chare.service.AuditService;
import com.chare.service.LinesReader;
import com.chare.service.NameGenerator;
import com.chare.service.UserSessionsList;

@Configuration
public class Config extends com.chare.service.Config {

	@Autowired
	@Qualifier(EnvironmentConfig.REPORT_DIRECTORY)
	private File reportDirectory;

	@Autowired
	private TitleSources titleSources;

	@Autowired
	private TransferTypeRepository transferTypeRepository;

	@Autowired
	private StatementRepository statementRepository;

	@Autowired
	private UserRepository userRepository;

	//	@Autowired
	//	private TitleSources titleSources;
	//	@Autowired
	//	@Qualifier(EnvironmentConfig.REPORT_DIRECTORY)
	//	private File reportDirectory;

	@Autowired
	private CardRepository cardRepository;

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private FeeTypeRepository feeTypeRepository;

	@Autowired
	private PostingFileRepository postingFileRepository;

	@Autowired
	private SettingRepository settingRepository;


	@Bean
	public AuditService auditService() {
		return new AuditService(Logger.getLogger(AuditService.class));
	}

	@Bean
	public AuthenticationService authenticationService() {
		return new AuthenticationServiceImpl(userRepository) ;
	}

	@Bean
	public BookingGenerator bookingGenerator() {
		return new BookingGenerator(settingRepository, cardRepository);
	}

	@Bean
	public CardTransactionParser cardTransactionParser() {
		return new CardTransactionParserImpl(cardRepository, feeTypeRepository);
	}

	@Bean
	@Autowired
	public GenerateCardStatementsTask generateCardStatementsTask(CardStatementGenerator statementGenerator) {
		return new GenerateCardStatementsTask(cardRepository, statementGenerator);
	}
	@Bean
	public PostingFileExporter postingFileExporter() {
		return new PostingFileExporterImpl(postingFileRepository, calendarRepository, new PostingFileWriter(new BookingFormatter(), settingRepository), new ReferenceGenerator(postingFileRepository));
	}

	@Bean
	@Autowired
	public PostingFileGenerator postingFileGenerator(BookingGenerator bookingGenerator, SettingRepository settingRepository) {
		return new PostingFileGeneratorImpl(postingFileRepository, bookingGenerator, new NameGenerator(), statementRepository, calendarRepository, settingRepository);
	}

	@Bean
	@Autowired
	public CardStatementGenerator statementGenerator(SystemParametersProvider systemParametersProvider) {
		return new CardStatementGeneratorImpl(cardRepository, createStatementExporter(systemParametersProvider), new CardStatementLinesMerger(), new StatementPeriodCalculator(calendarRepository));
	}

	private CardStatementExporter createStatementExporter(SystemParametersProvider systemParametersProvider) {
		return new CardStatementExporter(reportDirectory, systemParametersProvider, settingRepository, new DateFactory());
	}

	@Bean
	public SystemParametersProvider systemParametersProvider() {
		return new SystemParametersProviderImpl(titleSources);
	}

	@Bean
	@Autowired
	public TransferTypeResolver transferTypeResolver(CardTransactionParser parser) {
		return new TransferTypeResolverImpl(transferTypeRepository, parser);
	}

	@Bean
	@Autowired
	public UploadFileService uploadFileService(TransferTypeResolver transferTypeResolver) {
		return new UploadFileServiceImpl(new LinesReader(), new SwiftParser(), createSwiftBlocksParser(), statementRepository, transferTypeResolver);
	}

	private SwiftBlocksParser createSwiftBlocksParser() {
		return new SwiftBlocksParser(new HeaderParser(), new BodyParser(), new TrailerParser());
	}

	@Bean
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public UserPreferences userPreferences() {
		return new UserPreferences();
	}

	@Bean
	public UserSessionsList sessionsList() {
		return new UserSessionsList();
	}

	//	@Bean
	//	public ReportService reportService() {
	//		return new ReportServiceImpl(reportDirectory, connectionProvider, systemParametersProvider());
	//	}

}
