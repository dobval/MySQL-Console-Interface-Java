-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: arctic_athletes_simple
-- ------------------------------------------------------
-- Server version	8.4.0

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
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_UNIQUE` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Ivan','Petrov','+359899353535','ul. Srebarna 1',NULL,NULL),(2,'Petar','Georgiev','+359878900878','bul. Tsar Boris 2','p.georgiev@protonmail.com','parola89?'),(3,'Georgi','Ivanov','+359878400899','ul. Naroden geroi 3','g.ivanov@yahoo.com','parola!32'),(4,'Denis','Momchilov','+359899090912','ul. 681va 4','d.momchilov@protonmail.com','nopassword1234'),(5,' Ivan',' Ivanov',' +359899090913',' ul. Sharl Shampo 1','',''),(6,' Georgi',' Georgiev',' +359899090914',' ul. Kiril Botev 2','g.georgiev@gmail.com','nopassword4321'),(7,' Dimitar',' Dimitrov',' +359899090915',' ul. Kiril Botev 3','d.dimitrov@gmail.com','nopassword1234'),(8,' Petar',' Petrov',' +359899090916',' ul. Slavovitsa 4','',''),(9,' Nikolay',' Nikolov',' +359899090917',' ul. Roza 5','',''),(10,' Stoyan',' Stoyanov',' +359899090918',' ul. Georgi Izmirliev 6','s.stoyanov@gmail.com','Password9876');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `phone` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Krasimir','Mitkov','+359899787878'),(2,'Darin','Simov','+359878700441'),(3,'Georgi','Teodorov','+359899700855');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id_idx` (`order_id`),
  KEY `product_id_idx` (`product_id`),
  CONSTRAINT `order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,1,2),(2,2,2,3),(3,3,3,1),(4,4,4,2),(5,5,5,1),(6,6,6,3),(7,7,7,2),(8,8,8,1),(9,9,9,3),(10,10,10,2),(11,1,11,1),(12,2,12,2),(13,3,13,3),(14,4,14,1),(15,5,15,2),(16,6,16,1),(17,7,17,3),(18,8,18,2),(19,9,19,1),(20,10,20,3);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `employee_id` int DEFAULT NULL,
  `created_date` date NOT NULL,
  `finished_date` date DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_customer_idx` (`customer_id`),
  KEY `employee_id_idx` (`employee_id`),
  CONSTRAINT `customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `employee_id` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,1,'2024-05-15','2024-05-19',' FINISHED'),(2,2,2,'2024-05-14','2024-05-18',' FINISHED'),(3,3,3,'2024-05-13','2024-05-17',' FINISHED'),(4,4,1,'2024-05-12','2024-05-16',' CANCELLED'),(5,5,2,'2024-05-19',NULL,' DELIVERING'),(6,6,3,'2024-05-10','2024-05-14',' FINISHED'),(7,7,1,'2024-05-09','2024-05-13',' CANCELLED'),(8,8,2,'2024-05-19',NULL,' DELIVERING'),(9,9,3,'2024-05-07','2024-05-11',' FINISHED'),(10,10,1,'2024-05-06','2024-05-10',' CANCELLED');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `category` varchar(45) NOT NULL DEFAULT 'others',
  `price` decimal(10,2) NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Ski Carving Atomic K','Ski',99.99,9),(2,'Snowboard Burton Custom','Snowboard',150.00,5),(3,'Ice Skates Bauer Supreme','Ice Skating',80.00,10),(4,'Ski Helmet POC Obex','Ski',120.00,7),(5,'Snowboard Boots DC Phase','Snowboard',110.00,6),(6,'Ski Poles Black Diamond','Ski',50.00,15),(7,'Snowboard Goggles Oakley Flight Deck','Snowboard',75.00,8),(8,'Ice Hockey Stick CCM Ribcor','Ice Hockey',100.00,12),(9,'Ski Jacket North Face Free Thinker','Ski',200.00,4),(10,'Snowboard Pants Burton AK Swash','Snowboard',180.00,3),(11,'Ice Skating Dress ChloeNoel','Ice Skating',70.00,10),(12,'Ski Gloves Hestra Army Leather','Ski',60.00,14),(13,'Snowboard Bindings Union Force','Snowboard',130.00,5),(14,'Ice Hockey Puck A&R','Ice Hockey',1.00,50),(15,'Ski Socks Smartwool PhD','Ski',20.00,30),(16,'Snowboard Helmet Smith Vantage','Snowboard',120.00,6),(17,'Ice Skating Tights Over the Boot','Ice Skating',15.00,20),(18,'Ski Bag Thule RoundTrip','Ski',90.00,8),(19,'Snowboard Wax Demon Hyper','Snowboard',10.00,25),(20,'Ice Hockey Gloves Bauer Supreme','Ice Hockey',70.00,10);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-23 19:23:29
