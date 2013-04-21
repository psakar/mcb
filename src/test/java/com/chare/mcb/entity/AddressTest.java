package com.chare.mcb.entity;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AddressTest {

	private Address entity;

	@Before
	public void setUp() throws Exception {
		entity = new Address();
	}



	@Test
	public void testGetFullname() throws Exception {
		assertFullnmae("", null, null);
		assertFullnmae("a", null, "a");
		assertFullnmae("b", "b", null);
		assertFullnmae("b a", "b", "a");
	}

	private void assertFullnmae(String fullname, String name, String name2) {
		entity.name = name;
		entity.name2 = name2;
		assertEquals(fullname, entity.getFullName());
	}

	@Test
	public void testGetZipAndTown() throws Exception {
		assertZipAndTown("", null, null);
		assertZipAndTown("a", null, "a");
		assertZipAndTown("b", "b", null);
		assertZipAndTown("b a", "b", "a");
	}

	private void assertZipAndTown(String zipAndTown, String zip, String town) {
		entity.zip = zip;
		entity.town = town;
		assertEquals(zipAndTown, entity.getZipAndTown());
	}
}
