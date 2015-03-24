/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : sns

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2014-07-24 08:52:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for weibo_lstnum
-- ----------------------------
DROP TABLE IF EXISTS `weibo_lstnum`;
CREATE TABLE `weibo_lstnum` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `province` varchar(32) DEFAULT NULL,
  `city` varchar(32) DEFAULT NULL,
  `dtStart` datetime DEFAULT NULL,
  `dtEnd` datetime DEFAULT NULL,
  `num` bigint(20) DEFAULT NULL,
  `topic` varchar(256) CHARACTER SET utf8 DEFAULT NULL,
  `savetime` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=839 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for weibo_tweet
-- ----------------------------
DROP TABLE IF EXISTS `weibo_tweet`;
CREATE TABLE `weibo_tweet` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `mid` bigint(20) NOT NULL,
  `title` varchar(64) DEFAULT NULL,
  `userid` varchar(32) DEFAULT NULL,
  `posttime` datetime DEFAULT NULL,
  `url` varchar(128) DEFAULT NULL,
  `content` varchar(4096) DEFAULT NULL,
  `SaveTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=789 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for weibo_user
-- ----------------------------
DROP TABLE IF EXISTS `weibo_user`;
CREATE TABLE `weibo_user` (
  `Uid` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(32) DEFAULT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `followednum` int(11) DEFAULT NULL,
  `fansnum` int(11) DEFAULT NULL,
  `blognum` int(11) DEFAULT NULL,
  `province` varchar(16) DEFAULT NULL,
  `city` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`Uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
