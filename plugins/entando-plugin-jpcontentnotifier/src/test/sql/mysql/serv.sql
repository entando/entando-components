CREATE TABLE `jpcontentnotifier_contentchangingevents` (
  `id` int(11) NOT NULL,
  `eventdate` timestamp NOT NULL DEFAULT 0,
  `operationcode` int(11) NOT NULL,
  `contentid` varchar(16) NOT NULL,
  `contenttype` varchar(30) NOT NULL,
  `descr` varchar(100) NOT NULL,
  `maingroup` varchar(20) NOT NULL,
  `groups` longtext,
  `notified` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;