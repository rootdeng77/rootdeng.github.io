CREATE TABLE  if not exists `mybatis`.`news` (
  `id` VARCHAR(25) NOT NULL,
  `category_code` INT NULL COMMENT '分类标签\n',
  `category_name` VARCHAR(45) CHARACTER SET 'utf8' NULL COMMENT '分类名称',
  `title` VARCHAR(300) CHARACTER SET 'utf8' NULL COMMENT '标题',
  PRIMARY KEY (`id`));


CREATE TABLE if not exists `mybatis`.`news_label` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `news_id` VARCHAR(45) NULL COMMENT '新闻id',
  `label` VARCHAR(20) NULL COMMENT '标签',
  PRIMARY KEY (`id`));

ALTER TABLE `mybatis`.`news_label`
CHANGE COLUMN `label` `label` VARCHAR(20) CHARACTER SET 'utf8' NULL DEFAULT NULL COMMENT '标签' ;



CREATE TABLE if not exists `mybatis`.`hot_word` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `host` VARCHAR(20) NULL COMMENT 'ip',
  `user_agent` VARCHAR(90) NULL COMMENT '浏览器',
  `content` VARCHAR(50) NULL COMMENT '搜索内容',
  PRIMARY KEY (`id`));

ALTER TABLE `mybatis`.`hot_word`
CHANGE COLUMN `content` `content` VARCHAR(50) CHARACTER SET 'utf8' NULL DEFAULT NULL COMMENT '搜索内容' ;

{
    "mappings": {
            "properties": {
                "_class": {
                    "type": "text",
                    "fields": {
                        "keyword": {
                            "type": "keyword",
                            "ignore_above": 256
                        }
                    }
                },
                "categoryCode": {
                    "type": "long"
                },
                "categoryName": {
                    "type": "text",
                    "fields": {
                        "keyword": {
                            "type": "keyword",
                            "ignore_above": 256
                        }
                    }
                },
                "id": {
                    "type": "long"
                },
                "newsId": {
                    "type": "text",
                    "fields": {
                        "keyword": {
                            "type": "keyword",
                            "ignore_above": 256
                        }
                    }
                },
                "suggestTitle": {
                    "type": "completion",
                     "analyzer": "ik_smart"
                },
                "title": {
                    "type": "text",
                    "copy_to":"suggestTitle",
                    "analyzer":"ik_smart"
                }
            }
        }
}