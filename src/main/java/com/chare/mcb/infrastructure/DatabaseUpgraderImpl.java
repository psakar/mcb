package com.chare.mcb.infrastructure;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.chare.databaseUpgrader.DatabaseUpgrade;

class DatabaseUpgraderImpl extends com.chare.databaseUpgrader.DatabaseUpgraderImpl {

	private static final Format versionFormat = new DecimalFormat("000000");
	private JdbcTemplate jdbcTemplate;
	private PlatformTransactionManager transactionManager;
	private TransactionStatus transaction;
	private final String findSql;
	private final String existsSql;
	private final String updateSql;
	private final String insertSql;

	DatabaseUpgraderImpl(DataSource dataSource, List<DatabaseUpgrade> databaseUpgrades) {
		super(databaseUpgrades);
		this.transactionManager = new DataSourceTransactionManager(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		findSql = "SELECT value FROM settings WHERE code='DB_VERSION'";
		existsSql = "SELECT count(*) FROM settings WHERE code='DB_VERSION'";
		updateSql = "UPDATE settings SET value=? WHERE code='DB_VERSION'";
		insertSql = "INSERT INTO settings (code, description1, description2, description3, type, value) SELECT code = 'DB_VERSION', description1 = 'Version of database schema', description2 = 'Version of database schema', description3 = 'Version of database schema', type=0, value= ?";
	}

	@Override
	protected final void startTransaction() throws Exception {
		transaction = transactionManager.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED));
	}

	@Override
	protected final void commitTransaction() throws Exception {
		transactionManager.commit(transaction);
	}

	@Override
	protected final void rollbackTransaction() throws Exception {
		transactionManager.rollback(transaction);
	}

	@Override
	protected int getDatabaseVersion() throws Exception {
		String value = jdbcTemplate.queryForObject(findSql, String.class);
		int pos = value.indexOf(".");
		return (pos < 0) ? 0 : Integer.valueOf(value.substring(pos+1));
	}

	@Override
	protected void setDatabaseVersion(int version) throws Exception {
		String value = "1." + versionFormat.format(version);

		boolean exists = (1 == jdbcTemplate.queryForInt(existsSql));
		if (exists) {
			jdbcTemplate.update(updateSql, value);
		} else {
			jdbcTemplate.update(insertSql, value);
		}
	}

	@Override
	public void execute(String sql) throws SQLException {
		log.debug("SQL\n" + sql);
		jdbcTemplate.execute(sql);
	}

}
