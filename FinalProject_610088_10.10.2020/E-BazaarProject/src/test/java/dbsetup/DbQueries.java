package dbsetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import customersubsystem.AddressImpl;
import customersubsystem.CustomerSubsystemFacade;
import middleware.DbConfigProperties;
import middleware.externalinterfaces.DbConfigKey;
import alltests.AllTests;
import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.Order;
import business.externalinterfaces.ShoppingCart;
import business.ordersubsystem.OrderSubsystemFacade;
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.CartItemImpl;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
public class DbQueries {
	static {
		AllTests.initializeProperties();
	}
	static final DbConfigProperties PROPS = new DbConfigProperties();
	static Connection con = null;
	static Statement stmt = null;
	static final String USER = PROPS.getProperty(DbConfigKey.DB_USER.getVal()); 
    static final String PWD = PROPS.getProperty(DbConfigKey.DB_PASSWORD.getVal()); 
    static final String DRIVER = PROPS.getProperty(DbConfigKey.DRIVER.getVal());
    static final int MAX_CONN = Integer.parseInt(PROPS.getProperty(DbConfigKey.MAX_CONNECTIONS.getVal()));
    static final String PROD_DBURL = PROPS.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    static final String ACCT_DBURL = PROPS.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
	static Connection prodCon = null;
	static Connection acctCon = null;
    String insertStmt = "";
	String selectStmt = "";
	
