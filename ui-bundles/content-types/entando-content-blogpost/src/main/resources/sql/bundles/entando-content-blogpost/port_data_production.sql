INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_TAGS_INTRO', 'en', 'Tags');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_TAGS_INTRO', 'it', 'Tag');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_TAGS_FILTER_BY', 'en', 'Filter by');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_TAGS_FILTER_BY', 'it', 'Filtra per');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_WRITTEN_BY', 'en', 'by');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_WRITTEN_BY', 'it', 'scritto da');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_WRITTEN_ON', 'en', 'on');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('BLG_WRITTEN_ON', 'it', 'il');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('blog_postlist', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Blog - Post List</property>
<property key="it">Blog - Lista Post</property>
</properties>

', NULL, NULL, 'jpblog_post_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="maxElemForItem">1</property>
<property key="commentValidation">true</property>
<property key="usedContentRating">true</property>
<property key="filters">(order=DESC;attributeFilter=true;key=DateTime)</property>
<property key="usedCommentWithRating">true</property>
<property key="usedComment">true</property>
<property key="contentType">BLG</property>
</properties>

', 0, 'free');