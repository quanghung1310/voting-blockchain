CREATE TABLE `wallet` (
                          `ID` int NOT NULL AUTO_INCREMENT,
                          `WALLET_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
                          `EMAIL` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
                          `PASSWORD` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
                          `PRIVATE_KEY` varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
                          `PUBLIC_KEY` varchar(1024) COLLATE utf8_unicode_ci NOT NULL,
                          `ACTIVE` int NOT NULL,
                          `CREATE_DATE` timestamp NOT NULL,
                          `LAST_MODIFY` timestamp NOT NULL,
                          `FIRST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `LAST_NAME` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `TYPE` int NOT NULL,
                          `SEX` int NOT NULL,
                          `MAX_PER_DATE` int NOT NULL DEFAULT '3',
                          PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
