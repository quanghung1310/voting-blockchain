CREATE TABLE `vote_content` (
                                `ID` int NOT NULL AUTO_INCREMENT,
                                `START_DATE` timestamp NOT NULL,
                                `CONTENT` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
                                `CREATE_DATE` timestamp NOT NULL,
                                `END_DATE` timestamp NOT NULL,
                                `DESCRIPTION` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
                                `CONTENT_ID` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
                                PRIMARY KEY (`ID`,`CONTENT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
