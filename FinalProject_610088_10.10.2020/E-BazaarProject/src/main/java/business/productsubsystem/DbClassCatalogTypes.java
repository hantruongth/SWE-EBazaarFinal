package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import business.externalinterfaces.Catalog;
import business.externalinterfaces.DbClassCatalogForTest;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassCatalogTypes implements DbClass, DbClassCatalogForTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DbClassCatalogTypes.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	private String query;
	private String queryType;
	final String GET_TYPES = "GetTypes";
	private CatalogTypesImpl types;

	public CatalogTypesImpl getCatalogTypes() throws DatabaseException {
		queryType = GET_TYPES;
		dataAccessSS.atomicRead(this);
		return types;
	}

	public void buildQuery() {
		if (queryType.equals(GET_TYPES)) {
			buildGetTypesQuery();
		}
	}

	void buildGetTypesQuery() {
		query = "SELECT * FROM CatalogType";
	}

	/**
	 * This is activated when getting all catalog types.
	 */
	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		types = new CatalogTypesImpl();
		try {
			while (resultSet.next()) {
				types.addCatalog(resultSet.getInt("catalogid"), resultSet.getString("catalogname"));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}

	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

	public String getQuery() {

		return query;
	}

	@Override
	public List<Catalog> getCatalogList() {
		DbClassCatalogTypes cT = new DbClassCatalogTypes();
		try {
			return cT.getCatalogTypes().getCatalogs();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
