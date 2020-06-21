CREATE TABLE `elector` (
                           `ID` int NOT NULL AUTO_INCREMENT,
                           `WALLET_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
                           `CONTENT_ID` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
                           PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
