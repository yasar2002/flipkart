CREATE TABLE `user` (
  `uid` varchar[pk],
  `fname` varchar[notnull],
  `lname` varchar[notnull],
  `email` varchar[uq][notnull],
  `status` integer[notnull],
  `password` longtext[notnull]
);

CREATE TABLE `session` (
  `uid` varchar[notnull],
  `token` varchar[unique],
  `expiry` date[notnull],
  `status` integer[notnull]
);

CREATE TABLE `Product_details` (
  `pid` varchar[pk],
  `uid` varchar[notnull],
  `type` varchar[notnull],
  `pimage` blob,
  `pname` varchar[notnull],
  `pdesc` varchar[notnull],
  `pqua` integer[notnull]
);

CREATE TABLE `Order` (
  `sno` integer[autoincrement],
  `uid` varchar[notnull],
  `pid` varchar[notnull],
  `quan` integer[notnull],
  `price` integer[notnull]
);

ALTER TABLE `session` ADD CONSTRAINT `rela` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

ALTER TABLE `Product_details` ADD CONSTRAINT `rel` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

ALTER TABLE `Order` ADD CONSTRAINT `rel` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

ALTER TABLE `Order` ADD CONSTRAINT `rel` FOREIGN KEY (`pid`) REFERENCES `Product_details` (`pid`);
