-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.1.63-community - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             10.3.0.5771
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for accountsdb
CREATE DATABASE IF NOT EXISTS `accountsdb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `accountsdb`;

-- Dumping structure for table accountsdb.altshipaddress
CREATE TABLE IF NOT EXISTS `altshipaddress` (
  `addressid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `custid` smallint(5) unsigned NOT NULL,
  `street` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `state` varchar(25) DEFAULT NULL,
  `zip` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`addressid`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table accountsdb.altshipaddress: 3 rows
/*!40000 ALTER TABLE `altshipaddress` DISABLE KEYS */;
INSERT INTO `altshipaddress` (`addressid`, `custid`, `street`, `city`, `state`, `zip`) VALUES
	(1, 1, '101 N COURT', 'FAIRFIELD', 'IA', '52556'),
	(2, 1, '59 BEETLE', 'PALO ALTO', 'CA', '94301'),
	(3, 2, '5 ADAMS', 'FAIRFIELD', 'IA', '52556');
/*!40000 ALTER TABLE `altshipaddress` ENABLE KEYS */;

-- Dumping structure for table accountsdb.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `custid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `fname` varchar(50) DEFAULT NULL,
  `lname` varchar(50) DEFAULT NULL,
  `ssn` varchar(20) DEFAULT NULL,
  `login` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `shipaddress1` varchar(50) DEFAULT NULL,
  `shipaddress2` varchar(50) DEFAULT NULL,
  `shipcity` varchar(25) DEFAULT NULL,
  `shipstate` varchar(20) DEFAULT NULL,
  `shipzipcode` varchar(15) DEFAULT NULL,
  `billaddress1` varchar(50) DEFAULT NULL,
  `billaddress2` varchar(50) DEFAULT NULL,
  `billcity` varchar(25) DEFAULT NULL,
  `billstate` varchar(20) DEFAULT NULL,
  `billzipcode` varchar(15) DEFAULT NULL,
  `nameoncard` varchar(50) DEFAULT NULL,
  `expdate` varchar(20) DEFAULT NULL,
  `cardtype` varchar(20) DEFAULT NULL,
  `cardnum` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`custid`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- Dumping data for table accountsdb.customer: 2 rows
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`custid`, `fname`, `lname`, `ssn`, `login`, `password`, `shipaddress1`, `shipaddress2`, `shipcity`, `shipstate`, `shipzipcode`, `billaddress1`, `billaddress2`, `billcity`, `billstate`, `billzipcode`, `nameoncard`, `expdate`, `cardtype`, `cardnum`) VALUES
	(1, 'John', 'Smith', '555882121', '1', '1', '1000 N. 4th St.', 'PO Box 23', 'Fairfield', 'IA', '52557', '1000 N. 4th St.', 'PO Box 23', 'Fairfield', 'IA', '52557', 'John', '01/31/2020', 'MasterCard', '5555666655556666'),
	(2, 'Jim', 'Jacob', '515282426', '2', '2', '40 N. 4th St.', NULL, 'Fairfield', 'IA', '52556', '40 N. 4th St.', NULL, 'Fairfield', 'IA', '52556', 'James', '04/30/2022', 'Visa', '4444666633336666');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

-- Dumping structure for table accountsdb.ord
CREATE TABLE IF NOT EXISTS `ord` (
  `orderid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `custid` smallint(5) unsigned NOT NULL,
  `shipaddress1` varchar(50) DEFAULT NULL,
  `shipaddress2` varchar(50) DEFAULT NULL,
  `shipcity` varchar(25) DEFAULT NULL,
  `shipstate` varchar(20) DEFAULT NULL,
  `shipzipcode` varchar(15) DEFAULT NULL,
  `billaddress1` varchar(50) DEFAULT NULL,
  `billaddress2` varchar(50) DEFAULT NULL,
  `billcity` varchar(25) DEFAULT NULL,
  `billstate` varchar(20) DEFAULT NULL,
  `billzipcode` varchar(15) DEFAULT NULL,
  `nameoncard` varchar(50) DEFAULT NULL,
  `expdate` varchar(20) DEFAULT NULL,
  `cardtype` varchar(20) DEFAULT NULL,
  `cardnum` varchar(20) DEFAULT NULL,
  `orderdate` varchar(20) DEFAULT NULL,
  `shippeddate` varchar(20) DEFAULT NULL,
  `delivereddate` varchar(20) DEFAULT NULL,
  `shipstatus` varchar(20) DEFAULT NULL,
  `totalpriceamount` double DEFAULT NULL,
  `totalshipmentcost` double DEFAULT NULL,
  `totaltaxamount` double DEFAULT NULL,
  `totalamountcharged` double DEFAULT NULL,
  PRIMARY KEY (`orderid`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dumping data for table accountsdb.ord: 1 rows
/*!40000 ALTER TABLE `ord` DISABLE KEYS */;
INSERT INTO `ord` (`orderid`, `custid`, `shipaddress1`, `shipaddress2`, `shipcity`, `shipstate`, `shipzipcode`, `billaddress1`, `billaddress2`, `billcity`, `billstate`, `billzipcode`, `nameoncard`, `expdate`, `cardtype`, `cardnum`, `orderdate`, `shippeddate`, `delivereddate`, `shipstatus`, `totalpriceamount`, `totalshipmentcost`, `totaltaxamount`, `totalamountcharged`) VALUES
	(1, 1, '1000 N. 4th St.', 'PO Box 23', 'Fairfield', 'IA', '52557', '1000 N. 4th St.', 'PO Box 23', 'Fairfield', 'IA', '52557', 'John', '01/31/2020', 'MasterCard', '5555666655556666', '11/11/2002', '11/15/2002', '11/20/2002', 'delivered', 50, 5, 5, 60);
/*!40000 ALTER TABLE `ord` ENABLE KEYS */;

-- Dumping structure for table accountsdb.orderitem
CREATE TABLE IF NOT EXISTS `orderitem` (
  `orderitemid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `orderid` smallint(5) unsigned NOT NULL,
  `productid` smallint(5) unsigned NOT NULL,
  `quantity` int(10) unsigned DEFAULT NULL,
  `totalprice` double DEFAULT NULL,
  `shipmentcost` double DEFAULT NULL,
  `taxamount` double DEFAULT NULL,
  PRIMARY KEY (`orderitemid`)
) ENGINE=MyISAM AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;

-- Dumping data for table accountsdb.orderitem: 22 rows
/*!40000 ALTER TABLE `orderitem` DISABLE KEYS */;
INSERT INTO `orderitem` (`orderitemid`, `orderid`, `productid`, `quantity`, `totalprice`, `shipmentcost`, `taxamount`) VALUES
	(1, 1, 1, 1, 20, 2, 1.5),
	(2, 1, 2, 2, 30, 3, 3.5),
	(3, 2, 3, 1, 50, 0, 0),
	(4, 2, 6, 1, 30, 0, 0),
	(5, 2, 4, 1, 25, 0, 0),
	(6, 2, 3, 1, 50, 0, 0),
	(7, 2, 2, 3, 45, 0, 0),
	(8, 2, 1, 2, 40, 0, 0),
	(9, 2, 6, 3, 90, 0, 0),
	(10, 2, 5, 1, 12, 0, 0),
	(11, 2, 3, 2, 100, 0, 0),
	(12, 2, 2, 2, 30, 0, 0),
	(13, 2, 1, 1, 20, 0, 0),
	(14, 2, 4, 1, 25, 0, 0),
	(15, 2, 6, 3, 90, 0, 0),
	(16, 2, 5, 1, 12, 0, 0),
	(17, 2, 3, 2, 100, 0, 0),
	(18, 2, 2, 2, 30, 0, 0),
	(19, 2, 1, 1, 20, 0, 0),
	(20, 2, 2, 1, 15, 0, 0),
	(21, 2, 5, 1, 12, 0, 0),
	(22, 2, 2, 1, 15, 0, 0);
/*!40000 ALTER TABLE `orderitem` ENABLE KEYS */;

-- Dumping structure for table accountsdb.shopcartitem
CREATE TABLE IF NOT EXISTS `shopcartitem` (
  `cartitemid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `shopcartid` smallint(5) unsigned NOT NULL,
  `productid` smallint(5) unsigned NOT NULL,
  `quantity` int(10) unsigned DEFAULT NULL,
  `totalprice` double DEFAULT NULL,
  `shipmentcost` double DEFAULT NULL,
  `taxamount` double DEFAULT NULL,
  PRIMARY KEY (`cartitemid`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- Dumping data for table accountsdb.shopcartitem: 0 rows
/*!40000 ALTER TABLE `shopcartitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `shopcartitem` ENABLE KEYS */;

-- Dumping structure for table accountsdb.shopcarttbl
CREATE TABLE IF NOT EXISTS `shopcarttbl` (
  `shopcartid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `custid` smallint(5) unsigned NOT NULL,
  `shipaddress1` varchar(50) DEFAULT NULL,
  `shipaddress2` varchar(50) DEFAULT NULL,
  `shipcity` varchar(25) DEFAULT NULL,
  `shipstate` varchar(20) DEFAULT NULL,
  `shipzipcode` varchar(15) DEFAULT NULL,
  `billaddress1` varchar(50) DEFAULT NULL,
  `billaddress2` varchar(50) DEFAULT NULL,
  `billcity` varchar(25) DEFAULT NULL,
  `billstate` varchar(20) DEFAULT NULL,
  `billzipcode` varchar(15) DEFAULT NULL,
  `nameoncard` varchar(50) DEFAULT NULL,
  `expdate` varchar(20) DEFAULT NULL,
  `cardtype` varchar(20) DEFAULT NULL,
  `cardnum` varchar(20) DEFAULT NULL,
  `totalpriceamount` double DEFAULT NULL,
  `totalshipmentcost` double DEFAULT NULL,
  `totaltaxamount` double DEFAULT NULL,
  `totalamountcharged` double DEFAULT NULL,
  PRIMARY KEY (`shopcartid`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dumping data for table accountsdb.shopcarttbl: 0 rows
/*!40000 ALTER TABLE `shopcarttbl` DISABLE KEYS */;
/*!40000 ALTER TABLE `shopcarttbl` ENABLE KEYS */;


-- Dumping database structure for productsdb
CREATE DATABASE IF NOT EXISTS `productsdb` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `productsdb`;

-- Dumping structure for table productsdb.catalogtype
CREATE TABLE IF NOT EXISTS `catalogtype` (
  `catalogid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `catalogname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`catalogid`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

-- Dumping data for table productsdb.catalogtype: 2 rows
/*!40000 ALTER TABLE `catalogtype` DISABLE KEYS */;
INSERT INTO `catalogtype` (`catalogid`, `catalogname`) VALUES
	(1, 'Books'),
	(2, 'Clothing');
/*!40000 ALTER TABLE `catalogtype` ENABLE KEYS */;

-- Dumping structure for table productsdb.product
CREATE TABLE IF NOT EXISTS `product` (
  `productid` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `catalogid` smallint(5) unsigned NOT NULL,
  `productname` varchar(50) DEFAULT NULL,
  `totalquantity` int(10) unsigned DEFAULT NULL,
  `priceperunit` double DEFAULT NULL,
  `mfgdate` varchar(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`productid`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

-- Dumping data for table productsdb.product: 6 rows
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`productid`, `catalogid`, `productname`, `totalquantity`, `priceperunit`, `mfgdate`, `description`) VALUES
	(1, 1, 'Gone With The Wind', 200, 20, '10/10/2001', 'A moving classic that tells a tale of love and a tale of courage.'),
	(2, 1, 'Messiah of Dune', 30, 15, '05/20/2000', 'You saw how good Dune was. This is Part 2 of this unforgettable trilogy.'),
	(3, 1, 'Garden of Rama', 200, 50, '02/20/1995', 'Highly acclaimed Book of Isaac Asimov. A real nail-biter.'),
	(4, 2, 'Pants', 400, 25, '06/10/2002', 'I have seen the Grand Canyon. I have camped at Yellowstone. But nothing on earth compares to the glory of wearing these pants.'),
	(5, 2, 'T-Shirts', 100, 12, '10/05/2000', 'Can be worn by men or women. Always in style.'),
	(6, 2, 'Skirts', 80, 30, '01/01/2004', 'Once this brand of skirts becomes well-known, watch out!');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
