package com.chare.mcb.entity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.chare.core.LanguageIndex;
public class RoleTest {

	private Role role;

	@Before
	public void setUp() {
		role = new Role();
	}

	@Test
	public void testDefaults() throws Exception {
		assertNull(role.code);
		assertNull(role.description1);
		assertNull(role.description2);
		assertNull(role.description3);
	}

	static class MockLanaguageIndex implements LanguageIndex {
		private final int index;
		public MockLanaguageIndex(int index) {
			this.index = index;
		}
		@Override
		public int getLanguageIndex() {
			return index;
		}
	}

	@Test
	public void testGetDescription() throws Exception {
		role.description1 = "description1";
		role.description2 = "description2";
		role.description3 = "description3";
		assertEquals(role.description1, role.getDescription(new MockLanaguageIndex(1)));
		assertEquals(role.description2, role.getDescription(new MockLanaguageIndex(2)));
		assertEquals(role.description3, role.getDescription(new MockLanaguageIndex(3)));
		assertEquals(role.description1, role.getDescription(new MockLanaguageIndex(4)));
	}

}
