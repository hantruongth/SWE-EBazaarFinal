package business.usecasecontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.bind.ValidationException;

import business.BusinessConstants;
import business.SessionCache;
import business.Util;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.RulesConfigKey;
import business.externalinterfaces.RulesConfigProperties;
import business.externalinterfaces.ShoppingCart;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.rulesbeans.FinalOrderBean;
import business.rulesbeans.ShopCartBean;
import rulesengine.OperatingException;
import rulesengine.ReteWrapper;

public enum CheckoutController {
	INSTANCE;

	private static final Logger LOG = Logger.getLogger(CheckoutController.class.getPackage().getName());

	public void runShoppingCartRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		// implement
		ShoppingCart cart = scss.getLiveCart();
		try {
			// set up
			RulesConfigProperties props = new RulesConfigProperties();
			String moduleName = props.getProperty(RulesConfigKey.SHOPCART_MODULE.getVal());
			BufferedReader rulesReader = Util.pathToRules(getClass().getClassLoader(),
					props.getProperty(RulesConfigKey.SHOPCART_RULES_FILE.getVal()));

			String deftemplateName = props.getProperty(RulesConfigKey.SHOPCART_DEFTEMPLATE.getVal());
			ShopCartBean shopCartBean = new ShopCartBean(cart);
			HashMap h = new HashMap();
			h.put(deftemplateName, shopCartBean);

			// start up the rules engine
			ReteWrapper engine = new ReteWrapper();
			engine.setRulesAsString(rulesReader);
			engine.setCurrentModule(moduleName);
			engine.setTable(h);
			engine.runRules();

		} catch (IOException ex) {
			throw new RuleException(ex.getMessage());
		} catch (OperatingException ex) {
			throw new RuleException(ex.getMessage());
		} catch (Exception ex) {
			throw new RuleException(ex.getMessage());
		}

	}

	public void runPaymentRules(Address addr, CreditCard cc) throws RuleException, BusinessException {
		// implement
	}

	public Address runAddressRules(Address addr) throws RuleException, BusinessException {
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		return cust.runAddressRules(addr);
	}

	/** Asks the ShoppingCart Subsystem to run final order rules */
	public void runFinalOrderRules(ShoppingCartSubsystem scss) throws RuleException, BusinessException {
		// implement DatTX
		ShoppingCart cart = scss.getLiveCart();
		try {
			// set up
			RulesConfigProperties props = new RulesConfigProperties();
			String moduleName = props.getProperty(RulesConfigKey.FINAL_ORDER_MODULE.getVal());
			BufferedReader rulesReader = Util.pathToRules(getClass().getClassLoader(),
					props.getProperty(RulesConfigKey.FINAL_ORDER_RULES_FILE.getVal()));

			String deftemplateName = props.getProperty(RulesConfigKey.FINAL_ORDER_DEFTEMPLATE.getVal());
			FinalOrderBean quantityBean = new FinalOrderBean(cart);
			HashMap h = new HashMap();
			h.put(deftemplateName, quantityBean);

			// start up the rules engine
			ReteWrapper engine = new ReteWrapper();
			engine.setRulesAsString(rulesReader);
			engine.setCurrentModule(moduleName);
			engine.setTable(h);
			engine.runRules();

		} catch (IOException ex) {
			throw new RuleException(ex.getMessage());
		} catch (OperatingException ex) {
			throw new RuleException(ex.getMessage());
		} catch (Exception ex) {
			throw new RuleException(ex.getMessage());
		}
	}

	/**
	 * Asks Customer Subsystem to check credit card against Credit Verification
	 * System
	 */
	public void verifyCreditCard() throws BusinessException {
		// implement
	}

	public void saveNewAddress(Address addr) throws BackendException {
		CustomerSubsystem cust = (CustomerSubsystem) SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
		cust.saveNewAddress(addr);
	}

	/** Asks Customer Subsystem to submit final order */
	public void submitFinalOrder() throws BackendException {
		// implement
	}

}
