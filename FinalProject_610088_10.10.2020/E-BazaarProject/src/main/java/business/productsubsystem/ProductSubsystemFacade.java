package business.productsubsystem;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.DbClassCatalogForTest;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.util.TwoKeyHashMap;
import middleware.exceptions.DatabaseException;

public class ProductSubsystemFacade implements ProductSubsystem {
	private static final Logger LOGGER = Logger.getLogger(ProductSubsystemFacade.class.getName());

	public static Catalog createCatalog(int id, String name) {
		return new CatalogImpl(id, name);
	}

	public static Product createProduct(Catalog c, String name, LocalDate date, int numAvail, double price) {
		return new ProductImpl(c, name, date, numAvail, price);
	}

	public static Product createProduct(Catalog c, Integer pi, String pn, int qa, double up, LocalDate md,
			String desc) {
		return new ProductImpl(c, pi, pn, qa, up, md, desc);
	}

	/** obtains product for a given product name */
	public Product getProductFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(getProductIdFromName(prodName));
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while getting product by name", e);
			throw new BackendException(e);
		}
	}

	public Integer getProductIdFromName(String prodName) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			TwoKeyHashMap<Integer, String, Product> table = dbclass.readProductTable();
			return table.getFirstKey(prodName);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while getting product by ID from name", e);
			throw new BackendException(e);
		}

	}

	public Product getProductFromId(Integer prodId) throws BackendException {

		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(prodId);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while getting product by ID", e);
			throw new BackendException(e);
		}
	}

	/* @added by Tauseef */
	public List<Catalog> getCatalogList() throws BackendException {
		try {
			DbClassCatalogTypes dbClass = new DbClassCatalogTypes();
			return dbClass.getCatalogTypes().getCatalogs();
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while getting catalog list", e);
			throw new BackendException(e);
		}

	}

	/* @added by Tauseef */
	public List<Product> getProductList(Catalog catalog) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProductList(catalog);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while getting product list", e);
			throw new BackendException(e);
		}
	}

	/* @added by Tauseef */
	public int readQuantityAvailable(Product product) {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProduct(product.getProductId()).getQuantityAvail();
		} catch (DatabaseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/* @added by Tauseef */
	@Override
	public Catalog getCatalogFromName(String catName) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			return dbclass.readCatalog(getCatalogIdFromName(catName));
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while getting catalog from name", e);
			throw new BackendException(e);
		}
	}

	/* @added by Tauseef */
	public Integer getCatalogIdFromName(String catName) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			TwoKeyHashMap<Integer, String, Catalog> table = dbclass.readCatalogTable();
			return table.getFirstKey(catName);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while getting catalog ID by name", e);
			throw new BackendException(e);
		}

	}

	/* @added by Tauseef */
	@Override
	public void saveNewCatalog(Catalog catalog) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			dbclass.saveNewCatalog(catalog);
			;
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while saving new catalog", e);
			throw new BackendException(e);
		}
	}

	/* @added by Tauseef */
	@Override
	public void saveNewProduct(Integer catalogId, Product product) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			dbclass.saveNewProduct(catalogId, product);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while saving new product", e);
			throw new BackendException(e);
		}
	}

	/* @added by Tauseef */
	@Override
	public void deleteProduct(Product product) throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			dbclass.deleteProduct(product);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while deleting product", e);
			throw new BackendException(e);
		}

	}

	/* @added by Tauseef */
	@Override
	public void deleteCatalog(Catalog catalog) throws BackendException {
		try {
			DbClassCatalog dbclass = new DbClassCatalog();
			dbclass.deleteCatalog(catalog);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "DB Exception occured while deleting catalog", e);
			throw new BackendException(e);
		}

	}

	@Override
	public DbClassCatalogForTest getGenericDbClassCatalogs() {
		return new DbClassCatalogTypes();
	}

	// DatTX
	@Override
	public TwoKeyHashMap<Integer, String, Product> getProductTable() throws BackendException {
		try {
			DbClassProduct dbclass = new DbClassProduct();
			return dbclass.readProductTable();
		} catch (DatabaseException e) {
			throw new BackendException(e);
		}
	}
}
