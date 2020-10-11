package unittests.business;

import java.util.logging.Logger;

import junit.framework.TestCase;
import alltests.AllTests;
import business.util.StringParse;

public class StringParseTest extends TestCase {
	static String name = "business.StringParse";
	static Logger log = Logger.getLogger(StringParseTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}

	/**
	 * Constructor for StringParseTest.
	 * @param arg0
	 */
	public StringParseTest(String arg0) {
		super(arg0);
	}

	public void testSp() {
		String t1 = "ab";
		String [] t1sp = new String[4];
		for(int i = 0; i<4; ++i) {
			t1sp[i] = t1+ StringParse.sp(i+1)+"c";
		}
		assertTrue(t1sp[0].equals("ab c"));
		assertTrue(t1sp[1].equals("ab  c"));
		assertTrue(t1sp[2].equals("ab   c"));
		assertTrue(t1sp[3].equals("ab    c"));
		log.info("...passed");
		
		
		
		
	}
	public void testStringContains() {
	    
		//first test
		String testString = "abc";
		String [] targetStrings = {"1234abc566",
									"1234ABC566",
									null};
		
		assertTrue( StringParse.stringContains(targetStrings[0],testString,false) );
		assertFalse( StringParse.stringContains(targetStrings[1],testString,false) );
		assertTrue( StringParse.stringContains(targetStrings[0],testString,true) );
		assertTrue( StringParse.stringContains(targetStrings[1],testString,true) );
		assertFalse( StringParse.stringContains(targetStrings[2], testString,false) );
		assertFalse( StringParse.stringContains(targetStrings[2], testString,true) );
		assertTrue( StringParse.stringContains(targetStrings[2], null,false) );
		assertTrue( StringParse.stringContains(targetStrings[2], null,true) );
		assertFalse( StringParse.stringContains(targetStrings[0], null,false) );
		assertFalse( StringParse.stringContains(targetStrings[0], null,true) );		
		
		log.info("...passed");
	}
	//stringArrayContains(String[] arr, String testStr)
	public void testStringArrayContains() {
		String [][] arrs = { {"abc", null, "456"},
							 null,
							 { }
							};
		String [] badTestStrings = {"45", ""};
		for(int i = 0; i < arrs.length; ++i) {
			for(int j = 0; j< badTestStrings.length; ++j) {
				assertFalse( StringParse.stringArrayContains(arrs[i], badTestStrings[j]) ); 
			}
		}
		
		//these should all be found in the 0th array in arrs
		String [] goodTestStrings = {"456", null};		
		for(int i = 0; i < goodTestStrings.length; ++i) {
			assertTrue( StringParse.stringArrayContains(arrs[0], goodTestStrings[i]) );
		}
		log.info("...passed");
		
	
	}
	public void testIsNonnegLong() {
		String [] goodObjects = {"0",
							  "123423412341234"};
		String [] badObjects = { "-100",
							  "123a",
							  "0.23",
							  null};
		for(int i= 0; i < goodObjects.length; ++i) {
			assertTrue( StringParse.isNonnegLong(goodObjects[i]) );
			
		}
		for(int i= 0; i< badObjects.length; ++i) {
			assertFalse( StringParse.isNonnegLong(badObjects[i]) );			
			
		}
		
		log.info("...passed");					  
	}
	/*
	 * Test for String replace(String, String, String)
	 * replace(String newValue, String oldValue, String s)
	 */
	public void testReplaceStringStringString() {
		
		assertEquals(StringParse.replace("XXXX", "abc", "abc123abc56abbc11"),"XXXX123XXXX56abbc11"); 
		assertEquals(StringParse.replace("XXXX", "ab",  "abc123abc56abbc11"),"XXXXc123XXXXc56XXXXbc11");
		assertEquals(StringParse.replace("XXXX", "11",  "abc123abc56abbc11"),"abc123abc56abbcXXXX"); 
		assertEquals(StringParse.replace("XXXX", "",    "abc123abc56abbc11"),"abc123abc56abbc11");  //no replacement
		assertEquals(StringParse.replace("",     "23",  "abc123abc56abbc11"),"abc1abc56abbc11");   //lose "23"
		assertEquals(StringParse.replace(null,   "23",  "abc123abc56abbc11"),"abc123abc56abbc11"); //no replacement
		log.info("...passed");				
	}



