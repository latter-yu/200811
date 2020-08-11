drop database if exists `musicserver`;
create database if not exists `musicserver` character set utf8;

-- 使用数据库 use `musicserver`;
use `musicserver`;

DROP TABLE IF EXISTS `music`;
CREATE TABLE `music` (  `id` int PRIMARY KEY  AUTO_INCREMENT,`title` varchar(50) BINARY NOT NULL,  `singer` varchar(30) BINARY NOT NULL,  `time` varchar(13) BINARY NOT NULL,  `url` varchar(100) BINARY NOT NULL,  `userid` int(11) NOT NULL );

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (  `id` INT PRIMARY KEY AUTO_INCREMENT,  `username` varchar(20) BINARY NOT NULL,  `password` varchar(32) BINARY NOT NULL );

DROP TABLE IF EXISTS `lovemusic`;
CREATE TABLE `lovemusic` (  `id` int PRIMARY KEY  AUTO_INCREMENT,  `user_id` int(11) NOT NULL,  `music_id` int(11) NOT NULL );

DROP TABLE IF EXISTS `MV`;
CREATE TABLE `MV` (  `id` INT PRIMARY KEY AUTO_INCREMENT,  `title` varchar(50) BINARY NOT NULL,  `singer` varchar(32) BINARY NOT NULL,  `time` varchar(13) BINARY NOT NULL,  `url` varchar(100) BINARY NOT NULL,  `userid` int(11) NOT NULL );

DROP TABLE IF EXISTS `lovemv`;
CREATE TABLE `lovemv` (  `id` int PRIMARY KEY  AUTO_INCREMENT,  `user_id` int(11) NOT NULL,  `mv_id` int(11) NOT NULL );

INSERT INTO user(username, password) VALUES("kxy", "123456");
INSERT INTO music(title, singer, time, url, userid) VALUES("趁你还年轻", "华晨宇", "2020-07-11", "music\\趁你还年轻", 1);
INSERT INTO music(title, singer, time, url, userid) VALUES("夜的钢琴曲", "纯音乐", "2020-07-10", "music\\夜的钢琴曲", 1);
INSERT INTO MV(title, singer, time, url, userid) VALUES("光年之外", "邓紫棋", "2020-07-10", "mv\\光年之外", 1);
INSERT INTO MV(title, singer, time, url, userid) VALUES("年少有为", "李荣浩", "2020-07-10", "mv\\年少有为", 1);



