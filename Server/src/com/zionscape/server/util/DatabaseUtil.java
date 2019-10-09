package com.zionscape.server.util;

import com.google.common.base.MoreObjects;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zionscape.server.Server;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Stuart
 */
public final class DatabaseUtil {

	private final static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	private static boolean initiated = false;

	/**
	 * initiates the datasource
	 *
	 * @throws Exception
	 */
	public static void init() throws Exception {
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://varrock.org:3306/entranao_server?useServerPrepStmts=false&rewriteBatchedStatements=true&autoReconnect=true");
		dataSource.setUser("entranao_server");
		dataSource.setPassword("o1E8JdXDLVlS");

		// check each connection for timeout every 60 seconds
		dataSource.setTestConnectionOnCheckout(false);
		dataSource.setTestConnectionOnCheckin(true);
		dataSource.setIdleConnectionTestPeriod(60);

		dataSource.setMaxStatementsPerConnection(50);
		dataSource.setMinPoolSize(5);
		dataSource.setMaxPoolSize(100);

		System.out.println("min pool size: " + dataSource.getMinPoolSize() + " max " + dataSource.getMaxPoolSize());

		initiated = true;
	}

	/**
	 * Gets a Jdbc connection from the datasource
	 *
	 * @return Jdbc connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if (!initiated) {
			throw new RuntimeException("datasource has not been initiated");
		}
		return dataSource.getConnection();
	}

	public static void printDebug() {
		try {
			System.out.println(MoreObjects.toStringHelper(DatabaseUtil.class).add("idle connections", dataSource.getNumIdleConnections()).add("connections", dataSource.getNumConnections()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}