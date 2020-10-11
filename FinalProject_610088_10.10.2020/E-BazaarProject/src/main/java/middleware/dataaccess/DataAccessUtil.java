
package middleware.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;

import middleware.DbConfigProperties;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbConfigKey;

public class DataAccessUtil {
	private static final Logger LOG = Logger.getLogger(DataAccessUtil.class.getName());

	public static ResultSet runQuery(SimpleConnectionPool pool, Connection con, String query) throws DatabaseException {
		LOG.info("Executing query: " + query);
		ResultSet rs = SimpleConnectionPool.doQuery(con, query);
		return rs;
	}

	protected static SimpleConnectionPool getPool() throws DatabaseException {
		DbConfigProperties props = new DbConfigProperties();
		return SimpleConnectionPool.getInstance(props.getProperty(DbConfigKey.DB_USER.getVal()),
				props.getProperty(DbConfigKey.DB_PASSWORD.getVal()), props.getProperty(DbConfigKey.DRIVER.getVal()),
				Integer.parseInt(props.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal())));

	}

	public static Integer runUpdate(SimpleConnectionPool pool, Connection con, String query) throws DatabaseException {
		Integer generatedKey = SimpleConnectionPool.doUpdate(con, query);
		return generatedKey;
	}

}
