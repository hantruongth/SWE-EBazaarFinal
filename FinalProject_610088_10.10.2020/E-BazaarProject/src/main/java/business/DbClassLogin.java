package business;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassLogin implements DbClass {
	private static final Logger LOG = Logger.getLogger(DbClassLogin.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	private Integer custId;
	int authorizationLevel;
	private String password;
	private String query;
	private boolean authenticated = false;

	public DbClassLogin(Login login) {
		this.custId = login.getCustId();
		this.password = login.getPassword();
	}

	public boolean authenticate() throws BackendException {
		LOG.info("Authenticating");
		try {
			dataAccessSS.atomicRead(this);
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
		return authenticated;
	}

	public int getAuthorizationLevel() {
		LOG.info("authorizationLevel = " + authorizationLevel);
		return 1;
	}

	/** Tries to locate the custId/password pair in Customer table */
	public void buildQuery() {
		query = "SELECT * FROM Customer WHERE custid = " + custId + " AND " + "password = '" + password + "'";
	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		try {
			if (resultSet.next()) {
				// authorizationLevel = resultSet.getInt("admin");
				authenticated = true;
			} // else authenticated = false;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	}

	public String getQuery() {
		return query;
	}
}
