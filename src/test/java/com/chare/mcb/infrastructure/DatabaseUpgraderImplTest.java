package com.chare.mcb.infrastructure;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.chare.databaseUpgrader.DatabaseUpgrade;
import com.chare.databaseUpgrader.DatabaseUpgradesReader;
import com.chare.test.DbTestCase;

public class DatabaseUpgraderImplTest extends DbTestCase {

	public static final String updateDirectory = "src/main/webapp/WEB-INF/updates";

	@Test
	public void testUpgradesFound() throws Exception {
		List<DatabaseUpgrade> databaseUpgrades = readUpgrades();
		assertTrue(databaseUpgrades.size() > 0);
	}

	private List<DatabaseUpgrade> readUpgrades() {
		return new DatabaseUpgradesReader().readUpdates(updateDirectory);
	}

	@Test
	public void testUpgrade() throws Exception {
		DatabaseUpgraderImpl databaseUpgrader = new DatabaseUpgraderImpl(dataSource, readUpgrades());
		assertTrue(databaseUpgrader.upgrade());
	}
}
