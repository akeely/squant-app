
ALTER TABLE `bets` ADD COLUMN `externalId` VARCHAR(255);
ALTER TABLE `bets` ADD COLUMN `accepted` BOOLEAN NOT NULL DEFAULT 0;

ALTER TABLE `users` ADD COLUMN `externalId` VARCHAR(255);