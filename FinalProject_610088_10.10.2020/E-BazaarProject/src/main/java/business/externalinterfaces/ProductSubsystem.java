
package business.externalinterfaces;

import java.util.List;

import business.exceptions.BackendException;
import business.util.TwoKeyHashMap;

public interface ProductSubsystem {

	public int readQuantityAvailable(Product product);

	/** obtains product for a given product name */
	public Product getProductFromName(String prodName) throws BackendException;

	/** reads the product from the productid */
	public Product getProductFromId(Integer prodId) throws BackendException;

	public List<Catalog> getCatalogList() throws BackendException;

	/** gets a list of products from the database, based on catalog */
	public List<Product> getProductList(Catalog catalog) throws BackendException;

	public Integer getProductIdFromName(String prodName) throws BackendException;

	/** retrieves list of catalogs from database */
	public Catalog getCatalogFromName(String catName) throws BackendException;

	/** saves newly created catalog */
	public void saveNewCatalog(Catalog catalog) throws BackendException;

	/** saves a new product obtained from user input */
	public void saveNewProduct(Integer catalogId, Product product) throws BackendException;

	/** deletes a product obtained from user input */
	public void deleteProduct(Product product) throws BackendException;

	/** deletes a catalog obtained from user input */
	public void deleteCatalog(Catalog catalog) throws BackendException;

	/* test stuff */
	public DbClassCatalogForTest getGenericDbClassCatalogs();

	public TwoKeyHashMap<Integer, String, Product> getProductTable() throws BackendException;

}