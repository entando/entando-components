INSERT INTO sysconfig ( version, item, descr, config ) VALUES ( 'test', 'jpblog_config', 'Blog configuration', '<?xml version="1.0" encoding="UTF-8" standalone="yes"?><blogConfig><categories /></blogConfig>' );


INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpblog_archive', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Blog - Blog Archive</property>
<property key="it">Blog- Archivio Blog</property>
</properties>', '<config>
  <parameter name="contentType">Content Type (mandatory)</parameter>
	<action name="jpblogArchiveViewerConfig"/>
</config>', 'jpblog', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpblog_categories', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Blog - Categories Summary</property>
<property key="it">Blog - Riepilogo Categorie</property>
</properties>', '', 'jpblog', NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpblog_post_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Blog - Post List</property>
<property key="it">Blog - Lista Post</property>
</properties>', '<config>
  	<parameter name="contentType">Content Type (mandatory)</parameter>
	<parameter name="modelId">Content Model</parameter>
	<parameter name="userFilters">Front-End user filter options</parameter>
	<parameter name="category">Content Category **deprecated**</parameter>
	<parameter name="categories">Content Category codes (comma separeted)</parameter>
	<parameter name="orClauseCategoryFilter" />
	<parameter name="maxElemForItem">Contents for each page</parameter>
	<parameter name="maxElements">Number of contents</parameter>
	<parameter name="filters" />
	<parameter name="title_{lang}">Widget Title in lang {lang}</parameter>
	<parameter name="pageLink">The code of the Page to link</parameter>
	<parameter name="linkDescr_{lang}">Link description in lang {lang}</parameter>
	<parameter name="anonymousComment">Enable content rating (true|false)</parameter>
	<parameter name="usedContentRating">Enable content rating (true|false)</parameter>
	<parameter name="usedComment">Enable user comments (true|false)</parameter>
	<parameter name="usedCommentWithRating">Enable rating on comments (true|false)</parameter>
	<parameter name="commentValidation">Enable administrator moderation of comments (true|false)</parameter>
	<action name="jpblogBlogListViewerConfig"/>
</config>', 'jpblog', NULL, NULL, 1);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpblog_ARCHIVE_TITLE', 'it', 'Articoli del Blog');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpblog_ARCHIVE_TITLE', 'en', 'Blog Archive');

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpblog_CATEGORIES_TITLE', 'it', 'Categorie Blog');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jpblog_CATEGORIES_TITLE', 'en', 'Blog Categories');