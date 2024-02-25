CREATE DATABASE  IF NOT EXISTS `registertopic` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `registertopic`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: registertopic
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authority` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES ('ROLE_ADMIN'),('ROLE_GUEST'),('ROLE_HEAD'),('ROLE_LECTURER'),('ROLE_STUDENT');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `date_submit` datetime DEFAULT NULL,
  `poster` varchar(255) DEFAULT NULL,
  `task_id` int DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKfc34jy6dfju6h06j14xf3itqx` (`poster`),
  KEY `FKfknte4fhjhet3l1802m1yqa50` (`task_id`),
  CONSTRAINT `FKfc34jy6dfju6h06j14xf3itqx` FOREIGN KEY (`poster`) REFERENCES `person` (`person_id`),
  CONSTRAINT `FKfknte4fhjhet3l1802m1yqa50` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `file_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `comment_id` int DEFAULT NULL,
  `task_id` int DEFAULT NULL,
  PRIMARY KEY (`file_id`),
  KEY `FKfakp2597je9wtgnojlwmywqsb` (`comment_id`),
  KEY `FKkl8espv0gf1wcu82gvj2lccbm` (`task_id`),
  CONSTRAINT `FKfakp2597je9wtgnojlwmywqsb` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`comment_id`),
  CONSTRAINT `FKkl8espv0gf1wcu82gvj2lccbm` FOREIGN KEY (`task_id`) REFERENCES `task` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturer`
--

DROP TABLE IF EXISTS `lecturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturer` (
  `lecturer_id` varchar(255) NOT NULL,
  `major` varchar(50) DEFAULT NULL,
  `authority` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`lecturer_id`),
  KEY `FKhdkhx8tan7mlso7785d0wid2v` (`authority`),
  CONSTRAINT `FKhdkhx8tan7mlso7785d0wid2v` FOREIGN KEY (`authority`) REFERENCES `authority` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturer`
--

LOCK TABLES `lecturer` WRITE;
/*!40000 ALTER TABLE `lecturer` DISABLE KEYS */;
INSERT INTO `lecturer` VALUES ('nna9220','CongNghePhanMem','ROLE_HEAD');
INSERT INTO `lecturer` VALUES ('linhle','CongNghePhanMem','ROLE_LECTURER');
/*!40000 ALTER TABLE `lecturer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `date_submit` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `person_id` varchar(255) NOT NULL,
  `actived` bit(1) DEFAULT NULL,
  `birth_day` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gender` bit(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `provider_id` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `authority_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`person_id`),
  KEY `FKh5l7pviaei0j6351nkb3qn7hc` (`authority_name`),
  CONSTRAINT `FKh5l7pviaei0j6351nkb3qn7hc` FOREIGN KEY (`authority_name`) REFERENCES `authority` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES ('20110678',_binary '','12/12/2002',NULL,'Nguyen Thi',_binary '\0',NULL,'Na',NULL,'0987654321',NULL,_binary '\0','20110678@student.hcmute.edu.vn','ROLE_STUDENT');
INSERT INTO `person` VALUES ('20110753',_binary '','12/12/2002',NULL,'Nguyen Thi',_binary '\0',NULL,'Thuy Trang',NULL,'0987654321',NULL,_binary '\0','20110753@student.hcmute.edu.vn','ROLE_STUDENT');
INSERT INTO `person` VALUES ('nna9220',_binary '','12/12/2002',NULL,'Nguyen Thi',_binary '\0',NULL,'An',NULL,'0987654321',NULL,_binary '\0','nna9220@gmail.com','ROLE_HEAD');
INSERT INTO `person` VALUES ('linhle',_binary '','12/12/2002',NULL,'Nguyen Thi',_binary '\0',NULL,'Linh',NULL,'0987654321',NULL,_binary '\0','Linhle941999@gmail.com','ROLE_LECTURER');
INSERT INTO `person` VALUES ('nguyenthuan',_binary '','12/12/2002',NULL,'Nguyen',_binary '\0',NULL,'Thuan',NULL,'0987654321',NULL,_binary '\0','nguyenthuan2007bl@gmail.com','ROLE_ADMIN');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `register_period`
--

