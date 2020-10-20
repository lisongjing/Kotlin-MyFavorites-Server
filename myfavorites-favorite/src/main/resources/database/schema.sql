CREATE TABLE IF NOT EXISTS `favorites`(
   `id` INT UNSIGNED AUTO_INCREMENT,
   `favorite_id` VARCHAR(100) NOT NULL,
   `user_id` VARCHAR(100) NOT NULL,
   `title` VARCHAR(400),
   `abbreviation` VARCHAR(400),
   `data_id` VARCHAR(100) NOT NULL,
   `public` TINYINT(1) NOT NULL DEFAULT 0,
   `create_time` TIMESTAMP,
   `modified_time` TIMESTAMP,
   `version` INT UNSIGNED NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `favorite_id_unique` (`favorite_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;