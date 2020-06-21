CREATE TABLE `transaction` (
                               `ID` int NOT NULL AUTO_INCREMENT,
                               `SENDER` varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `RECEIPT` varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `VALUE` int NOT NULL,
                               `SIGNATURE` varchar(1024) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `TRANS_ID` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                               `CURRENCY` varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT 'vote',
                               `DESCRIPTION` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                               `CREATE_DATE` timestamp NOT NULL,
                               `LAST_MODIFY` timestamp NOT NULL,
                               `ACTIVE` int NOT NULL DEFAULT '1',
                               `CONTENT_ID` varchar(50) COLLATE utf8_bin NOT NULL,
                               `IS_MINE` int NOT NULL DEFAULT '0',
                               PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
