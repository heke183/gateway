/*
 Navicat Premium Data Transfer

 Source Server         : dev-appgw
 Source Server Type    : MySQL
 Source Server Version : 50625
 Source Host           : mysql.dev.xianglin.com:3306
 Source Schema         : confdb

 Target Server Type    : MySQL
 Target Server Version : 50625
 File Encoding         : 65001

 Date: 09/01/2019 13:46:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dd_field
-- ----------------------------
DROP TABLE IF EXISTS `dd_field`;
CREATE TABLE `dd_field`  (
  `FIELD_CODE` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字段编码',
  `FIELD_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字段名称',
  `TYPES` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字段类型，目前三种情况，一、下拉框（SELECT）二、文本框（INPUT）三、图片上传（IMAGE）',
  `FIELD_DESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述',
  `CREATOR` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `COMMENTS` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`FIELD_CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dd_field_group
-- ----------------------------
DROP TABLE IF EXISTS `dd_field_group`;
CREATE TABLE `dd_field_group`  (
  `GROUP_CODE` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '组编码',
  `GROUP_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '组名称',
  `GROUP_DESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '组描述',
  `ORDER_NO` int(11) NULL DEFAULT NULL COMMENT '组排序号\\n',
  `CREATOR` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `COMMENTS` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`GROUP_CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dd_fieldgroup_field
-- ----------------------------
DROP TABLE IF EXISTS `dd_fieldgroup_field`;
CREATE TABLE `dd_fieldgroup_field`  (
  `GROUP_CODE` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '组编码',
  `FIELD_CODE` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字段编码',
  `ORDER_NO` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `CREATOR` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `COMMENTS` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`GROUP_CODE`, `FIELD_CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dd_items
-- ----------------------------
DROP TABLE IF EXISTS `dd_items`;
CREATE TABLE `dd_items`  (
  `ITEM_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '选项编码',
  `ITEM_VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '选项值',
  `ITEM_DESC` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述',
  `FIELD_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '字段编码',
  `ORDER_NO` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `CREATOR` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '修改人',
  `CREATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `COMMENTS` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ITEM_KEY`, `FIELD_CODE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for gateway_config_items
-- ----------------------------
DROP TABLE IF EXISTS `gateway_config_items`;
CREATE TABLE `gateway_config_items`  (
  `ID` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增，主键',
  `GATEWAY_NAME` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '网关系统名称',
  `ITEM_KEY` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '配置项key',
  `ITEM_VALUE` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '配置项value',
  `STATUS` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态：ENABLE/DISABLE',
  `CREATOR` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后更新人',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime(0) NOT NULL COMMENT '最后修改时间',
  `COMMENTS` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UNI_MUI_IDX_GATEWAY_CONFIG_ITEMS#GATEWAY_NAME#ITEM_KEY`(`GATEWAY_NAME`, `ITEM_KEY`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '网关配置信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for gateway_groovy_script
-- ----------------------------
DROP TABLE IF EXISTS `gateway_groovy_script`;
CREATE TABLE `gateway_groovy_script`  (
  `ID` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `GATEWAY_NAME` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '网关名称',
  `GATEWAY_SERVICE_ALIAS` varchar(192) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '网关服务别名',
  `SCRIPT` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'GROOVY脚本内容',
  `STATUS` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态',
  `CREATOR` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后修改人',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime(0) NOT NULL COMMENT '最后更新时间',
  `COMMENTS` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UNI_MUI_IDX_GATEWAY_SERVICE_CONFIG#GATEWAY_NAME#ALIAS`(`GATEWAY_NAME`, `GATEWAY_SERVICE_ALIAS`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '网关GROOVY脚本表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for gateway_service_config
-- ----------------------------
DROP TABLE IF EXISTS `gateway_service_config`;
CREATE TABLE `gateway_service_config`  (
  `ID` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `GATEWAY_NAME` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '网关系统名称',
  `ALIAS` varchar(192) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '服务别名',
  `SYSTEM_NAME` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '服务提供者系统名称',
  `SERVICE_ID` varchar(192) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '服务唯一标识',
  `RPC_TYPE` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'service' COMMENT 'rpc调用类型',
  `SPI_VERSION` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '网关spi接口版本号',
  `PROTOCOL` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'dubbo' COMMENT 'rpc调用协议：dubbo/hessian',
  `TIMEOUT` int(10) UNSIGNED NOT NULL DEFAULT 3000 COMMENT '调用超时时间',
  `GATEWAY_SERVICE_INTERFACE_NAME` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '服务实现的网关服务接口名称',
  `NEED_LOGIN` int(11) NOT NULL DEFAULT 1 COMMENT '在调用服务前是否需要先登录，默认需要登录',
  `CHANGE_SESSION` int(11) NOT NULL DEFAULT 0 COMMENT '在调用前是否需要更换session，默认不需要',
  `DELETE_SESSION` int(11) NOT NULL DEFAULT 0 COMMENT '在调用后是否需要删除session，默认不需要',
  `USE_ETAG` int(11) NOT NULL DEFAULT 0 COMMENT '是否启用ETag做响应缓存',
  `STATUS` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态：ENABLE/DISABLE',
  `CREATOR` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后修改人',
  `CREATE_TIME` datetime(0) NOT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime(0) NOT NULL COMMENT '最后修改时间',
  `COMMENTS` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '备注',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE INDEX `UNI_MUI_IDX_GATEWAY_SERVICE_CONFIG#GATEWAY_NAME#ALIAS`(`GATEWAY_NAME`, `ALIAS`) USING BTREE,
  UNIQUE INDEX `UNI_MUI_IDX_GATEWAY_SERVICE_CONFIG#GATEWAY_NAME#SERVICE_ID`(`GATEWAY_NAME`, `SERVICE_ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 535 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '网关服务配置信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ggw_config
-- ----------------------------
DROP TABLE IF EXISTS `ggw_config`;
CREATE TABLE `ggw_config`  (
  `ID` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `CONFIG_KEY` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '配置项KEY',
  `CONFIG_VALUE` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '配置项VALUE',
  `STATUS` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '状态',
  `CREATOR` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后修改人',
  `REMARK` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'GGW网关配置信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for ggw_service_config
-- ----------------------------
DROP TABLE IF EXISTS `ggw_service_config`;
CREATE TABLE `ggw_service_config`  (
  `ID` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `SPI_VERSION` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '服务实现的网关SPI中服务接口的版本号',
  `OPERATION_TYPE` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '操作类型',
  `SYSTEM_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '服务提供方系统名称',
  `INTERFACE_NAME` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '服务接口名称',
  `METHOD_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '服务方法名称',
  `TIMEOUT` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '服务调用超时设置，单位：毫秒',
  `PROTOCOL` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'RPC调用协议',
  `IS_LOCAL` char(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否本地服务',
  `NEED_LOGIN` char(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否需要登录',
  `STATUS` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '状态',
  `CREATOR` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `UPDATER` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后修改人',
  `REMARK` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `CREATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 219 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'GGW网关配置信息表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
