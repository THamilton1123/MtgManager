-- MySQL dump 10.13  Distrib 8.0.35, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: mtg_manager_test
-- ------------------------------------------------------
-- Server version	8.0.35-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cards`
--

DROP TABLE IF EXISTS `cards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cards` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cardName` varchar(255) NOT NULL,
  `cardCmc` int DEFAULT NULL,
  `cardType` varchar(100) NOT NULL,
  `cardQuantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cards`
--

LOCK TABLES `cards` WRITE;
/*!40000 ALTER TABLE `cards` DISABLE KEYS */;
INSERT INTO `cards` VALUES (1,'Counterspell',2,'Instant',20),(2,'Sol Ring',1,'Artifact',20),(3,'Urza, Lord High Artificer',4,'Creature',1),(4,'Island',NULL,'Land',100),(5,'Tinker',3,'Sorcery',10),(6,'Omniscience',10,'Enchantment',2),(7,'Jace, Wielder of Mysteries',4,'Planeswalker',1),(8,'Welding Jar',0,'Artifact',8);
/*!40000 ALTER TABLE `cards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rulings`
--

DROP TABLE IF EXISTS `rulings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rulings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ruling_date` date DEFAULT NULL,
  `ruling_text` varchar(1000) DEFAULT NULL,
  `card_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rulings_cards_id_fk` (`card_id`),
  CONSTRAINT `rulings_cards_id_fk` FOREIGN KEY (`card_id`) REFERENCES `cards` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rulings`
--

LOCK TABLES `rulings` WRITE;
/*!40000 ALTER TABLE `rulings` DISABLE KEYS */;
INSERT INTO `rulings` VALUES (1,'2022-12-08','If a spell has X in its mana cost, you must choose 0 as the value of X when casting it without paying its mana cost.',3),(2,'2004-10-04','Because the \"search\" requires you to find a card with certain characteristics, you don\'t have to find the card if you don\'t want to.',5),(3,'2018-07-13','If a spell has X in its mana cost, you must choose 0 as the value of X when casting it without paying its mana cost.',6),(4,'2019-05-03','If for some reason you can\'t win the game (because your opponent controls Platinum Angel, for example), you won\'t lose for having tried to draw a card from a library with no cards in it.  The draw was still replaced.',7),(5,'2022-12-08','You can tap any untapped artifact you control to pay the cost of the mana ability, including an artifact creature you haven\'t controlled continuously since the beginning of your most recent turn.  Tapping an Equipment this way won\'t affect its abilities or the equipped creature.',3),(6,'2018-07-13','Once you cast Omniscience, if it\'s your turn, you\'ll have priority immediately after it resolves.  You can cast another spell before any player can attempt to remove Omniscience with spells or abilities.',6);
/*!40000 ALTER TABLE `rulings` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-07 18:05:00
