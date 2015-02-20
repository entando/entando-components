INSERT INTO sysconfig(
            version, item, descr, config)
    VALUES ('production', 'jpsharedocs_config', 'Share documents configuration', '<?xml version="1.0" encoding="UTF-8"?>
<config>
   <removal>
      <attachmentDeletion>true</attachmentDeletion>
      <deletion>false</deletion>
   </removal>
   <tmpWorkingPath>/tmp/path</tmpWorkingPath>
</config>');


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked ) VALUES ( 'jpsharedocs_edit', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Share Docs - Edit document</property>
<property key="it">Share Docs - Edita documento</property>
</properties>', NULL, 'jpsharedocs', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="typeCode">SDC</property>
<property key="actionPath">/ExtStr2/do/jpsharedocs/Document/entry</property>
</properties>', 1 );


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked ) VALUES ( 'jpsharedocs_comment', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Share Docs - Comment document</property>
<property key="it">Share Docs - Commenta Documento</property>
</properties>', NULL, 'jpsharedocs', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="typeCode">SDC</property>
<property key="actionPath">/ExtStr2/do/jpsharedocs/Document/entryComment</property>
</properties>', 1 );


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked ) VALUES ( 'jpsharedocs_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Share Docs - Document details</property>
<property key="it">Share Docs - Dettagli del documento</property>
</properties>', NULL, 'jpsharedocs', NULL, NULL, 1 );


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked ) VALUES ( 'jpsharedocs_comments_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Share Docs - Document comments</property>
<property key="it">Share Docs - Commenti del documento</property>
</properties>', NULL, 'jpsharedocs', NULL, NULL, 1 );


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked ) VALUES ( 'jpsharedocs_versions_viewer', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Share Docs - Document versions</property>
<property key="it">Share Docs - Versioni del documento</property>
</properties>', NULL, 'jpsharedocs', NULL, NULL, 1 );

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
    VALUES ('jpsharedocs_list','<?xml version="1.0" encoding="UTF-8"?>
<properties><property key="en">Share Docs - Publish a list of shared documents</property><property key="it">Share Docs - Pubblica lista di documenti condivisi</property></properties>','<config>
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
	<action name="listViewerConfig"/>
</config>','jpsharedocs', null,null,1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) 
 VALUES ('jpsharedocs_trash', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Share Docs - Remove document</property>
<property key="it">Share Docs - Rimuovi documento</property>
</properties>', NULL, 'jpsharedocs', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpsharedocs/Document/trashContent</property>
</properties>', 1, NULL);

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP1', 'en', 'Step 1 of 4');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP1', 'it', 'Passo 1 di 4');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP2', 'en', 'Step 2 of 4');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP2', 'it', 'Passo 2 di 4');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP3', 'en', 'Step 3 of 4');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP3', 'it', 'Passo 3 di 4');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP4', 'en', 'Step 4 of 4');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_STEP4', 'it', 'Passo 4 di 4');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_INFO', 'en', 'Document details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_INFO', 'it', 'Informazioni sul documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_CATEGORIES', 'en', 'Document Categories');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_CATEGORIES', 'it', 'Categorie del documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_GROUPS', 'en', 'Document Groups');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_GROUPS', 'it', 'Gruppi del Documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_CONFIRM_DATA', 'en', 'Verifiy inserted datas');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_CONFIRM_DATA', 'it', 'Verifica i dati inseriti');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_TITLE', 'en', 'Title');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_TITLE', 'it', 'Titolo');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_AUTHOR', 'en', 'Author');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_AUTHOR', 'it', 'Autore');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_DESCRIPTION', 'en', 'Document Description');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_DESCRIPTION', 'it', 'Descrizione Documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_DOCUMENT', 'en', 'Document');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_DOCUMENT', 'it', 'Documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_NEW_VERSION', 'en', 'Document New Version');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_NEW_VERSION', 'it', 'Documento Nuova Versione');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_REMOVE_ATTACH', 'en', 'Remove attached file');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_REMOVE_ATTACH', 'it', 'Rimuovi file allegato');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_VERSION_DESCR', 'en', 'Version Description');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_VERSION_DESCR', 'it', 'Descrizione Versione');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_NEWVERSION_DESCR', 'en', 'New Version Description');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_NEWVERSION_DESCR', 'it', 'Indicazioni Nuova Versione
');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_VERSION_UNCHANGED', 'en', 'Last version unchanged');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_VERSION_UNCHANGED', 'it', 'Ultima versione invariata');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_NEXT_STEP', 'en', 'Next');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_NEXT_STEP', 'it', 'Avanti');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_PREV_STEP', 'en', 'Prev');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_PREV_STEP', 'it', 'Indietro');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_JOIN', 'en', 'Join');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_JOIN', 'it', 'Associa');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_SAVE', 'en', 'Save');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_SAVE', 'it', 'Salva');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CATEGORIES_TABLE_SUMMARY', 'en', 'Summary of Categories related to the Content');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CATEGORIES_TABLE_SUMMARY', 'it', 'Riepilogo delle Categorie associate al Contenuto');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CATEGORIES_TABLE_CAPTION', 'en', 'Related Categories');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CATEGORIES_TABLE_CAPTION', 'it', 'Categorie associate');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_REMOVE_CATEGORY', 'en', 'Remove category');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_REMOVE_CATEGORY', 'it', 'Rimuovi categoria');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CATEGORY', 'en', 'Category');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CATEGORY', 'it', 'Categoria');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_MAINGROUP', 'en', 'Main Group');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_MAINGROUP', 'it', 'Gruppo principale');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_EXTRA_GROUPS', 'en', 'Only view Groups');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_EXTRA_GROUPS', 'it', 'Gruppi di sola visualizzazione');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_REMOVE_GROUP', 'en', 'Remove Group');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_REMOVE_GROUP', 'it', 'Rimuovi Gruppo');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_NO_EXTRA_GROUPS', 'en', 'No only view groups related to this document.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_NO_EXTRA_GROUPS', 'it', 'Nessun gruppo di sola visualizzazione configurato.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_JOIN_GROUP', 'en', 'Join Group');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_JOIN_GROUP', 'it', 'Associa Gruppo');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_GOTO_EDITINFO', 'en', 'Edit the informations');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_GOTO_EDITINFO', 'it', 'Modifica le informazioni');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_GOTO_EDITCATEGORIES', 'en', 'Edit the Categories');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_GOTO_EDITCATEGORIES', 'it', 'Modifica le Categorie');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_GOTO_EDITGROUPS', 'en', 'Edit the related Groups');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_GOTO_EDITGROUPS', 'it', 'Edit the Gruppi associati');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_SAVED_CONFIRMATION', 'en', 'Document successfully saved.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_SAVED_CONFIRMATION', 'it', 'Documento aggiunto correttamente.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_ADD_NEW', 'en', 'Create a new document');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_ADD_NEW', 'it', 'Aggiungi un altro documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_ADDINFO', 'en', 'Insert the text comment in the field below');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_ADDINFO', 'it', 'Inserisci il testo del commento nel campo sottostante');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_TEXT', 'en', 'Comment');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_TEXT', 'it', 'Commento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_SAVED_CONFIRMATION', 'en', 'Comment successfully saved.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_SAVED_CONFIRMATION', 'it', 'Il commento è stato salvato correttamente.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_INFO', 'en', 'Document Detail');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_DOC_INFO', 'it', 'Dettaglio del Documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_ADD_NEW', 'en', 'Add another comment');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_ADD_NEW', 'it', 'Aggiungi un altro commento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_RELATED_DOCS', 'en', 'Related Documents');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_RELATED_DOCS', 'it', 'Documenti Correlati');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_ADDITIONAL_INFOS', 'en', 'Additional Informations');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_ADDITIONAL_INFOS', 'it', 'Informazioni Aggiuntive');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_VERSIONS_DETAILS', 'en', 'Versions Details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_VERSIONS_DETAILS', 'it', 'Dettaglio delle Versioni');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENTS_DETAILS', 'en', 'Comments Details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENTS_DETAILS', 'it', 'Dettaglio dei Commenti');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_VERSIONS_HISTORY', 'en', 'Versions History');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_VERSIONS_HISTORY', 'it', 'Storico Versioni');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENTS_HISTORY', 'en', 'Comments');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENTS_HISTORY', 'it', 'Commenti');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENTS_FOR', 'en', 'Comments for');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENTS_FOR', 'it', 'Commenti per');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_NO_COMMENTS', 'en', 'No comments.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_NO_COMMENTS', 'it', 'Nessun commento presente.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_DOC', 'en', 'Edit Document');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_DOC', 'it', 'Modifica Documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_THIS_DOC', 'en', 'Add a comment for this document');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_COMMENT_THIS_DOC', 'it', 'Aggiungi un commento per questo documento');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CHECK_IN', 'en', 'Check In');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CHECK_IN', 'it', 'Check In');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CHECK_OUT', 'en', 'Check Out');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_CHECK_OUT', 'it', 'Check Out');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LOCKED_BY', 'en', 'This document was locked by:');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LOCKED_BY', 'it', 'Questo documento è stato bloccato da:');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_CONTINUE', 'en', 'Continue editing locked content');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_EDIT_CONTINUE', 'it', 'Modifica il contenuto bloccato');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_NEW', 'en', 'New document');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_NEW', 'it', 'Nuovo document');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LIST_FULL', 'en', 'Full list');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LIST_FULL', 'it', 'Lista completa');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LIST', 'en', 'Return to list');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LIST', 'it', 'Ritorna alla lista');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_ARE_YOU_SURE', 'en', 'Are you sure to delete the content and its attachment');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_ARE_YOU_SURE', 'it', 'Sei sicuro di voler procedere con la rimozione del contenuto con gli allegati');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_TRASH', 'en', 'Delete shared document');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_TRASH', 'it', 'Cancella documento condiviso');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_REMOVE', 'en', 'Delete');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_REMOVE', 'it', 'Cancella');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_INSUFFICENT_PERMISSION', 'en', 'Insufficient privileges');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_INSUFFICENT_PERMISSION', 'it', 'Privilegi insufficienti');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LIST_NO_CONTENT_FOUND', 'en', 'No contents found');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpsharedocs_LIST_NO_CONTENT_FOUND', 'it', 'Nessun contenuto trovato');