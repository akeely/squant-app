ALTER DATABASE `squant` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE `users` (
  `id` INT AUTO_INCREMENT,
  `name` varchar(255),
  `email` varchar(255),
  PRIMARY KEY (`id`),
  UNIQUE (`email`)
);

CREATE TABLE `bets` (
  `id` INT AUTO_INCREMENT,
  `createTime` BIGINT NOT NULL,
  `creator` INT NOT NULL,
  `against` INT NOT NULL,
  `winner` INT NULL,
  `amount` DECIMAL(13,2) NOT NULL DEFAULT 1.0,
  `currency` varchar(255) NOT NULL DEFAULT '',
  `paid` BOOLEAN,
  `description` TEXT NOT NULL,
  CONSTRAINT `creator_user` FOREIGN KEY (`creator`) REFERENCES users(`id`),
  CONSTRAINT `against_user` FOREIGN KEY (`against`) REFERENCES users(`id`),
  CONSTRAINT `winner_user`  FOREIGN KEY (`winner`)  REFERENCES users(`id`),
  PRIMARY KEY (`id`)
);