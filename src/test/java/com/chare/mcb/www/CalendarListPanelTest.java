package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.chare.core.Utils;
import com.chare.mcb.entity.Calendar;
import com.chare.mcb.repository.CalendarRepository;
import com.chare.mcb.repository.UserRepository;
import com.chare.mcb.service.UserPreferences;
import com.chare.mcb.www.CalendarList.CalendarListPanel;
import com.chare.repository.Restriction;

public class CalendarListPanelTest extends WicketTestCase {

	private CalendarListPanel calendarPanel;

	@Override
	@Before
	public void before() throws Exception {
		super.before();
		calendarPanel = new CalendarListPanel("id");
		tester.startComponentInPage(calendarPanel);
	}

	@Test
	public void testPanelContents() throws Exception {
		assertNotNull(calendarPanel.get(AbstractListPanel.LIST_ID));
		assertNotNull(calendarPanel.get(AbstractListPanel.NAVIGATOR_ID));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testListContents() throws Exception {
		tester.assertContains(String.valueOf(TestConfig.calendar.holiday));
		tester.assertNoErrorMessage();
	}

	@Test
	public void testIsSecured() throws Exception {
		assertPageIsSecured(CalendarList.class);
	}

	@Override
	protected Class<?> getCustomConfig() {
		return TestConfig.class;
	}

	@Configuration
	static class TestConfig {
		public static Calendar calendar;

		@SuppressWarnings("unchecked")
		@Bean
		public CalendarRepository calendarDataProvider() {
			calendar = createCalendar();

			List<Calendar> list = Arrays.asList(calendar);
			CalendarRepository repository = mock(CalendarRepository.class);
			when(repository.find(anyInt(), anyInt(), any(List.class), any(List.class))).thenReturn(list);
			when(repository.findById(anyInt())).thenReturn(calendar);
			when(repository.getCount(anyListOf(Restriction.class))).thenReturn(1);

			return repository;
		}

		@Bean
		public UserRepository userRepository() {
			return mock(UserRepository.class);
		}
		@Bean
		@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public UserPreferences userPreferences() {
			return new UserPreferences();
		}
	}

	static Calendar createCalendar() {
		Calendar calendar = new Calendar();
		calendar.id_date = Utils.getToday();
		calendar.holiday = new Integer(1);
		return calendar;
	}

}
