package customersubsystem;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassAddressForTest;
import business.externalinterfaces.DbClassCustomerProfileForTest;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import middleware.creditverifcation.CreditVerificationFacade;
import middleware.exceptions.DatabaseException;
import middleware.exceptions.MiddlewareException;
import middleware.externalinterfaces.CreditVerification;

public class CustomerSubsystemFacade implements CustomerSubsystem {
	private static final Logger LOGGER = Logger.getLogger(CustomerSubsystemFacade.class.getName());
	// Subsystems
	ShoppingCartSubsystem shoppingCartSubsystem;
	OrderSubsystem orderSubsystem;
	// External Interfaces
	AddressImpl defaultShipAddress;
	AddressImpl defaultBillAddress;
	CreditCardImpl defaultPaymentInfo;
	CustomerProfileImpl customerProfile;
	// Attributes
	List<Order> orderHistory;

	/**
	 * Use for loading order history, default addresses, default payment info, saved
	 * shopping cart,cust profile after login
	 */
	public void initializeCustomer(Integer id, int authorizationLevel) throws BackendException {
		boolean isAdmin = (authorizationLevel >= 1);
		loadCustomerProfile(id, isAdmin);
		loadDefaultShipAddress();
		loadDefaultBillAddress();
		loadDefaultPaymentInfo();
		shoppingCartSubsystem = ShoppingCartSubsystemFacade.INSTANCE;
		shoppingCartSubsystem.setCustomerProfile(customerProfile);
		shoppingCartSubsystem.retrieveSavedCart();
		loadOrderData();
	}

