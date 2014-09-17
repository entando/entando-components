INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_CONTENT_RATING', 'it', 'Valutazione del Contenuto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_CONTENT_RATING', 'en', 'Content Rating');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_AVG_RATING', 'it', 'Voto medio dei lettori:');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_AVG_RATING', 'en', 'Average users rating:');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTERS_NUM', 'it', 'votanti.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTERS_NUM', 'en', 'voters.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTERS_NULL', 'it', 'Ancora nessun utente ha votato questo contenuto.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTERS_NULL', 'en', 'No rating yet.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTE', 'it', 'Valuta');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTE', 'en', 'Rate it Now');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTE_SUBMIT', 'it', 'Invia la Valutazione');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_VOTE_SUBMIT', 'en', 'Send Rating');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENTS', 'it', 'Commenti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENTS', 'en', 'Comments');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_AUTHOR', 'it', 'Autore');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_AUTHOR', 'en', 'Author');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_DATE', 'it', 'Data');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_DATE', 'en', 'Date');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_TEXT', 'it', 'Testo Commento');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_TEXT', 'en', 'Comment Text');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_DELETE', 'it', 'Cancella');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_DELETE', 'en', 'Delete');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_RATING', 'it', 'Valutazione Commento');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_RATING', 'en', 'Comment Rating');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_NORATING', 'it', 'Nessuna valutazione da parte degli utenti.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_NORATING', 'en', 'No rating yet.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_LABEL_RATING', 'it', 'Voto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_LABEL_RATING', 'en', 'Rating Value');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_SEND', 'it', 'Invia');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_SEND', 'en', 'Send');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_THE_CONTENT', 'it', 'Commenta il Contenuto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENT_THE_CONTENT', 'en', 'Comment this Content');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_LABEL_COMMENTTEXT', 'it', 'Testo del Commento');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_LABEL_COMMENTTEXT', 'en', 'Comment Text');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENTS_NULL', 'it', 'Ancora nessun commento per questo contenuto.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcontentfeedback_COMMENTS_NULL', 'en', 'No comments yet.');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('content_feedback_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Content Feedback - Publish a Content</property>
<property key="it">Feedback Contenuti - Pubblica un Contenuto</property>
</properties>', '<config>
	<parameter name="contentId">Content ID</parameter>
	<parameter name="modelId">Content Model ID</parameter>
	<parameter name="anonymousComment">Enable content rating (true|false)</parameter>
	<parameter name="usedContentRating">Enable content rating (true|false)</parameter>
	<parameter name="usedComment">Enable user comments (true|false)</parameter>
	<parameter name="usedCommentWithRating">Enable rating on comments (true|false)</parameter>
	<parameter name="commentValidation">Enable administrator moderation of comments (true|false)</parameter> 
	<action name="viewerContentFeedbackConfig" />
</config>','jpcontentfeedback',NULL,NULL,1);





INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpcontentfeedback_config', 'Content Feedback global configuration', '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<contentFeedbackConfig>
		<anonymousComment>true</anonymousComment>
		<comment>true</comment>
		<rateComment>true</rateComment>
		<rateContent>true</rateContent>
</contentFeedbackConfig>');