SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for edge_data_sync_log
-- ----------------------------
DROP TABLE IF EXISTS `edge_data_sync_log`;
CREATE TABLE `edge_data_sync_log`  (
  `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`table_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for feature_table
-- ----------------------------
DROP TABLE IF EXISTS `feature_table`;
CREATE TABLE `feature_table`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feature_table_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `feature_table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'HTTP',
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `columns` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datasource_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'http-data-source',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_feature_table_id`(`feature_table_id`) USING BTREE,
  UNIQUE INDEX `upk_datasource_feature_table_id`(`feature_table_id`, `node_id`, `datasource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for feature_table_backup
-- ----------------------------
DROP TABLE IF EXISTS `feature_table_backup`;
CREATE TABLE `feature_table_backup`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feature_table_id` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `feature_table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'HTTP',
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `columns` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datasource_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history`  (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `checksum` int(11) NULL DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  INDEX `flyway_schema_history_s_idx`(`success`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for inst
-- ----------------------------
DROP TABLE IF EXISTS `inst`;
CREATE TABLE `inst`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inst_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_inst_id`(`inst_id`) USING BTREE,
  INDEX `key_inst_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for node
-- ----------------------------
DROP TABLE IF EXISTS `node`;
CREATE TABLE `node`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `auth` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `control_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `net_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'normal',
  `mode` int(11) NOT NULL DEFAULT 0,
  `master_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'master',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `inst_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `inst_token` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `protocol` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  `inst_token_state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'UNUSED',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_node_id`(`node_id`) USING BTREE,
  UNIQUE INDEX `upk_inst_node_id`(`inst_id`, `node_id`) USING BTREE,
  INDEX `key_node_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for node_route
-- ----------------------------
DROP TABLE IF EXISTS `node_route`;
CREATE TABLE `node_route`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `route_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `src_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `dst_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `src_net_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `dst_net_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_route_src_dst`(`src_node_id`, `dst_node_id`) USING BTREE,
  INDEX `key_router_src`(`src_node_id`) USING BTREE,
  INDEX `key_router_dst`(`dst_node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for node_route_approval_config
-- ----------------------------
DROP TABLE IF EXISTS `node_route_approval_config`;
CREATE TABLE `node_route_approval_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vote_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_single` tinyint(1) NOT NULL,
  `src_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `src_node_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `des_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `des_node_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `all_participants` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_node_route_approval_config_vote_id`(`vote_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `compute_mode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'mpc',
  `compute_func` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ALL',
  `project_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `owner_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `status` tinyint(1) NOT NULL DEFAULT 0,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_id`(`project_id`) USING BTREE,
  INDEX `key_project_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_approval_config
-- ----------------------------
DROP TABLE IF EXISTS `project_approval_config`;
CREATE TABLE `project_approval_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vote_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `initiator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parties` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `invite_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `participant_node_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_approval_config_vote_id`(`vote_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_datatable
-- ----------------------------
DROP TABLE IF EXISTS `project_datatable`;
CREATE TABLE `project_datatable`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `datatable_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_configs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_datatable_id`(`project_id`, `node_id`, `datatable_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_feature_table
-- ----------------------------
DROP TABLE IF EXISTS `project_feature_table`;
CREATE TABLE `project_feature_table`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `feature_table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_configs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datasource_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'http-data-source',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_feature_table_id`(`project_id`, `node_id`, `feature_table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_feature_table_backup
-- ----------------------------
DROP TABLE IF EXISTS `project_feature_table_backup`;
CREATE TABLE `project_feature_table_backup`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `feature_table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_configs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `datasource_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_fed_table
-- ----------------------------
DROP TABLE IF EXISTS `project_fed_table`;
CREATE TABLE `project_fed_table`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `fed_table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `joins` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_fed_table_id`(`project_id`, `fed_table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_graph
-- ----------------------------
DROP TABLE IF EXISTS `project_graph`;
CREATE TABLE `project_graph`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `edges` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `owner_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `node_max_index` int(11) NOT NULL,
  `max_parallelism` int(11) NULL DEFAULT 1,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_graph`(`project_id`, `graph_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_graph_domain_datasource
-- ----------------------------
DROP TABLE IF EXISTS `project_graph_domain_datasource`;
CREATE TABLE `project_graph_domain_datasource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `domain_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_source_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'default-data-source',
  `data_source_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'default-data-source',
  `edit_enable` tinyint(1) NOT NULL DEFAULT 1,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_graph_domain`(`project_id`, `graph_id`, `domain_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_graph_node
-- ----------------------------
DROP TABLE IF EXISTS `project_graph_node`;
CREATE TABLE `project_graph_node`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `label` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `x` int(11) NULL DEFAULT NULL,
  `y` int(11) NULL DEFAULT NULL,
  `inputs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `outputs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `node_def` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_graph_node`(`project_id`, `graph_id`, `graph_node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_graph_node_kuscia_params
-- ----------------------------
DROP TABLE IF EXISTS `project_graph_node_kuscia_params`;
CREATE TABLE `project_graph_node_kuscia_params`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `graph_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `inputs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `outputs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `node_eval_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_graph_node_kuscia_params_id`(`project_id`, `graph_id`, `graph_node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_inst
-- ----------------------------
DROP TABLE IF EXISTS `project_inst`;
CREATE TABLE `project_inst`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `inst_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_inst_id`(`project_id`, `inst_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_job
-- ----------------------------
DROP TABLE IF EXISTS `project_job`;
CREATE TABLE `project_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `err_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `edges` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `finished_time` datetime NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_job_id`(`project_id`, `job_id`) USING BTREE,
  UNIQUE INDEX `upk_job_id`(`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_job_task
-- ----------------------------
DROP TABLE IF EXISTS `project_job_task`;
CREATE TABLE `project_job_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parties` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `err_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `graph_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `graph_node` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `extra_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_job_task_id`(`project_id`, `job_id`, `task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_job_task_log
-- ----------------------------
DROP TABLE IF EXISTS `project_job_task_log`;
CREATE TABLE `project_job_task_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `log` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_project_job_task_log`(`project_id`, `job_id`, `task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_model
-- ----------------------------
DROP TABLE IF EXISTS `project_model`;
CREATE TABLE `project_model`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_model_id`(`project_id`, `model_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_model_pack
-- ----------------------------
DROP TABLE IF EXISTS `project_model_pack`;
CREATE TABLE `project_model_pack`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `initiator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `model_stats` tinyint(1) NOT NULL DEFAULT 0,
  `serving_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `sample_tables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `train_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `model_report_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `model_datasource` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_model_id`(`model_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_model_serving
-- ----------------------------
DROP TABLE IF EXISTS `project_model_serving`;
CREATE TABLE `project_model_serving`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `serving_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `initiator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `serving_input_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parties` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `party_endpoints` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `serving_stats` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_serving_id`(`serving_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_node
-- ----------------------------
DROP TABLE IF EXISTS `project_node`;
CREATE TABLE `project_node`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_node_id`(`project_id`, `node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_read_data
-- ----------------------------
DROP TABLE IF EXISTS `project_read_data`;
CREATE TABLE `project_read_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `output_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `report_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `hash` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `task` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `grap_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `content` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `raw` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_read_data_id`(`project_id`, `report_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_report
-- ----------------------------
DROP TABLE IF EXISTS `project_report`;
CREATE TABLE `project_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `report_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_report_id`(`project_id`, `report_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_result
-- ----------------------------
DROP TABLE IF EXISTS `project_result`;
CREATE TABLE `project_result`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `kind` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_result_kind_node_ref_id`(`project_id`, `kind`, `node_id`, `ref_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_rule
-- ----------------------------
DROP TABLE IF EXISTS `project_rule`;
CREATE TABLE `project_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rule_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_rule_id`(`project_id`, `rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_schedule
-- ----------------------------
DROP TABLE IF EXISTS `project_schedule`;
CREATE TABLE `project_schedule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `cron` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `request` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `project_schedule_job`;
CREATE TABLE `project_schedule_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `err_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `edges` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `finished_time` datetime NULL DEFAULT NULL,
  `owner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_project_schedule_job_id`(`project_id`, `job_id`) USING BTREE,
  UNIQUE INDEX `upk_schedule_job_id`(`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_schedule_task
-- ----------------------------
DROP TABLE IF EXISTS `project_schedule_task`;
CREATE TABLE `project_schedule_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `schedule_task_expect_start_time` datetime NOT NULL,
  `schedule_task_start_time` datetime NULL DEFAULT NULL,
  `schedule_task_end_time` datetime NULL DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_request` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `all_re_run` tinyint(1) NOT NULL DEFAULT 0,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for strategy_usage
-- ----------------------------
DROP TABLE IF EXISTS `strategy_usage`;
CREATE TABLE `strategy_usage`  (
  `strategy_id` int(11) NOT NULL AUTO_INCREMENT,
  `table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据表id',
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '授权的项目id',
  `strategy_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `is_active` tinyint(1) NULL DEFAULT NULL,
  `usage_count_limit` int(11) NULL DEFAULT NULL COMMENT '时间---限定使用总次数',
  `valid_start_time` datetime NULL DEFAULT NULL COMMENT '起始时间',
  `valid_end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `time_window_days` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '可使用的星期几',
  `daily_start_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '每天开始时间',
  `daily_end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '每天结束时间',
  `freq_limit_minute` int(11) NULL DEFAULT NULL COMMENT '每分钟最大使用次数',
  `freq_limit_hour` int(11) NULL DEFAULT NULL,
  `freq_limit_day` int(11) NULL DEFAULT NULL,
  `trigger_events` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定使用的触发事件',
  `region_limit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地点---限定使用地域（如行政区划码）',
  `position_limit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定使用位置标识（平台或连接器）',
  `env_requirements` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定运行环境（如沙箱、TEE）',
  `allowed_users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主体---限定允许使用的用户或组织',
  `delivery_connectors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定交付连接器',
  `usage_connectors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定使用连接器',
  `role_limit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定角色（如数据服务方）',
  `required_data_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '客体---限定数据状态（如加密）',
  `is_auto_destroy` tinyint(1) NULL DEFAULT NULL COMMENT '使用完成后立即销毁',
  `max_access_volume` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最大访问数据量（如 10G）',
  `max_dist_vol_single` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单次最大分发量限制',
  `max_dist_vol_total` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '总量最大分发量限制',
  `is_vpn_required` tinyint(1) NULL DEFAULT NULL COMMENT '通信---是否强制使用 VPN',
  `allowed_protocols` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '允许的传输协议',
  `encryption_channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '加密信道要求',
  `is_persistence_allowed` tinyint(1) NULL DEFAULT NULL COMMENT '存储---是否允许持久化存储',
  `is_storage_encrypted` tinyint(1) NULL DEFAULT NULL COMMENT '存储是否加密',
  `storage_enc_algo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '存储加密算法',
  `storage_locations` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定存储位置',
  `retention_days` int(11) NULL DEFAULT NULL COMMENT '存储最长保留时间（单位：天）',
  `basic_permissions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '基础权限：查看 复制 下载 数据处理 联合开发',
  `application_controls` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用管控',
  `flow_control` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流转控制',
  `compliance_obligations` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '履行义务',
  `data_preparation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据准备',
  `data_filtering` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据过滤',
  `feature_processing` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '特征处理',
  `statistics` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '统计',
  `model_training` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模型训练',
  `model_prediction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模型预测',
  `model_evaluation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模型评估',
  PRIMARY KEY (`strategy_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for strategy_usage_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `strategy_usage_dynamic`;
CREATE TABLE `strategy_usage_dynamic`  (
  `usage_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '使用记录',
  `strategy_id` int(11) NOT NULL,
  `table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据表id',
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '授权的项目id',
  `strategy_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `usage_count` int(11) NULL DEFAULT NULL COMMENT '!!时间---当前使用总次数',
  `current_operation_time` datetime NULL DEFAULT NULL COMMENT '!!记录使用的时间',
  `trigger_events` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定使用的触发事件',
  `region` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地点---限定使用地域（如行政区划码）',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定使用位置标识（平台或连接器）',
  `env` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定运行环境（如沙箱、TEE）',
  `users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主体---限定允许使用的用户或组织',
  `delivery_connectors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定交付连接器',
  `usage_connectors` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定使用连接器',
  `role_` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定角色（如数据服务方）',
  `required_data_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '客体---限定数据状态（如加密）',
  `access_volume` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '访问数据量（如 10G）',
  `dist_vol_single` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单次分发量限制',
  `dist_vol_total` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '总量分发量限制',
  `protocols` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '允许的传输协议',
  `encryption_channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '加密信道要求',
  `is_storage_encrypted` tinyint(1) NULL DEFAULT NULL COMMENT '存储是否加密',
  `storage_enc_algo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '存储加密算法',
  `storage_locations` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定存储位置',
  `retention_days` int(11) NULL DEFAULT NULL COMMENT '已经保留时间（单位：天）',
  `allowed_purposes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '使用行为控制---限定用途（查看、处理等）',
  `allowed_apps` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定调用应用程序',
  `allowed_algorithms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '限定允许的算法类型',
  `term_condition_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '谁可以终止',
  `term_condition_when` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '在什么条件下终止',
  PRIMARY KEY (`usage_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resource_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `resource_code`(`resource_code`) USING BTREE,
  INDEX `key_resource_type`(`resource_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_resource_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource_rel`;
CREATE TABLE `sys_role_resource_rel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_role_code_resource_code`(`role_code`, `resource_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_node_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_node_rel`;
CREATE TABLE `sys_user_node_rel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_user_id_node_id`(`user_id`, `node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_permission_rel
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_permission_rel`;
CREATE TABLE `sys_user_permission_rel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ROLE',
  `target_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_user_key_target_code`(`user_key`, `target_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tee_download_approval_config
-- ----------------------------
DROP TABLE IF EXISTS `tee_download_approval_config`;
CREATE TABLE `tee_download_approval_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vote_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `project_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `graph_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `all_participants` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_tee_download_approval_config_vote_id`(`vote_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tee_node_datatable_management
-- ----------------------------
DROP TABLE IF EXISTS `tee_node_datatable_management`;
CREATE TABLE `tee_node_datatable_management`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tee_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `datatable_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `datasource_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `kind` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `err_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `operate_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_node_datatable_management`(`node_id`, `tee_node_id`, `datatable_id`, `job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_accounts
-- ----------------------------
DROP TABLE IF EXISTS `user_accounts`;
CREATE TABLE `user_accounts`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `owner_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CENTER',
  `owner_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'kuscia-system',
  `passwd_reset_failed_attempts` int(11) NULL DEFAULT NULL,
  `gmt_passwd_reset_release` datetime NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `failed_attempts` int(11) NULL DEFAULT NULL,
  `locked_invalid_time` datetime NULL DEFAULT NULL,
  `inst_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_tokens
-- ----------------------------
DROP TABLE IF EXISTS `user_tokens`;
CREATE TABLE `user_tokens`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gmt_token` datetime NULL DEFAULT NULL,
  `session_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_name`(`name`) USING BTREE,
  CONSTRAINT `fk_name` FOREIGN KEY (`name`) REFERENCES `user_accounts` (`name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for vote_invite
-- ----------------------------
DROP TABLE IF EXISTS `vote_invite`;
CREATE TABLE `vote_invite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vote_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `initiator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `vote_participant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `vote_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `action` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'REVIEWING',
  `reason` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_vote_invite_participant_id`(`vote_id`, `vote_participant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for vote_request
-- ----------------------------
DROP TABLE IF EXISTS `vote_request`;
CREATE TABLE `vote_request`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `vote_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `initiator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `voters` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `vote_counter` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `executors` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `approved_threshold` int(11) NOT NULL,
  `request_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `status` tinyint(1) NOT NULL,
  `execute_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'COMMITTED',
  `msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `party_vote_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upk_vote_id`(`vote_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-------- add mock data --------------

-- resource
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'ALL_INTERFACE_RESOURCE', 'ALL_INTERFACE_RESOURCE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_UPDATE', 'NODE_UPDATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_CREATE', 'NODE_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_PAGE', 'NODE_PAGE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_GET', 'NODE_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_DELETE', 'NODE_DELETE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_TOKEN', 'NODE_TOKEN');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_NEW_TOKEN', 'NODE_NEW_TOKEN');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_REFRESH', 'NODE_REFRESH');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_LIST', 'NODE_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_RESULT_LIST', 'NODE_RESULT_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_RESULT_DETAIL', 'NODE_RESULT_DETAIL');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATA_CREATE', 'DATA_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATA_CREATE_DATA', 'DATA_CREATE_DATA');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATA_UPLOAD', 'DATA_UPLOAD');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATA_DOWNLOAD', 'DATA_DOWNLOAD');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATA_LIST_DATASOURCE', 'DATA_LIST_DATASOURCE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATATABLE_LIST', 'DATATABLE_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATATABLE_GET', 'DATATABLE_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATATABLE_DELETE', 'DATATABLE_DELETE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_COMM_I18N', 'GRAPH_COMM_I18N');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_COMM_LIST', 'GRAPH_COMM_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_COMM_GET', 'GRAPH_COMM_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_COMM_BATH', 'GRAPH_COMM_BATH');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_CREATE', 'GRAPH_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_DELETE', 'GRAPH_DELETE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_META_UPDATE', 'GRAPH_META_UPDATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_UPDATE', 'GRAPH_UPDATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_LIST', 'GRAPH_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_NODE_UPDATE', 'GRAPH_NODE_UPDATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_START', 'GRAPH_START');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_NODE_STATUS', 'GRAPH_NODE_STATUS');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_STOP', 'GRAPH_STOP');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_DETAIL', 'GRAPH_DETAIL');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_NODE_OUTPUT', 'GRAPH_NODE_OUTPUT');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'GRAPH_NODE_LOGS', 'GRAPH_NODE_LOGS');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'INDEX', 'INDEX');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_ROUTE_CREATE', 'NODE_ROUTE_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_ROUTE_PAGE', 'NODE_ROUTE_PAGE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_ROUTE_GET', 'NODE_ROUTE_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_ROUTE_UPDATE', 'NODE_ROUTE_UPDATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_ROUTE_LIST_NODE', 'NODE_ROUTE_LIST_NODE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_ROUTE_REFRESH', 'NODE_ROUTE_REFRESH');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_ROUTE_DELETE', 'NODE_ROUTE_DELETE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_CREATE', 'PRJ_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_LIST', 'PRJ_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_GET', 'PRJ_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_UPDATE', 'PRJ_UPDATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_DELETE', 'PRJ_DELETE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_ADD_INST', 'PRJ_ADD_INST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_ADD_NODE', 'PRJ_ADD_NODE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_ADD_TABLE', 'PRJ_ADD_TABLE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_DATATABLE_DELETE', 'PRJ_DATATABLE_DELETE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_DATATABLE_GET', 'PRJ_DATATABLE_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_JOB_LIST', 'PRJ_JOB_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_JOB_GET', 'PRJ_JOB_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_JOB_STOP', 'PRJ_JOB_STOP');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_TASK_LOGS', 'PRJ_TASK_LOGS');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'PRJ_TASK_OUTPUT', 'PRJ_TASK_OUTPUT');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'USER_CREATE', 'USER_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'USER_GET', 'USER_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'REMOTE_USER_RESET_PWD', 'REMOTE_USER_RESET_PWD');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'REMOTE_USER_CREATE', 'REMOTE_USER_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'REMOTE_USER_LIST_BY_NODE', 'REMOTE_USER_LIST_BY_NODE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_USER_RESET_PWD', 'NODE_USER_RESET_PWD');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_USER_CREATE', 'NODE_USER_CREATE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_USER_LIST_BY_NODE', 'NODE_USER_LIST_BY_NODE');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_USER_NODE_LIST', 'NODE_USER_NODE_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_USER_REMOTE_NODE_LIST', 'NODE_USER_REMOTE_NODE_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'NODE_USER_OTHER_NODE_BASE_INFO_LIST', 'NODE_USER_OTHER_NODE_BASE_INFO_LIST');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'AUTH_LOGIN', 'AUTH_LOGIN');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'AUTH_LOGOUT', 'AUTH_LOGOUT');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'ENV_GET', 'ENV_GET');
insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'USER_UPDATE_PWD', 'USER_UPDATE_PWD');


-----------
--  role --
insert ignore into sys_role(role_code, role_name)
values ('ADMIN', '管理员');
insert ignore into sys_role(role_code, role_name)
values ('EDGE_NODE', 'Edge node rpc');
insert ignore into sys_role(role_code, role_name)
values ('EDGE_USER', 'Edge 用户');
insert ignore into sys_role(role_code, role_name)
values ('P2P_NODE', 'P2P 用户');

-----------
-- role resource --
------------
-- Center admin user
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('CENTER_ADMIN', 'ALL_INTERFACE_RESOURCE');
-- Edge user common
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'INDEX');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'ENV_GET');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'INDEX');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'ENV_GET');
-- Edge user manage
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'NODE_USER_CREATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'NODE_USER_RESET_PWD');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'NODE_USER_LIST_BY_NODE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'NODE_USER_CREATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'NODE_USER_RESET_PWD');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'NODE_USER_LIST_BY_NODE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'NODE_USER_NODE_LIST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'NODE_USER_REMOTE_NODE_LIST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'NODE_USER_OTHER_NODE_BASE_INFO_LIST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'DATA_CREATE_DATA');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'PRJ_ADD_TABLE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'NODE_UPDATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'NODE_ROUTE_UPDATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'NODE_ROUTE_DELETE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'DATATABLE_DELETE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'PRJ_DATATABLE_DELETE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'PRJ_UPDATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'DATA_CREATE');

-- Edge user login permission
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'AUTH_LOGIN');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'AUTH_LOGOUT');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'USER_GET');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'USER_UPDATE_PWD');
-- Edge user permission
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_COMM_BATH');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_COMM_GET');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_COMM_I18N');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_COMM_LIST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_CREATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_DELETE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_DETAIL');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_LIST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_META_UPDATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_NODE_LOGS');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_NODE_OUTPUT');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_NODE_STATUS');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_NODE_UPDATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_START');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_STOP');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'GRAPH_UPDATE');

insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_ADD_INST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_ADD_NODE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_ADD_TABLE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_CREATE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_DATATABLE_DELETE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_DATATABLE_GET');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_DELETE');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_GET');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_JOB_GET');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_JOB_LIST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_JOB_STOP');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_LIST');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_TASK_LOGS');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_TASK_OUTPUT');
insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_USER', 'PRJ_UPDATE');

insert ignore into sys_role_resource_rel(role_code, resource_code)
values ('EDGE_NODE', 'DATATABLE_CREATE');

insert ignore into sys_resource(resource_type, resource_code, resource_name)
values ('API', 'DATATABLE_CREATE', 'DATATABLE_CREATE');

