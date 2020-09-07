-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: localhost    Database: FoodRadar
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Article`
--

DROP TABLE IF EXISTS `Article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Article` (
  `articleId` int NOT NULL AUTO_INCREMENT,
  `articleTitle` varchar(50) NOT NULL,
  `articleTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `articleText` text NOT NULL,
  `modifyTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resId` int NOT NULL,
  `userId` int NOT NULL,
  `ConAmount` int NOT NULL,
  `ConNum` int NOT NULL,
  `articleStatus` tinyint(1) NOT NULL,
  PRIMARY KEY (`articleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Article`
--

LOCK TABLES `Article` WRITE;
/*!40000 ALTER TABLE `Article` DISABLE KEYS */;
/*!40000 ALTER TABLE `Article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ArticleGood`
--

DROP TABLE IF EXISTS `ArticleGood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ArticleGood` (
  `articleGoodId` int NOT NULL AUTO_INCREMENT,
  `articleId` int NOT NULL,
  `userId` int NOT NULL,
  PRIMARY KEY (`articleGoodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ArticleGood`
--

LOCK TABLES `ArticleGood` WRITE;
/*!40000 ALTER TABLE `ArticleGood` DISABLE KEYS */;
/*!40000 ALTER TABLE `ArticleGood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Category`
--

DROP TABLE IF EXISTS `Category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Category` (
  `resCategoryId` int NOT NULL AUTO_INCREMENT,
  `resCategoryInfo` varchar(50) NOT NULL,
  `resCategorySn` int NOT NULL,
  PRIMARY KEY (`resCategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Category`
--

LOCK TABLES `Category` WRITE;
/*!40000 ALTER TABLE `Category` DISABLE KEYS */;
/*!40000 ALTER TABLE `Category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Comment`
--

DROP TABLE IF EXISTS `Comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Comment` (
  `commentId` int NOT NULL AUTO_INCREMENT,
  `commentTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `articleId` int NOT NULL,
  `userId` int NOT NULL,
  `commentModifyTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `commentStatus` tinyint(1) NOT NULL,
  `commentText` text NOT NULL,
  PRIMARY KEY (`commentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Comment`
--

LOCK TABLES `Comment` WRITE;
/*!40000 ALTER TABLE `Comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CommentGood`
--

DROP TABLE IF EXISTS `CommentGood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CommentGood` (
  `commentGoodId` int NOT NULL AUTO_INCREMENT,
  `commentId` int NOT NULL,
  `userId` int NOT NULL,
  PRIMARY KEY (`commentGoodId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CommentGood`
--

LOCK TABLES `CommentGood` WRITE;
/*!40000 ALTER TABLE `CommentGood` DISABLE KEYS */;
/*!40000 ALTER TABLE `CommentGood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CouPon`
--

DROP TABLE IF EXISTS `CouPon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CouPon` (
  `couPonId` int NOT NULL AUTO_INCREMENT,
  `resId` int NOT NULL,
  `couPonStartDate` date DEFAULT NULL,
  `couPonEndDate` date DEFAULT NULL,
  `couPonType` tinyint(1) NOT NULL,
  `couPonInfo` varchar(200) DEFAULT NULL,
  `couPonPhoto` longblob,
  `couPonEnable` tinyint(1) NOT NULL,
  PRIMARY KEY (`couPonId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CouPon`
--

LOCK TABLES `CouPon` WRITE;
/*!40000 ALTER TABLE `CouPon` DISABLE KEYS */;
/*!40000 ALTER TABLE `CouPon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Img`
--

DROP TABLE IF EXISTS `Img`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Img` (
  `imgId` int NOT NULL AUTO_INCREMENT,
  `articleId` int NOT NULL,
  `img` longblob NOT NULL,
  PRIMARY KEY (`imgId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Img`
--

LOCK TABLES `Img` WRITE;
/*!40000 ALTER TABLE `Img` DISABLE KEYS */;
/*!40000 ALTER TABLE `Img` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MyArticle`
--

DROP TABLE IF EXISTS `MyArticle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MyArticle` (
  `myArticleId` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `articleId` int NOT NULL,
  `modifyDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`myArticleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MyArticle`
--

LOCK TABLES `MyArticle` WRITE;
/*!40000 ALTER TABLE `MyArticle` DISABLE KEYS */;
/*!40000 ALTER TABLE `MyArticle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MyCouPon`
--

DROP TABLE IF EXISTS `MyCouPon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MyCouPon` (
  `myCouPonId` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `couPonId` int NOT NULL,
  `couPonIsUsed` tinyint(1) NOT NULL,
  `modifyDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`myCouPonId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MyCouPon`
--

LOCK TABLES `MyCouPon` WRITE;
/*!40000 ALTER TABLE `MyCouPon` DISABLE KEYS */;
/*!40000 ALTER TABLE `MyCouPon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MyRes`
--

DROP TABLE IF EXISTS `MyRes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MyRes` (
  `myResId` int NOT NULL AUTO_INCREMENT,
  `userId` int NOT NULL,
  `resId` int NOT NULL,
  `modifyDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`myResId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MyRes`
--

LOCK TABLES `MyRes` WRITE;
/*!40000 ALTER TABLE `MyRes` DISABLE KEYS */;
/*!40000 ALTER TABLE `MyRes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Res`
--

DROP TABLE IF EXISTS `Res`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Res` (
  `resId` int NOT NULL AUTO_INCREMENT,
  `resName` varchar(50) NOT NULL,
  `resAddress` varchar(200) NOT NULL,
  `resLat` decimal(10,7) NOT NULL,
  `resLon` decimal(10,7) NOT NULL,
  `resTel` varchar(20) NOT NULL,
  `resHours` varchar(100) DEFAULT NULL,
  `resCategoryId` int NOT NULL,
  `resEnable` tinyint(1) NOT NULL,
  `userId` int NOT NULL COMMENT '最後修改的管理者ID',
  `modifyDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `resImg` longblob NOT NULL,
  PRIMARY KEY (`resId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Res`
--

LOCK TABLES `Res` WRITE;
/*!40000 ALTER TABLE `Res` DISABLE KEYS */;
/*!40000 ALTER TABLE `Res` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ResRating`
--

DROP TABLE IF EXISTS `ResRating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ResRating` (
  `resRatingId` int NOT NULL AUTO_INCREMENT,
  `resId` int NOT NULL,
  `userId` int NOT NULL,
  `rating` decimal(2,1) NOT NULL,
  PRIMARY KEY (`resRatingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ResRating`
--

LOCK TABLES `ResRating` WRITE;
/*!40000 ALTER TABLE `ResRating` DISABLE KEYS */;
/*!40000 ALTER TABLE `ResRating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserAccount`
--

DROP TABLE IF EXISTS `UserAccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UserAccount` (
  `userId` int NOT NULL AUTO_INCREMENT,
  `userPhone` varchar(20) NOT NULL,
  `userPwd` varchar(100) NOT NULL,
  `userBirth` datetime DEFAULT NULL,
  `userName` varchar(100) DEFAULT NULL,
  `allowNotifi` tinyint(1) NOT NULL,
  `isEnable` tinyint(1) NOT NULL,
  `isAdmin` tinyint(1) NOT NULL,
  `userAvatar` longblob,
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifyDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserAccount`
--

LOCK TABLES `UserAccount` WRITE;
/*!40000 ALTER TABLE `UserAccount` DISABLE KEYS */;
/*!40000 ALTER TABLE `UserAccount` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-07 10:20:01
