package presentation.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import business.CartItemData;
import business.Util;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.productsubsystem.ProductSubsystemFacade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import presentation.gui.GuiUtils;

public enum ManageProductsData {
	INSTANCE;

	private CatalogPres defaultCatalog = readDefaultCatalogFromDataSource();

	private CatalogPres readDefaultCatalogFromDataSource() {
		return readCatalogsFromDataSource().get(0);
	}

	public CatalogPres getDefaultCatalog() {
		return defaultCatalog;
	}

	private CatalogPres selectedCatalog = defaultCatalog;

	public void setSelectedCatalog(CatalogPres selCatalog) {
		selectedCatalog = selCatalog;
	}

	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}

//	private CatalogPres theSelectedCatalog = defaultCatalog;
//	public void setTheSelectedCatalog(CatalogPres selCatalog) {
//		theSelectedCatalog = selCatalog;
//	}
//	public CatalogPres getTheSelectedCatalog() {
//		return theSelectedCatalog;
//	}
	//////////// Products List model
	private ObservableList<ProductPres> productsMap = readProductsFromDataSource();

	/** Initializes the productsMap */
	private ObservableList<ProductPres> readProductsFromDataSource() {
		System.out.println("xxxx");
		ProductSubsystem productSub = new ProductSubsystemFacade();
		try {
			List<Product> products = productSub.getProductList(selectedCatalog.getCatalog());
			List<ProductPres> productPresList = new ArrayList<ProductPres>();
			for (int i = 0; i < products.size(); i++) {
				ProductPres orderPres = new ProductPres();
				orderPres.setProduct(products.get(i));
				productPresList.add(orderPres);
			}
			return FXCollections.observableList(productPresList);
		} catch (BackendException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Delivers the requested products list to the UI */
	public ObservableList<ProductPres> getProductsList(CatalogPres catPres) {
		setSelectedCatalog(catPres);
		return readProductsFromDataSource();
	}

	public ProductPres productPresFromData(Catalog c, String name, String date, // MM/dd/yyyy
			int numAvail, double price) {

		Product product = ProductSubsystemFacade.createProduct(c, name, GuiUtils.localDateForString(date), numAvail,
				price);
		ProductPres prodPres = new ProductPres();
		prodPres.setProduct(product);
		return prodPres;
	}

	public void addToProdList(CatalogPres catPres, ProductPres prodPres) {

		ProductSubsystem pSs = new ProductSubsystemFacade();
		try {
			pSs.saveNewProduct(catPres.getCatalog().getId(), prodPres.getProduct());

			ObservableList<ProductPres> newProducts = FXCollections.observableArrayList(prodPres);
			List<ProductPres> specifiedProds = productsMap;
			specifiedProds.addAll(newProducts);
		} catch (BackendException e) {
			e.printStackTrace();
		}

		// Place the new item at the bottom of the list

	}

	/**
	 * This method looks for the 0th element of the toBeRemoved list and if found,
	 * removes it. In this app, removing more than one product at a time is not
	 * supported.
	 */
	public boolean removeFromProductList(CatalogPres cat, ObservableList<ProductPres> toBeRemoved) {
		boolean result = false;
		ProductPres item = toBeRemoved.get(0);
		if (toBeRemoved != null && !toBeRemoved.isEmpty()) {
			ProductSubsystem pSs = new ProductSubsystemFacade();
			try {
				pSs.deleteProduct(item.getProduct());
			} catch (BackendException e) {
				e.printStackTrace();
			}
			return result = productsMap.remove(item);

		}
		return false;
	}

	//////// Catalogs List model
	private ObservableList<CatalogPres> catalogList = readCatalogsFromDataSource();

	/** Initializes the catalogList */

	private ObservableList<CatalogPres> readCatalogsFromDataSource() {
		ProductSubsystem productSub = new ProductSubsystemFacade();
		try {
			List<Catalog> catalogs = productSub.getCatalogList();
			List<CatalogPres> catalogPresList = new ArrayList<CatalogPres>();
			for (int i = 0; i < catalogs.size(); i++) {
				CatalogPres orderPres = new CatalogPres();
				orderPres.setCatalog(catalogs.get(i));
				catalogPresList.add(orderPres);
			}
			return FXCollections.observableList(catalogPresList);
		} catch (BackendException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Delivers the already-populated catalogList to the UI */
	public ObservableList<CatalogPres> getCatalogList() {
		return catalogList;
	}

	public CatalogPres catalogPresFromData(int id, String name) {
		Catalog cat = ProductSubsystemFacade.createCatalog(id, name);
		CatalogPres catPres = new CatalogPres();
		catPres.setCatalog(cat);
		return catPres;
	}

	public void addToCatalogList(CatalogPres catPres) {
		ObservableList<CatalogPres> newCatalogs = FXCollections.observableArrayList(catPres);

		// Place the new item at the bottom of the list
		// catalogList is guaranteed to be non-null
		ProductSubsystem pSs = new ProductSubsystemFacade();
		try {
			pSs.saveNewCatalog(catPres.getCatalog());
			boolean result = catalogList.addAll(newCatalogs);
			if (result) { // must make this catalog accessible in productsMap
				productsMap = FXCollections.observableList(new ArrayList<ProductPres>());
			}
		} catch (BackendException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method looks for the 0th element of the toBeRemoved list in catalogList
	 * and if found, removes it. In this app, removing more than one catalog at a
	 * time is not supported.
	 * 
	 * This method also updates the productList by removing the products that belong
	 * to the Catalog that is being removed.
	 * 
	 * Also: If the removed catalog was being stored as the selectedCatalog, the
	 * next item in the catalog list is set as "selected"
	 */
	public boolean removeFromCatalogList(ObservableList<CatalogPres> toBeRemoved) {
		boolean result = false;
		CatalogPres item = toBeRemoved.get(0);
		if (toBeRemoved != null && !toBeRemoved.isEmpty()) {
			ProductSubsystem pSs = new ProductSubsystemFacade();
			try {
				pSs.deleteCatalog(item.getCatalog());
			} catch (BackendException e) {
				e.printStackTrace();
			}
			result = catalogList.remove(item);
		}
		if (item.equals(selectedCatalog)) {
			if (!catalogList.isEmpty()) {
				selectedCatalog = catalogList.get(0);
			} else {
				selectedCatalog = null;
			}
		}
		if (result) {// update productsMap
			productsMap.remove(item);
		}
		return result;
	}

	// Synchronizers
	public class ManageProductsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			productsMap = list;
		}
	}

	public ManageProductsSynchronizer getManageProductsSynchronizer() {
		return new ManageProductsSynchronizer();
	}

	private class ManageCatalogsSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			catalogList = list;
		}
	}

	public ManageCatalogsSynchronizer getManageCatalogsSynchronizer() {
		return new ManageCatalogsSynchronizer();
	}
}
