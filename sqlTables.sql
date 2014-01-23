CREATE TABLE `mdl_prg_user` (
  `username` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `passwrd` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `realname` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
)

CREATE TABLE `mdl_prg_ques` (
  `testid` int(11) NOT NULL,
  `qno` int(11) NOT NULL,
  `question` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  `lang` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL
)


CREATE TABLE `mdl_prg_test1` (
  `qno` int(11) DEFAULT NULL,
  `username` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(8000) COLLATE utf8_unicode_ci DEFAULT NULL
)

