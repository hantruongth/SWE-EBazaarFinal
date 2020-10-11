
package business.ordersubsystem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderItem;
import business.externalinterfaces.ShoppingCart;
import middleware.DbConfigProperties;
import middleware.dataaccess.DataAccessSubsystemFacade;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessSubsystem;
import middleware.externalinterfaces.DbClass;
import middleware.externalinterfaces.DbConfigKey;

class DbClassOrder implements DbClass {
	private static final Logger LOG = Logger.getLogger(DbClassOrder.class.getPackage().getName());
	private DataAccessSubsystem dataAccessSS = new DataAccessSubsystemFacade();
	private String query;
	private String queryType;
	private final String GET_ORDER_ITEMS = "GetOrderItems";
	private final String GET_ORDER_IDS = "GetOrderIds";
	private final String GET_ORDER_DATA = "GetOrderData";
	private final String SUBMIT_ORDER = "SubmitOrder";
	private final String SUBMIT_ORDER_ITEM = "SubmitOrderItem";
	private CustomerProfile custProfile;
	private Integer orderId;
	private List<Integer> orderIds;
	private List<OrderItem> orderItems;
	private OrderImpl orderData;
	private OrderItem orderItem;
	private Order order;

	DbClassOrder() {
	}

	DbClassOrder(OrderImpl order) {
		this.order = order;
	}