	/* Connection setup */
	static {
		try {
			Class.forName(DRIVER);
		}
		catch(ClassNotFoundException e){
			//debug
			e.printStackTrace();
		}
		try {
			prodCon = DriverManager.getConnection(PROD_DBURL, USER, PWD);
			acctCon = DriverManager.getConnection(ACCT_DBURL, USER, PWD);
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	// just to test this class
	public static void testing() {
		try {
			stmt = prodCon.createStatement();
			stmt.executeQuery("SELECT * FROM Product");
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//////////////// The public methods to be used in the unit tests ////////////
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - product id
	 * 2 - product name
	 */
	public static String[] insertProductRow() {
		String[] vals = saveProductSql();
		String query = vals[0];
		try {
			stmt = prodCon.createStatement();
			
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - catalog id
	 * 2 - catalog name
	 */
	public static List<Address> readCustAddresses() {
		String query = readAddressesSql();
		List<Address> addressList = new LinkedList<Address>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			    
                while(rs.next()) {
                    
                    String street = rs.getString("street");
                    String city = rs.getString("city");
                    String state = rs.getString("state");
                    String zip = rs.getString("zip");
                    
             
                    Address addr 
                      = CustomerSubsystemFacade.createAddress(street,city,state,zip,true,true);
                   
                    addressList.add(addr);
                }  
                stmt.close();
                
                    
	            
		}
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return addressList;
		
	}
	
	
	
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - catalog id
	 * 2 - catalog name
	 */
	public static String[] insertCatalogRow() {
		String[] vals = saveCatalogSql();
		String query = vals[0];
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - address id
	 * 2 - address
	 */
	public static String[] insertAddressRow() {
		String[] vals = saveAddressSql();
		String query = vals[0];
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (rs.getString(2));
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - customer id
	 * 2 - cust fname
	 * 3 - cust lname
	 */
	public static String[] insertCustomerRow() {
		String[] vals = saveCustomerSql();
		String query = vals[0];
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	public static void deleteCatalogRow(Integer catId) {
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(deleteCatalogSql(catId));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deleteAddressRow(String street) {
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(deleteAddressSql(street));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deleteProductRow(Integer prodId) {
		try {
			stmt = prodCon.createStatement();
			stmt.executeUpdate(deleteProductSql(prodId));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deleteCustomerRow(Integer custId) {
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(deleteCustomerSql(custId));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteOrderRow(Integer orderId) {
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(deleteOrderSql(orderId));
			stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	///queries
	public static String readAddressesSql() {
		return "SELECT * from altaddress WHERE custid = 1";
	}
	
	
	public static String[] saveAddressSql() {
		String[] vals = new String[3];
		
		String name = "testaddress";
		vals[0] = "INSERT into altaddress (addressid,custid,street,city,state,zip) VALUES(NULL,1,'xxx','yyy','zzz','1232')";  
		vals[1] = "xxx";
		vals[2] = name;
		return vals;
	}
	public static String[] saveCatalogSql() {
		String[] vals = new String[3];
		
		String name = "testcatalog";
		vals[0] =
		"INSERT into CatalogType "+
		"(catalogid,catalogname) " +
		"VALUES(NULL, '" + name+"')";	  
		vals[1] = null;
		vals[2] = name;
		return vals;
	}
	public static String[] saveProductSql() {
		String[] vals = new String[3];
		String name = "testprod";
		vals[0] =
		"INSERT into Product "+
		"(productid,productname,totalquantity,priceperunit,mfgdate,catalogid,description) " +
		"VALUES(NULL, '" +
				  name+"',1,1,'12/12/2015',1,'test')";				  
		vals[1] = null;
		vals[2] = name;
		return vals;
	}
	public static String[] saveCustomerSql() {
		String[] vals = new String[4];
		String fname = "testf";
		String lname = "testl";
		vals[0] =
		"INSERT into Customer "+
		"(custid,fname,lname) " +
		"VALUES(NULL,'" +
				  fname+"','"+ lname+"')";
				  
		vals[2] = fname;
		vals[3] = lname;
		return vals;
	}
	
	//DatTX
	public static String[] saveOrderSql() {
		String custid = "1";
		String query = "INSERT into Ord "+
		        "(orderid, custid, orderdate)" +
		        "VALUES(NULL," + custid + ", '10/10/2015')"; 
		String[] vals = new String[2];
		vals[0] = query;
		vals[1] = custid;
		return vals;
	}
	public static String deleteProductSql(Integer prodId) {
		return "DELETE FROM Product WHERE productid = "+prodId;
	}
	public static String deleteAddressSql(String streetAd) {
		return "DELETE FROM AltAddress WHERE street = '"+streetAd+"'";
	}
	public static String deleteCatalogSql(Integer catId) {
		return "DELETE FROM CatalogType WHERE catalogid = "+catId;
	}
	public static String deleteOrderSql(Integer orderId) {
		return "DELETE FROM Ord WHERE orderid = "+orderId;
	}
	
	public static String deleteCustomerSql(Integer custId) {
		return "DELETE FROM Customer WHERE custid = "+custId;
	}
	
	public static void main(String[] args) {
		readAddressesSql();
		//System.out.println(System.getProperty("user.dir"));
		/*
		String[] results = DbQueries.insertCustomerRow();
		System.out.println("id = " + results[1]);
		DbQueries.deleteCustomerRow(Integer.parseInt(results[1]));
		results = DbQueries.insertCatalogRow();
		System.out.println("id = " + Integer.parseInt(results[1]));
		DbQueries.deleteCatalogRow(Integer.parseInt(results[1]));*/
	}
	/**
	 * Returns a String[] with values:
	 * 0 - query
	 * 1 - customer id
	 */
	public static String[] insertOrderRow() {
		String[] vals = saveOrderSql();
		String query = vals[0];
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				vals[1] = (new Integer(rs.getInt(1)).toString());
			}
			stmt.close();
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return vals;
	}
	
	
	public static String readOrdersSql() {
		return "SELECT * from Ord WHERE custid = 1";
	}
	/**
	 * Returns a list of order:
	 */
	public static List<Order> readOrders() {
		String query = readOrdersSql();
		List<Order> orderList = new LinkedList<Order>();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				int orderId = rs.getInt("orderId");
				String orderDate = rs.getString("orderDate");
				String totalPrice = rs.getString("totalPriceamount");
				Order order = OrderSubsystemFacade.createOrder(orderId,
						orderDate, totalPrice);
				orderList.add(order);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}
	public static List<Catalog> readCustCatalogs() {
		String query = readCatalogsSql();
		List<Catalog> catalogList = new LinkedList<Catalog>();
		try {
			stmt = prodCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
	    	System.out.println(rs);

                while(rs.next()) {
                    
                    Integer catalogid = rs.getInt("catalogid");
                    String catalogname = rs.getString("catalogname");
                    
             
                    Catalog catalog = ProductSubsystemFacade.createCatalog(catalogid, catalogname);
                   
                    catalogList.add(catalog);
                }  
                stmt.close();
                
		}
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return catalogList;
		
	}
	public static String readCatalogsSql() {
		return "SELECT * FROM CatalogType";
	}
	public static String[] insertCartRow(Integer customerId, double totalamountcharged) {
        String[] vals = saveCartSql(customerId, totalamountcharged);
        String query = vals[0];
        try {
            stmt = acctCon.createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                vals[1] = rs.getString(1);
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vals;
    }

	public static String[] saveCartSql(Integer customerId,
			double totalamountcharged) {
		String[] vals = new String[5];
		vals[0] = "INSERT into shopcarttbl(shopcartid,custid,totalpriceamount,totalamountcharged) "
				+ "VALUES(NULL,"
				+ customerId
				+ ","
				+ totalamountcharged
				+ ","
				+ totalamountcharged + ")";
		vals[2] = customerId.toString();
		vals[3] = Double.toString(totalamountcharged);
		vals[4] = Double.toString(totalamountcharged);
		return vals;
	}

	public static void deleteCartRow(String cartId) {
		try {
			stmt = acctCon.createStatement();
			stmt.executeUpdate(deleteCartSql(cartId));
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String deleteCartSql(String cartId) {
		return "DELETE FROM shopcarttbl WHERE shopcartid = " + cartId;
	}
	
	public static Address[] readDefaultShippingBilling() {
		String query = readDefaultShippingBillingQuery();
		Address[] addresses = new AddressImpl[2];
		
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				  String shipaddress1 = rs.getString("shipaddress1");
                  //String shipaddress2 = rs.getString("shipaddress2");
                  String shipcity = rs.getString("shipcity");
                  String shipstate = rs.getString("shipstate");
                  String shipzipcode = rs.getString("shipzipcode");
                  Address shipAddress = CustomerSubsystemFacade.createAddress(shipaddress1, shipcity, shipstate, shipzipcode, true, false);
                  		
                  String billaddress1 = rs.getString("billaddress1");
                  //String billaddress2 = rs.getString("billaddress2");
                  String billcity = rs.getString("billcity");
                  String billstate = rs.getString("billstate");
                  String billzipcode = rs.getString("billzipcode");
                  Address billAddress = CustomerSubsystemFacade.createAddress(billaddress1, billcity, billstate, billzipcode, false, true);
                  
                  addresses[0] = shipAddress;
                  addresses[1]=billAddress;
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addresses;
	}
	
	public static String readDefaultShippingBillingQuery(){
		return "SELECT * FROM Customer WHERE custid =1";
	}
	
	public static ShoppingCart readSavedCart() {
		String query = readShoppingCartSql();
		ShoppingCart shoppingCart= ShoppingCartSubsystemFacade.INSTANCE.getEmptyCartForTest();
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                	String shopcartid = rs.getString("shopcartid");
                	
                    String shipaddress1 = rs.getString("shipaddress1");
                    //String shipaddress2 = rs.getString("shipaddress2");
                    String shipcity = rs.getString("shipcity");
                    String shipstate = rs.getString("shipstate");
                    String shipzipcode = rs.getString("shipzipcode");
                    Address shipAddress = CustomerSubsystemFacade.createAddress(shipaddress1, shipcity, shipstate, shipzipcode, true, false);
                    		
                    String billaddress1 = rs.getString("billaddress1");
                    //String billaddress2 = rs.getString("billaddress2");
                    String billcity = rs.getString("billcity");
                    String billstate = rs.getString("billstate");
                    String billzipcode = rs.getString("billzipcode");
                    Address billAddress = CustomerSubsystemFacade.createAddress(billaddress1, billcity, billstate, billzipcode, false, true);
                    
                    String nameoncard = rs.getString("nameoncard");
                    String expdate = rs.getString("expdate");
                    String cardtype = rs.getString("cardtype");
                    String cardnum = rs.getString("cardnum");
                    CreditCard creditCard = CustomerSubsystemFacade.createCreditCard(nameoncard, expdate, cardnum, cardtype);
                    
                    shoppingCart.setBillAddress(billAddress);
                    shoppingCart.setShipAddress(shipAddress);
                    shoppingCart.setPaymentInfo(creditCard);
                    shoppingCart.setCartItems(getCartItems(shopcartid));
                }  
                stmt.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
			
		}
		return shoppingCart;
	}
	
	public static String readShoppingCartSql(){
		return "SELECT * FROM ShopCartTbl WHERE custid =1";
	}
	
	private static List<CartItem> getCartItems(String shopcartid){
		String query = readCartItemsQuery(shopcartid);
		List<CartItem> cartItems = new LinkedList<CartItem>();
		
		try {
			stmt = acctCon.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
				CartItem item = new CartItemImpl();
				cartItems.add(item);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cartItems;
	}
	
	public static String readCartItemsQuery(String cartId){
		return "SELECT * FROM ShopCartItem WHERE shopcartid="+cartId;
	}

}