	void loadCustomerProfile(int id, boolean isAdmin) throws BackendException {
		try {
			DbClassCustomerProfile dbclass = new DbClassCustomerProfile();
			dbclass.readCustomerProfile(id);
			customerProfile = dbclass.getCustomerProfile();
			customerProfile.setIsAdmin(isAdmin);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "Database Exception occur to load Customer Profile", e);
			throw new BackendException(e);
		}
	}

	void loadDefaultShipAddress() throws BackendException {
		DbClassAddress dbClassAddress = new DbClassAddress();
		try {
			dbClassAddress.readDefaultShipAddress(customerProfile);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "Database Exception occur to loadDefaultShipAddress", e);
			e.printStackTrace();
		}
		defaultShipAddress = dbClassAddress.getDefaultShipAddress();
	}

	void loadDefaultBillAddress() throws BackendException {
		DbClassAddress dbClassAddress = new DbClassAddress();
		try {
			dbClassAddress.readDefaultBillAddress(customerProfile);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "Database Exception occur to loadDefaultBillAddress", e);
			e.printStackTrace();
		}
		defaultBillAddress = dbClassAddress.getDefaultBillAddress();
	}

	void loadDefaultPaymentInfo() throws BackendException {
		DbClassCreditCard dbClassCreditCard = new DbClassCreditCard();
		try {
			dbClassCreditCard.readDefaultPaymentInfo(customerProfile);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "Database Exception occur to loadDefaultPaymentInfo", e);
			e.printStackTrace();
		}
		defaultPaymentInfo = dbClassCreditCard.getDefaultPaymentInfo();

	}

	void loadOrderData() throws BackendException {
		orderSubsystem = new OrderSubsystemFacade(customerProfile);
		orderHistory = orderSubsystem.getOrderHistory();
		LOGGER.info("total count of order history =  " + orderHistory.size());
	}

	public boolean isAdmin() {
		return customerProfile.isAdmin();
	}

	public void saveNewAddress(Address addr) throws BackendException {
		try {
			DbClassAddress dbClass = new DbClassAddress();
			dbClass.setAddress(addr);
			dbClass.saveAddress(customerProfile);
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "Database Exception occur to save New Address", e);
			throw new BackendException(e);

		}
	}

	public CustomerProfile getCustomerProfile() {

		return customerProfile;
	}

	public Address getDefaultShippingAddress() {
		return defaultShipAddress;
	}

	public Address getDefaultBillingAddress() {
		return defaultBillAddress;
	}

	public CreditCard getDefaultPaymentInfo() {
		return defaultPaymentInfo;
	}

	/**
	 * Use to supply all stored addresses of a customer when he wishes to select an
	 * address in ship/bill window
	 */
	public List<Address> getAllAddresses() throws BackendException {
		DbClassAddress dbClassAddress = new DbClassAddress();
		try {
			dbClassAddress.readAllAddresses(customerProfile);
			return dbClassAddress.getAddressList();
		} catch (DatabaseException e) {
			LOGGER.log(Level.SEVERE, "Database Exception occur to get All Addresses", e);
			throw new BackendException(e);
		}
	}

	public Address runAddressRules(Address addr) throws RuleException, BusinessException {

		Rules transferObject = new RulesAddress(addr);
		transferObject.runRules();
		AddressImpl update = (AddressImpl) transferObject.getUpdates().get(0);
		return update;
	}

	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		Rules transferObject = new RulesPayment(addr, cc);
		transferObject.runRules();
	}

	public static Address createAddress(String street, String city, String state, String zip, boolean isShip,
			boolean isBill) {
		return new AddressImpl(street, city, state, zip, isShip, isBill);
	}

	public static CustomerProfile createCustProfile(Integer custid, String firstName, String lastName,
			boolean isAdmin) {
		return new CustomerProfileImpl(custid, firstName, lastName, isAdmin);
	}

	public static CreditCard createCreditCard(String nameOnCard, String expirationDate, String cardNum,
			String cardType) {
		return new CreditCardImpl(nameOnCard, expirationDate, cardNum, cardType);
	}

	@Override
	public List<Order> getOrderHistory() {
		return Collections.unmodifiableList(orderHistory);
	}

	@Override
	public void setShippingAddressInCart(Address addr) {
		shoppingCartSubsystem.setShippingAddress(addr);

	}

	@Override
	public void setBillingAddressInCart(Address addr) {
		shoppingCartSubsystem.setBillingAddress(addr);

	}

	@Override
	public void setPaymentInfoInCart(CreditCard cc) {
		shoppingCartSubsystem.setPaymentInfo(cc);

	}

	@Override
	public void submitOrder() throws BackendException {
		orderSubsystem.submitOrder(shoppingCartSubsystem.getLiveCart());

	}

	@Override
	public void refreshAfterSubmit() throws BackendException {
		LOGGER.warning("Load Order Data may get fail after submit");
		loadOrderData();

	}

	@Override
	public ShoppingCartSubsystem getShoppingCart() {
		return shoppingCartSubsystem;
	}

	@Override
	public void saveShoppingCart() throws BackendException {
		if (shoppingCartSubsystem.getLiveCart().getShippingAddress() == null) {
			shoppingCartSubsystem.setShippingAddress(defaultShipAddress);
		}
		if (shoppingCartSubsystem.getLiveCart().getBillingAddress() == null) {
			shoppingCartSubsystem.setBillingAddress(defaultBillAddress);
		}
		if (shoppingCartSubsystem.getLiveCart().getPaymentInfo() == null) {
			shoppingCartSubsystem.setPaymentInfo(this.defaultPaymentInfo);
		}
		LOGGER.info("Going to save Live Shopping Cart");
		shoppingCartSubsystem.saveLiveCart();

	}

	@Override
	public void checkCreditCard(CreditCard cc) throws BusinessException {
		ShoppingCart shoppingCart = ShoppingCartSubsystemFacade.INSTANCE.getLiveCart();
		Address billingAddress = shoppingCart.getBillingAddress();
		CreditCard creditCard = shoppingCart.getPaymentInfo();
		Double amount = (double) 55;
		CreditVerification creditVerif = new CreditVerificationFacade();
		try {
			creditVerif.checkCreditCard(customerProfile, billingAddress, creditCard, new Double(amount));
		} catch (MiddlewareException e) {
			LOGGER.log(Level.SEVERE, "Middleware Exception occur to check Credit Card", e);
			throw new BusinessException(e);
		}

	}

	@Override
	public DbClassAddressForTest getGenericDbClassAddress() {
		return new DbClassAddress();
	}

	@Override
	public CustomerProfile getGenericCustomerProfile() {
		return new CustomerProfileImpl(1, "John", "Smith");
	}

	public void setCustomerProfile(CustomerProfile profile) {
		this.customerProfile = (CustomerProfileImpl) profile;
	}

	@Override
	public void loadDefaultCustomerData() {
		try {
			loadDefaultBillAddress();
			loadDefaultShipAddress();
			loadDefaultPaymentInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public DbClassCustomerProfileForTest getGenericDbClassCustomerProfile() {
		return new DbClassCustomerProfile();
	}
}
