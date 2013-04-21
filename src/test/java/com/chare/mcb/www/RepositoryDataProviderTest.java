package com.chare.mcb.www;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.chare.mcb.entity.User;
import com.chare.mcb.repository.UserRepository;
import com.chare.repository.Restriction;
import com.chare.repository.Sort;

public class RepositoryDataProviderTest {

	private RepositorySortableDataProvider<User> dataProvider;
	@Mock
	private UserRepository rootAccountRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		dataProvider = new RepositorySortableDataProvider<User>(rootAccountRepository);
		dataProvider.addRestriction(new Restriction("code", "1337"));
		dataProvider.addSort(new Sort("id"));
	}

	@Test
	public void testSize() throws Exception {
		when(rootAccountRepository.getCount(dataProvider.getRestrictions())).thenReturn(200);
		assertEquals(200, dataProvider.size());
	}

	@Test
	public void testIterator() throws Exception {
		List<User> rootAccounts = Arrays.asList(new User());
		when(rootAccountRepository.find(5, 10, dataProvider.getRestrictions(), dataProvider.getSorts())).thenReturn(rootAccounts);
		assertEquals(rootAccounts.iterator().next(), dataProvider.iterator(5, 10).next());
	}

}