	/*
	 * Test for String replace(char, int, String)
	 * String replace(char c, int index, String s)
	 */
	public void testReplaceCIString() {
		assertEquals(StringParse.replace('!',0,"abc0123"), "!bc0123");
		assertEquals(StringParse.replace('&',10,"abc0123"),"abc0123");
		assertEquals(StringParse.replace('&',2 ,null ), null );				
		assertEquals(StringParse.replace( ' ', 3 , "abc0123"), "abc 123");
		assertEquals(StringParse.replace('!',-1,"abc0123"), "abc0123");
		log.info("...passed");						
	}
	/**
	 * String removeCharacter(char c, String s)
	 */
	public void testRemoveCharacter() {
		assertEquals(StringParse.removeCharacter('-',"565-84-1315"),"565841315");
		assertEquals(StringParse.removeCharacter(' ',"Everybody must see this"),"Everybodymustseethis");
		assertEquals(StringParse.removeCharacter('b',null),null);
		assertEquals(StringParse.removeCharacter('b',""),"");
		assertEquals(StringParse.removeCharacter('0',"abc"),"abc"); //no change
		log.info("...passed");
	}
	/**
	 * String removeLeadingTrailingChar(char c, String s) 
	 */
	public void testRemoveLeadingTrailingChar() {
		assertEquals(StringParse.removeLeadingTrailingChar(' ',null), null);
		assertEquals(StringParse.removeLeadingTrailingChar(' ',""), "" );
		assertEquals(StringParse.removeLeadingTrailingChar('%', "%%%%%%%%%abc%%"),"abc");
		assertEquals(StringParse.removeLeadingTrailingChar(' ', "     abc"),"abc" );
		log.info("...passed");
		
	}
	/**
	 * boolean hasLeadingChar(char c,String s)
	 */
	public void testHasLeadingChar() {
		assertTrue(StringParse.hasLeadingChar(' ',"   abc"));
		assertTrue(StringParse.hasLeadingChar('%',"%123"));
		assertFalse(StringParse.hasLeadingChar('a', ""));
		assertFalse(StringParse.hasLeadingChar('a',"ba"));
		assertFalse(StringParse.hasLeadingChar('a',null));
		log.info("...passed");
	}
	public void testHasTrailingChar() {
		assertTrue(StringParse.hasTrailingChar(' ',"abc   "));
		assertTrue(StringParse.hasTrailingChar('%',"123%"));
		assertFalse(StringParse.hasTrailingChar('a', ""));
		assertFalse(StringParse.hasTrailingChar('a',"bac"));
		assertFalse(StringParse.hasTrailingChar('a',null));
		log.info("...passed");
	}
	
	public void testIsEmptyString() {
		//isEmptyString(String)
		assertTrue(StringParse.isEmptyString(""));
		assertTrue(StringParse.isEmptyString(null));
		assertFalse(StringParse.isEmptyString("a"));
		
		//isEmptyString(Object)
		
		class TestNonEmptyClass {
			public String toString() {
				return "not empty";
			}
		}
		TestNonEmptyClass cl = null;
	
		assertFalse(StringParse.isEmptyString(new TestNonEmptyClass()));
		assertFalse(StringParse.isEmptyString(cl));
		
		log.info("...passed");
	}
	public void testIsEmptyStringAfterTrim() {
		assertTrue(StringParse.isEmptyStringAfterTrim("      "));
		assertTrue(StringParse.isEmptyStringAfterTrim(null));
		assertFalse(StringParse.isEmptyStringAfterTrim("   a"));
	}
	/**
	 * String getClassNameNoPackage(Class aClass)
	 */
	public void testGetClassNameNoPackage() {
		String expected = "String";
		String result = StringParse.getClassNameNoPackage(String.class);
		assertEquals(expected, result);
		
		log.info("...passed");
	}

	public void testNumOccurrences() {
		assertEquals(StringParse.numOccurrences("mississippi",'s'),4);
		assertEquals(StringParse.numOccurrences("mississippi",'q'),0);
		assertEquals(StringParse.numOccurrences("",'a'),0);
		assertEquals(StringParse.numOccurrences(null,'a'),0);
		log.info("...passed");
	}
	
	public void testAscii() {
		assertEquals(StringParse.ascii('a'),97);
		assertEquals(StringParse.ascii('7'),55);
		assertEquals(StringParse.ascii('+'),43);
		log.info("...passed");
	}
	
	public void testIndexOf() {
		String [] test1 = {"abc", "", null, "bbc", "bbc"};
		String [] test2 = new String[0];
		String [] test3 = null;
		assertEquals(StringParse.indexOf("bbc",test1),3);
		assertEquals(StringParse.indexOf("qz", test1),-1);
		assertEquals(StringParse.indexOf("abc",test2),-1);
		assertEquals(StringParse.indexOf("abc",test3),-1);
		log.info("...passed");
		
	}
	
	public void testConvertNullToBlank() {
		assertEquals(StringParse.convertNullToBlank(null),"");
		assertEquals(StringParse.convertNullToBlank("xyz"),"xyz");
		log.info("...passed");
	}
	
	public void testReplaceFirst() {
		String test1 = "obcaobcbcobcobc";
		String expected = "aobcbcobcobc";
		String result = StringParse.replaceFirst("", "obc", test1);
		assertEquals(expected, result);
		log.info("...passed");
	}
	
	public void testMultiplyDoubles() {
		String expected = "10.8";
		String result = StringParse.multiplyDoubles("2.3","4.7");
		double resultDbl = Double.parseDouble(result);
		assertTrue(result.indexOf(expected) > -1);
		assertTrue(resultDbl > 10.80 && resultDbl < 10.82);
		log.info("...passed");
	}
	
	public void testAddDoubles() {
		String expected = "7.0";
		String result = StringParse.addDoubles("2.3","4.7");
		double resultDbl = Double.parseDouble(result);
		assertTrue(result.indexOf(expected) > -1);
		assertTrue(resultDbl > 6.9 && resultDbl < 7.1);
		log.info("...passed");
	}
	
	public void testDivideDoubles() {
		String expected = "7.1";
		String result = StringParse.divideDoubles("49.7","7.0");
		double resultDbl = Double.parseDouble(result);
		assertTrue(result.indexOf(expected) > -1);
		assertTrue(resultDbl > 7.0 && resultDbl < 7.2);
		log.info("...passed");
	}
	
		
	
	

}
