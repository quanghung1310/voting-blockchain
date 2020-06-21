CREATE TABLE `block_chain` (
                               `ID` int NOT NULL AUTO_INCREMENT,
                               `BLOCK_ID` int NOT NULL,
                               `CREATE_DATE` timestamp NOT NULL,
                               `IS_ACTIVE` int NOT NULL DEFAULT '1',
                               `PARENT_ID` int DEFAULT '0',
                               `WALLET_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
                               PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
