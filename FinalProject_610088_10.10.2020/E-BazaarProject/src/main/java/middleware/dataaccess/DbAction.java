
package middleware.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

class DbAction {
	protected String query;
	protected ResultSet resultSet;
	protected DbClass concreteDbClass;
	protected SimpleConnectionPool pool;
	protected Connection con;

	DbAction(DbClass c) throws DatabaseException {
		concreteDbClass = c;
		pool = DataAccessUtil.getPool();
		con = pool.getConnection(concreteDbClass.getDbUrl()); // new
	}

	void performRead() throws DatabaseException {
		concreteDbClass.buildQuery();
		ResultSet resultSet = DataAccessUtil.runQuery(pool, con, concreteDbClass.getQuery());

		concreteDbClass.populateEntity(resultSet);
	}

	Integer performUpdate() throws DatabaseException {
		concreteDbClass.buildQuery();
		Integer generatedKey = DataAccessUtil.runUpdate(pool, con, concreteDbClass.getQuery());

		return generatedKey;
	}

	void performDelete() throws DatabaseException {
		concreteDbClass.buildQuery();
		DataAccessUtil.runUpdate(pool, con, concreteDbClass.getQuery());
	}

	void returnToPool() throws DatabaseException {
		pool.returnToPool(con, concreteDbClass.getDbUrl());
	}

	void startTransaction() throws DatabaseException {
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DatabaseException(
					"DbAction.startTransaction() " + "encountered a SQLException " + e.getMessage());
		}
	}

	void commit() throws DatabaseException {
		System.out.println("\nCOMMIT \n");
		try {
			con.commit();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	public void rollback() throws DatabaseException {
		try {
			con.rollback();
		} catch (SQLException e) {
			throw new DatabaseException("rollback encountered a SQLException " + e.getMessage());
		}
	}

}
