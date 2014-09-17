DROP TABLE IF EXISTS `jpaddressbook_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpaddressbook_contacts` (
  `contactkey` varchar(40) NOT NULL,
  `profiletype` varchar(30) NOT NULL,
  `contactxml` longtext NOT NULL,
  `contactowner` varchar(40) NOT NULL,
  `publiccontact` smallint(6) NOT NULL,
  PRIMARY KEY (`contactkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

DROP TABLE IF EXISTS `jpaddressbook_contactsearch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpaddressbook_contactsearch` (
  `contactkey` varchar(40) NOT NULL,
  `attrname` varchar(30) NOT NULL,
  `textvalue` varchar(255) DEFAULT NULL,
  `datevalue` date DEFAULT NULL,
  `numvalue` int(11) DEFAULT NULL,
  `langcode` varchar(2) DEFAULT NULL,
  KEY `jpaddressbook_contactsearch_contactkey_fkey` (`contactkey`),
  CONSTRAINT `jpaddressbook_contactsearch_contactkey_fkey` FOREIGN KEY (`contactkey`) REFERENCES `jpaddressbook_contacts` (`contactkey`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;