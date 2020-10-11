package alltests;

import java.io.File;
import java.util.logging.Logger;

import business.externalinterfaces.RulesConfigProperties;
import junit.framework.Test;
import junit.framework.TestSuite;
import middleware.DbConfigProperties;


public class AllTests extends TestSuite {
    static Logger log = Logger.getLogger(AllTests.class.getName());
    private static final String LOC_DB_PROPS = "dbconfig.properties";
    private static final String LOC_RULES_PROPS = "rulesconfig.properties";
    private static final String context = computeDir();

    static {
        log.info("Looking here: " + context + LOC_DB_PROPS);
        initializeProperties();
    }

    private static String computeDir() {
        File f = new File(System.getProperty("user.dir"));
        if (f.exists() && f.isDirectory()) {
            System.out.println(f.getParent());
            return f.getParent();
        }
        return null;

    }

    @SuppressWarnings("unused")
    private static boolean initialized = false;

    public static void initializeProperties() {
        // Need to specify full path to dbconfig.properties
        // when accessing from outside the project.
        if (!initialized) {
            //DbConfigProperties.readProps(context + LOC_DB_PROPS);
            DbConfigProperties.readProps("dbconfig.properties");
            //RulesConfigProperties.readProps(context + LOC_RULES_PROPS);
            RulesConfigProperties.readProps("rulesconfig.properties");
            initialized = true;
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        //$JUnit-BEGIN$ -- put fully qualified classnames of all tests here
        suite.addTest(new TestSuite(integrationtests.BrowseAndSelectTest.class));
        suite.addTest(new TestSuite(daotests.DbClassAddressTest.class));
        suite.addTest(new TestSuite(subsystemtests.ProductSubsystemTest.class));
        suite.addTest(new TestSuite(performancetests.RulesPerformanceTests.class));
        suite.addTest(new TestSuite(unittests.business.StringParseTest.class));
        suite.addTest(new TestSuite(unittests.middleware.dataaccess.SimpleConnectionPoolTest.class));

        //$JUnit-END$
        return suite;
    }


}

