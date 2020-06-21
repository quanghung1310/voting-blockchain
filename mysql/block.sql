CREATE TABLE `block` (
                         `ID` int NOT NULL AUTO_INCREMENT,
                         `BLOCK_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
                         `PREVIOUS_HASH` varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
                         `HASH` varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
                         `NONCE` mediumint NOT NULL,
                         `TRANS_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
                         `MINER_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
                         `PARENT_ID` int DEFAULT '0',
                         `IS_ACTIVE` int DEFAULT '1',
                         `STATUS_BLOCK` int DEFAULT '0',
                         `CREATE_DATE` timestamp NOT NULL,
                         `LAST_MODIFY` timestamp NOT NULL,
                         `TIME_HASH` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
                         `TOTAL` int NOT NULL,
                         `DIFFICULTY` int NOT NULL,
                         PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
