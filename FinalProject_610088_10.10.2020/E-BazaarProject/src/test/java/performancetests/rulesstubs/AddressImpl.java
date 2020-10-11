
package performancetests.rulesstubs;


import business.externalinterfaces.Address;


public class AddressImpl implements Address{
    
    public AddressImpl(String street, String city, 
    		String state, String zip, boolean isShippingAddress, boolean isBillingAddress){
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.isShippingAddress = isShippingAddress;
        this.isBillingAddress = isBillingAddress;
    }
    public AddressImpl(String[] fields) {
    	this.street=fields[0];
    	this.city = fields[1];
    	this.state = fields[2];
    	this.zip = fields[3];
    	isShippingAddress = isBillingAddress = true;
    }
    private String street;   
    private String city;
    private String state;
    private String zip;
    private boolean isShippingAddress;
    private boolean isBillingAddress;
 
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

 
    public String getState() {
        return state;
    }
 
    public void setState(String state) {
        this.state = state;
    }
 
  
 
 
 

 

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
 
    public String toString() {
        String n = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();
        sb.append("Street: "+street+n);        
        sb.append("City: "+city+n);
        sb.append("State: "+state+n);
        sb.append("Zip: "+zip+n);
        return sb.toString();
    }

	

	
	
	@Override
	public String getStreet() {
		return street;
	}
	@Override
	public void setStreet(String s) {
		street = s;
		
	}
	@Override
	public boolean isShippingAddress() {
		return isShippingAddress;
	}
	@Override
	public boolean isBillingAddress() {
		
		return isBillingAddress;
	}
}