DROP TABLE IF EXISTS `register_period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `register_period` (
  `period_id` int NOT NULL AUTO_INCREMENT,
  `registration_name` varchar(255) DEFAULT NULL,
  `registration_time_end` datetime DEFAULT NULL,
  `registration_time_start` datetime DEFAULT NULL,
  `type_subject_id` int DEFAULT NULL,
  PRIMARY KEY (`period_id`),
  KEY `FKf8orx2htsrpu0bx4tyqp4upql` (`type_subject_id`),
  CONSTRAINT `FKf8orx2htsrpu0bx4tyqp4upql` FOREIGN KEY (`type_subject_id`) REFERENCES `type_subject` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `register_period`
--

LOCK TABLES `register_period` WRITE;
/*!40000 ALTER TABLE `register_period` DISABLE KEYS */;
/*!40000 ALTER TABLE `register_period` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `register_period_lecturer`
--

DROP TABLE IF EXISTS `register_period_lecturer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `register_period_lecturer` (
  `period_id` int NOT NULL AUTO_INCREMENT,
  `registration_name` varchar(255) DEFAULT NULL,
  `registration_time_end` datetime DEFAULT NULL,
  `registration_time_start` datetime DEFAULT NULL,
  PRIMARY KEY (`period_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `register_period_lecturer`
--

LOCK TABLES `register_period_lecturer` WRITE;
/*!40000 ALTER TABLE `register_period_lecturer` DISABLE KEYS */;
/*!40000 ALTER TABLE `register_period_lecturer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `school_year`
--

DROP TABLE IF EXISTS `school_year`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `school_year` (
  `year_id` int NOT NULL AUTO_INCREMENT,
  `year` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`year_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `school_year`
--

LOCK TABLES `school_year` WRITE;
/*!40000 ALTER TABLE `school_year` DISABLE KEYS */;
INSERT INTO `school_year` VALUES (1,'2023-2027');
INSERT INTO `school_year` VALUES (2,'2020-2024');
/*!40000 ALTER TABLE `school_year` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `student_id` varchar(255) NOT NULL,
  `major` varchar(50) DEFAULT NULL,
  `year_id` int DEFAULT NULL,
  `class_id` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  KEY `FK5uymh67120e0pktnyf7plid6w` (`year_id`),
  KEY `FK6jmw3w31l9ojcckkrgv3ga7un` (`class_id`),
  KEY `FKmyd718fb0oebjwr8siyift6gu` (`subject_id`),
  CONSTRAINT `FK5uymh67120e0pktnyf7plid6w` FOREIGN KEY (`year_id`) REFERENCES `school_year` (`year_id`),
  CONSTRAINT `FK6jmw3w31l9ojcckkrgv3ga7un` FOREIGN KEY (`class_id`) REFERENCES `student_class` (`id`),
  CONSTRAINT `FKmyd718fb0oebjwr8siyift6gu` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('20110678','CongNghePhanMem',1,1,NULL);
INSERT INTO `student` VALUES ('20110753','CongNghePhanMem',1,1,NULL);
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_class`
--

DROP TABLE IF EXISTS `student_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_class` (
  `id` int NOT NULL AUTO_INCREMENT,
  `classname` varchar(50) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_class`
--

LOCK TABLES `student_class` WRITE;
/*!40000 ALTER TABLE `student_class` DISABLE KEYS */;
INSERT INTO `student_class` VALUES (1,'20110ST4');
INSERT INTO `student_class` VALUES (2,'20110ST5');
/*!40000 ALTER TABLE `student_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject` (
  `subject_id` int NOT NULL AUTO_INCREMENT,
  `active` tinyint DEFAULT NULL,
  `expected` varchar(255) DEFAULT NULL,
  `major` varchar(50) DEFAULT NULL,
  `requirement` varchar(255) DEFAULT NULL,
  `review` varchar(255) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  `student_1` varchar(255) DEFAULT NULL,
  `student_2` varchar(255) DEFAULT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `ÿear` varchar(255) DEFAULT NULL,
  `instructor_id` varchar(255) DEFAULT NULL,
  `thesis_advisor_id` varchar(255) DEFAULT NULL,
  `type_id_subject` int DEFAULT NULL,
  PRIMARY KEY (`subject_id`),
  KEY `FKeodtsr2fw3wxvb7aob0yg7oy1` (`instructor_id`),
  KEY `FK5ldgqo6j19rlbimbk02lavo2w` (`student_1`),
  KEY `FKit8bvjmlff7fdltao8ijudvj7` (`student_2`),
  KEY `FKm8x3egqit8xp3v27w9k9ovn4r` (`thesis_advisor_id`),
  KEY `FKplu43ukiq1q567m37vno0e02g` (`type_id_subject`),
  CONSTRAINT `FK5ldgqo6j19rlbimbk02lavo2w` FOREIGN KEY (`student_1`) REFERENCES `student` (`student_id`),
  CONSTRAINT `FKeodtsr2fw3wxvb7aob0yg7oy1` FOREIGN KEY (`instructor_id`) REFERENCES `lecturer` (`lecturer_id`),
  CONSTRAINT `FKit8bvjmlff7fdltao8ijudvj7` FOREIGN KEY (`student_2`) REFERENCES `student` (`student_id`),
  CONSTRAINT `FKm8x3egqit8xp3v27w9k9ovn4r` FOREIGN KEY (`thesis_advisor_id`) REFERENCES `lecturer` (`lecturer_id`),
  CONSTRAINT `FKplu43ukiq1q567m37vno0e02g` FOREIGN KEY (`type_id_subject`) REFERENCES `type_subject` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `requirement` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `time_end` datetime DEFAULT NULL,
  `time_start` datetime DEFAULT NULL,
  `assign_to` varchar(255) DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `instructor_id` varchar(255) DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  KEY `FKi52htc463mk791mv4s7rav8m0` (`assign_to`),
  KEY `FK2dg06mnot91yxjuhcbvlwtc4j` (`create_by`),
  KEY `FKa8erqgkdycwwrx5cib1487s19` (`instructor_id`),
  KEY `FK5k22wv8pvap89p7wpo0ghs95g` (`subject_id`),
  CONSTRAINT `FK2dg06mnot91yxjuhcbvlwtc4j` FOREIGN KEY (`create_by`) REFERENCES `person` (`person_id`),
  CONSTRAINT `FK5k22wv8pvap89p7wpo0ghs95g` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`),
  CONSTRAINT `FKa8erqgkdycwwrx5cib1487s19` FOREIGN KEY (`instructor_id`) REFERENCES `lecturer` (`lecturer_id`),
  CONSTRAINT `FKi52htc463mk791mv4s7rav8m0` FOREIGN KEY (`assign_to`) REFERENCES `student` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type_subject`
--

DROP TABLE IF EXISTS `type_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type_subject` (
  `type_id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type_subject`
--

LOCK TABLES `type_subject` WRITE;
/*!40000 ALTER TABLE `type_subject` DISABLE KEYS */;
INSERT INTO `school_year` VALUES (1,'TieuLuanChuyenNganh');
/*!40000 ALTER TABLE `type_subject` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-25 11:56:37
