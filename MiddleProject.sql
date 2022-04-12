-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- 主機： localhost
-- 產生時間： 2022 年 04 月 12 日 15:51
-- 伺服器版本： 10.4.21-MariaDB
-- PHP 版本： 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫: `MiddleProject`
--

-- --------------------------------------------------------

--
-- 資料表結構 `Account`
--

CREATE TABLE `Account` (
  `UserName` varchar(100) NOT NULL,
  `UserIdNumber` varchar(50) NOT NULL,
  `Account` varchar(100) NOT NULL,
  `Passwd` varchar(100) NOT NULL,
  `CreateTime` datetime DEFAULT sysdate(),
  `ReviseTime` datetime DEFAULT NULL,
  `ReviseName` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `Content`
--

CREATE TABLE `Content` (
  `ContentID` int(11) NOT NULL,
  `Account` varchar(100) NOT NULL,
  `TabsName` varchar(100) NOT NULL,
  `TabsContentObj` longblob NOT NULL,
  `CreateTime` datetime DEFAULT sysdate(),
  `ReviseTime` datetime DEFAULT NULL,
  `ReviseName` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `ContentBackup`
--

CREATE TABLE `ContentBackup` (
  `BackupID` int(11) NOT NULL,
  `Account` varchar(100) NOT NULL,
  `TabsName` varchar(100) NOT NULL,
  `TabsContentObj` longblob NOT NULL,
  `CreateTime` datetime DEFAULT sysdate(),
  `CreateName` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- 資料表結構 `Userinfo`
--

CREATE TABLE `Userinfo` (
  `UserName` varchar(100) NOT NULL,
  `UserIdNumber` varchar(50) NOT NULL,
  `UserGender` enum('男','女') NOT NULL,
  `UserAge` int(11) DEFAULT timestampdiff(YEAR,`UserBirth`,sysdate()),
  `UserBirth` date NOT NULL,
  `UserEmail` varchar(200) NOT NULL,
  `UserTel` varchar(100) NOT NULL,
  `CreateTime` datetime DEFAULT sysdate(),
  `ReviseTime` datetime DEFAULT NULL,
  `ReviseName` varchar(100) DEFAULT NULL
) ;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `Account`
--
ALTER TABLE `Account`
  ADD PRIMARY KEY (`UserIdNumber`),
  ADD UNIQUE KEY `UserName` (`UserName`),
  ADD UNIQUE KEY `Account` (`Account`);

--
-- 資料表索引 `Content`
--
ALTER TABLE `Content`
  ADD PRIMARY KEY (`ContentID`),
  ADD KEY `Account` (`Account`);

--
-- 資料表索引 `ContentBackup`
--
ALTER TABLE `ContentBackup`
  ADD PRIMARY KEY (`BackupID`),
  ADD KEY `Account` (`Account`);

--
-- 資料表索引 `Userinfo`
--
ALTER TABLE `Userinfo`
  ADD PRIMARY KEY (`UserIdNumber`),
  ADD UNIQUE KEY `UserName` (`UserName`),
  ADD UNIQUE KEY `UserEmail` (`UserEmail`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `Content`
--
ALTER TABLE `Content`
  MODIFY `ContentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `ContentBackup`
--
ALTER TABLE `ContentBackup`
  MODIFY `BackupID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `Account`
--
ALTER TABLE `Account`
  ADD CONSTRAINT `account_ibfk_1` FOREIGN KEY (`UserIdNumber`) REFERENCES `userinfo` (`UserIdNumber`),
  ADD CONSTRAINT `account_ibfk_2` FOREIGN KEY (`UserName`) REFERENCES `userinfo` (`UserName`);

--
-- 資料表的限制式 `Content`
--
ALTER TABLE `Content`
  ADD CONSTRAINT `content_ibfk_1` FOREIGN KEY (`Account`) REFERENCES `account` (`Account`);

--
-- 資料表的限制式 `ContentBackup`
--
ALTER TABLE `ContentBackup`
  ADD CONSTRAINT `contentbackup_ibfk_1` FOREIGN KEY (`Account`) REFERENCES `account` (`Account`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
