package business.externalinterfaces;

import java.util.List;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

/* Used only for testing DbClassProducts */
public interface DbClassCatalogForTest extends DbClass {
	public List<Catalog> getCatalogList();
	// public void readAllProducts() throws DatabaseException;
}