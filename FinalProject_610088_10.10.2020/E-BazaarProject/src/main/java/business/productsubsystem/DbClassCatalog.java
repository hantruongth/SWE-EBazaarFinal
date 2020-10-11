package business.productsubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.util.TwoKeyHashMap;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

public class DbClassCatalog implements DbClass {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DbClassCatalog.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();

	private List<Catalog> catalogList;
	private static TwoKeyHashMap<Integer, String, Catalog> catalogTable;
	private Catalog catalog;
	private String catalogName;
	private String query;
	private String queryType;
	private Product product;
	Integer catalogId;

	private final String SAVE = "Save";
	private final String LOAD_CAT_TABLE = "LoadCatTable";
	private final String READ_CATALOG = "ReadCatalog";
	private final String DELETE_CATALOG = "DeleteCatalog";

	private final String READ_PROD_LIST = "ReadProdList";
	private final String SAVE_NEW_PROD = "SaveNewProd";

	/* @edited by Tauseef */
	public void saveNewCatalog(Catalog catalog) throws DatabaseException {
		this.catalogName = catalog.getName();
		queryType = SAVE;
		dataAccessSS.saveWithinTransaction(this);
	}

	/* @added by Tauseef */
	public void deleteCatalog(Catalog catalog) throws DatabaseException {
		catalogId = catalog.getId();
		queryType = DELETE_CATALOG;
		dataAccessSS.saveWithinTransaction(this);
	}

	/* @edited by Tauseef */
	public void buildQuery() throws DatabaseException {
		if (queryType.equals(SAVE)) {
			buildSaveQuery();
		} else if (queryType.equals(READ_CATALOG)) {
			buildReadCatalogQuery();
		} else if (queryType.equals(DELETE_CATALOG)) {
			buildDeleteCatalogQuery();
		}
	}

	void buildSaveQuery() throws DatabaseException {
		query = "INSERT into CatalogType " + "(catalogid,catalogname) " + "VALUES(NULL,'" + catalogName + "')";
	}

	private void buildReadCatalogQuery() {
		query = "SELECT * FROM CatalogType";
	}

	private void buildDeleteCatalogQuery() {
		query = "DELETE FROM CatalogType WHERE catalogId = " + catalogId;
	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
	}

	public String getQuery() {
		return query;
	}

	public TwoKeyHashMap<Integer, String, Catalog> refreshCatalogTable() throws DatabaseException {
		queryType = READ_CATALOG;
		dataAccessSS.atomicRead(this);

		// Return a clone since productTable must not be corrupted
		return catalogTable.clone();
	}

	public TwoKeyHashMap<Integer, String, Catalog> readCatalogTable() throws DatabaseException {
		if (catalogTable != null) {
			return catalogTable.clone();
		}
		return refreshCatalogTable();
	}

	public Catalog readCatalog(Integer catalogId) throws DatabaseException {
		if (catalogTable != null && catalogTable.isAFirstKey(catalogId)) {
			return catalogTable.getValWithFirstKey(catalogId);
		}
		queryType = READ_CATALOG;
		this.catalogId = catalogId;
		dataAccessSS.atomicRead(this);
		return catalog;
	}

	void populateCatalogList(ResultSet rs) throws DatabaseException {
		catalogList = new LinkedList<Catalog>();
		if (rs != null) {
			try {
				while (rs.next()) {
					catalog = new CatalogImpl();
					String str = rs.getString(catalogName);
					catalog.setName(str);
					catalogList.add(catalog);
				}
			} catch (SQLException e) {
				throw new DatabaseException(e);
			}
		}
	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		if (queryType.equals(READ_CATALOG)) {
			populateCatalogList(resultSet);
		}

	}

	public List<Catalog> getCatalogList() {
		return catalogList;
	}

}