	DbClassOrder(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	DbClassOrder(CustomerProfile custProfile) {
		this.custProfile = custProfile;
	}

	DbClassOrder(OrderImpl order, CustomerProfile custProfile) {
		this(order);
		this.custProfile = custProfile;
	}

	List<Integer> getAllOrderIds(CustomerProfile custProfile) throws DatabaseException {
		this.custProfile = custProfile;
		queryType = GET_ORDER_IDS;
		dataAccessSS.atomicRead(this);
		return Collections.unmodifiableList(orderIds);
	}

	OrderImpl getOrderData(Integer orderId) throws DatabaseException {
		queryType = GET_ORDER_DATA;
		this.orderId = orderId;
		dataAccessSS.atomicRead(this);
		return orderData;
	}

	// Precondition: CustomerProfile has been set by the constructor
	void submitOrder(ShoppingCart shopCart) throws DatabaseException {
		// implement DatTX
		try {
			dataAccessSS.createConnection(this);
			dataAccessSS.startTransaction();
			orderData = new OrderImpl();
			orderData.setDate(LocalDate.now());
			orderItems = new ArrayList<OrderItem>();
			List<Integer> orders = getAllOrderIds(custProfile);
			orders.stream().sorted();
			int orderId = orders.get(orders.size() - 1) + 1;
			for (int i = 0; i < shopCart.getCartItems().size(); i++) {
				CartItem cartItem = shopCart.getCartItems().get(i);
				OrderItem item = new OrderItemImpl(cartItem.getProductName(), Integer.parseInt(cartItem.getQuantity()),
						Double.parseDouble(cartItem.getTotalprice()) / Integer.parseInt(cartItem.getQuantity()));
				item.setProductId(cartItem.getProductid());
				item.setOrderId(orderId);
				orderItems.add(item);
				submitOrderItem(item);
			}
			orderData.setOrderItems(orderItems);
			orderData.setShipAddress(shopCart.getShippingAddress());
			orderData.setBillAddress(shopCart.getBillingAddress());
			orderData.setPaymentInfo(shopCart.getPaymentInfo());
			orderData.setOrderId(orderId);
			order = orderData;
			submitOrderData();
		} catch (DatabaseException e) {
			dataAccessSS.rollback();
			throw e;
		} finally {
			dataAccessSS.releaseConnection();
		}
	}

	/** This is part of the general submitOrder method */
	private Integer submitOrderData() throws DatabaseException {
		queryType = SUBMIT_ORDER;
		// creation and release of connection handled by submitOrder
		return dataAccessSS.save();
	}

	/** This is part of the general submitOrder method */
	private void submitOrderItem(OrderItem item) throws DatabaseException {
		this.orderItem = item;
		queryType = SUBMIT_ORDER_ITEM;
		// creation and release of connection handled by submitOrder
		dataAccessSS.save();
	}

	public List<OrderItem> getOrderItems(Integer orderId) throws DatabaseException {
		queryType = GET_ORDER_ITEMS;
		this.orderId = orderId;
		dataAccessSS.atomicRead(this);
		return Collections.unmodifiableList(orderItems);
	}

	public void buildQuery() {
		if (queryType.equals(GET_ORDER_ITEMS)) {
			buildGetOrderItemsQuery();
		} else if (queryType.equals(GET_ORDER_IDS)) {
			buildGetOrderIdsQuery();
		} else if (queryType.equals(GET_ORDER_DATA)) {
			buildGetOrderDataQuery();
		} else if (queryType.equals(SUBMIT_ORDER)) {
			buildSaveOrderQuery();
		} else if (queryType.equals(SUBMIT_ORDER_ITEM)) {
			buildSaveOrderItemQuery();
		}
	}

	private void buildSaveOrderQuery() {
		Address shipAddr = order.getShipAddress();
		Address billAddr = order.getBillAddress();
		CreditCard cc = order.getPaymentInfo();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String formattedDate = order.getOrderDate().format(fmt);
		query = "INSERT into Ord "
				+ "(orderid, custid, shipaddress1, shipcity, shipstate, shipzipcode, billaddress1, billcity, billstate,"
				+ "billzipcode, nameoncard,  cardnum,cardtype, expdate, orderdate, totalpriceamount)" + "VALUES(NULL,"
				+ custProfile.getCustId() + ",'" + shipAddr.getStreet() + "','" + shipAddr.getCity() + "','"
				+ shipAddr.getState() + "','" + shipAddr.getZip() + "','" + billAddr.getStreet() + "','"
				+ billAddr.getCity() + "','" + billAddr.getState() + "','" + billAddr.getZip() + "','"
				+ cc.getNameOnCard() + "','" + cc.getCardNum() + "','" + cc.getCardType() + "','"
				+ cc.getExpirationDate() + "','" + formattedDate + "'," + order.getTotalPrice() + ")";
	}

	private void buildSaveOrderItemQuery() {
		// implement DatTX
		query = "INSERT INTO AccountsDb.OrderItem "
				+ "(orderitemid, orderid, productid, quantity, totalprice, shipmentcost, taxamount) " + "VALUES(NULL, "
				+ orderItem.getOrderId() + "," + orderItem.getProductId() + "," + orderItem.getQuantity() + ",'"
				+ orderItem.getTotalPrice() + "','" + 0 + "','" + 0 + "')";
	}

	private void buildGetOrderDataQuery() {
		query = "SELECT orderdate, totalpriceamount FROM AccountsDb.Ord WHERE orderid = " + orderId;
	}

	private void buildGetOrderIdsQuery() {
		query = "SELECT orderid FROM AccountsDb.Ord WHERE custid = " + custProfile.getCustId();
	}

	private void buildGetOrderItemsQuery() {
		query = "SELECT ProductsDb.Product.productname, " + "ProductsDb.Product.priceperunit as unitprice, "
				+ "AccountsDb.OrderItem.quantity " + "from AccountsDb.OrderItem INNER JOIN ProductsDb.Product "
				+ "where ProductsDb.Product.productid = AccountsDb.OrderItem.productid "
				+ "and AccountsDb.OrderItem.orderid = " + orderId;
	}

	private void populateOrderItems(ResultSet rs) throws DatabaseException {
		// implement DatTX
		orderItems = new LinkedList<OrderItem>();
		try {
			while (rs.next()) {
				String name = rs.getString("productname");
				int quantity = rs.getInt("quantity");
				double price = rs.getDouble("unitprice");
				OrderItemImpl item = new OrderItemImpl(name, quantity, price);
				orderItems.add(item);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	private void populateOrderIds(ResultSet resultSet) throws DatabaseException {
		orderIds = new LinkedList<Integer>();
		try {
			while (resultSet.next()) {
				orderIds.add(resultSet.getInt("orderid"));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	private void populateOrderData(ResultSet resultSet) throws DatabaseException {
		// implement DatTX
		orderData = new OrderImpl();
		try {
			while (resultSet.next()) {
//            	LocalDate date = resultSet.getDate("orderdate").toLocalDate();
				String dateString = resultSet.getString("orderdate");
				LocalDate localDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
				String amount = resultSet.getString("totalpriceamount");
				orderData.setDate(localDate);
				orderData.setOrderId(orderId);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}

	public void populateEntity(ResultSet resultSet) throws DatabaseException {
		if (queryType.equals(GET_ORDER_ITEMS)) {
			populateOrderItems(resultSet);
		} else if (queryType.equals(GET_ORDER_IDS)) {
			populateOrderIds(resultSet);
		} else if (queryType.equals(GET_ORDER_DATA)) {
			populateOrderData(resultSet);
		}
	}

	public String getDbUrl() {
		DbConfigProperties props = new DbConfigProperties();
		return props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	}

	public String getQuery() {
		return query;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
