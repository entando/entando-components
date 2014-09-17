INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, locked )
	VALUES ( 'jpfastcontentedit_content_viewer_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Contents - Publish List of Editable Content</property>
<property key="it">Contenuti - Pubblica una Lista di contenuti editabili da Front-End</property>
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
	<action name="listViewerConfig"/>
</config>', 'jpfastcontentedit', 1 );

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
	VALUES ('jpfastcontentedit_formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Content Editing Form</property>
<property key="it">Form Editing Contenuti</property>
</properties>', '<config>
	<parameter name="typeCode">Content Type (optional)</parameter>
	<parameter name="authorAttribute">Name of attribute containing Author name (optional)</parameter>
	<action name="jpfastcontenteditConfig"/>
</config>', 'jpfastcontentedit', NULL, NULL, 1);


INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE', 'it', 'Bacheca Annunci' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE', 'en', 'Notice Board' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_USER_NOT_ALLOWED', 'it', 'Operazione non consentita' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_USER_NOT_ALLOWED', 'en', 'Operation not allowed' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_GENERIC_ERROR', 'it', 'Si è verificato un errore' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_GENERIC_ERROR', 'en', 'An error is happened' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_ERRORS', 'it', 'Errori' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_ERRORS', 'en', 'Errors' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_WORKING_ON', 'it', 'Stai lavorando a' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_WORKING_ON', 'en', 'You are working on' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONTENT_FREE', 'it', 'Contenuto visibile a tutti' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONTENT_FREE', 'en', 'Content visible to all' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SAVE', 'it', 'Salva' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SAVE', 'en', 'Save' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SEARCH', 'it', 'Cerca' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SEARCH', 'en', 'Search' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONTINUE', 'it', 'Continua' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONTINUE', 'en', 'Continue' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_URL', 'it', 'Link a url' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_URL', 'en', 'Link to url' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_PAGE', 'it', 'Link a pagina' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_PAGE', 'en', 'Link to page' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_CONTENT', 'it', 'Link a contenuto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_CONTENT', 'en', 'Link to content' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_CONTENT_ON_PAGE', 'it', 'su pagina' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINK_TO_CONTENT_ON_PAGE', 'en', 'on page' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE', 'it', 'Configura' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE', 'en', 'Configure' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_REMOVE', 'it', 'Rimuovi' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_REMOVE', 'en', 'Remove' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_DO_THIS_ON_DEFAULT_LANG', 'it', 'Questo Attributo può essere usato solamente nella sezione della lingua di default' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_DO_THIS_ON_DEFAULT_LANG', 'en', 'You can only fill this Attribute from the default language section' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE', 'it', 'Configura Attributo Link' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE', 'en', 'Configure Link Attribute' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_STEP_1_OF_2', 'it', '(Passo 1 di 2)' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_STEP_1_OF_2', 'en', '(Step 1 of 2)' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_STEP_2_OF_2', 'it', '(Passo 2 di 2)' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_STEP_2_OF_2', 'en', '(Step 2 of 2)' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CHOOSE_LINK_TYPE', 'it', 'Scelta tipo di link' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CHOOSE_LINK_TYPE', 'en', 'Choose link type' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_LINK_TO_CONTENT', 'it', 'Scegli un Contenuto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_LINK_TO_CONTENT', 'en', 'Choose a Content' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SEARCH_FOR_DESCR', 'it', 'Cerca per Descrizione:' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SEARCH_FOR_DESCR', 'en', 'Search for Description:' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SEARCH_FILTERS', 'it', 'Raffina la Ricerca' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SEARCH_FILTERS', 'en', 'Refine your Search' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_TYPE', 'it', 'Tipo' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_TYPE', 'en', 'Type' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SELECT_ALL', 'it', 'Tutti' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_SELECT_ALL', 'en', 'All' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_STATUS', 'it', 'Stato' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_STATUS', 'en', 'Status' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINKATTRIBUTE_CONTENT_SUMMARY', 'it', 'La tabella mostra l''elenco dei contenuti disponibili per la configurazione del LinkAttribute. Per ogni contenuto è indicata la descrizione, il codice, il Gruppo di Utenti a cui appartiene, la data di creazione e quella di ultima modifica.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINKATTRIBUTE_CONTENT_SUMMARY', 'en', 'This table shows a list of contents available for the configuration of LinkAttribute. For each content it shows description, code, group, date of creation and of last modification.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINKATTRIBUTE_CONTENT_CAPTION', 'it', 'Lista Contenuti' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LINKATTRIBUTE_CONTENT_CAPTION', 'en', 'Content List' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_DESCRIPTION', 'it', 'Descrizione' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_DESCRIPTION', 'en', 'Description' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_GROUP', 'it', 'Gruppo' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_GROUP', 'en', 'Group' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CREATION_DATE', 'it', 'Creazione' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CREATION_DATE', 'en', 'Creation' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LAST_EDIT', 'it', 'Ultima Modifica' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LAST_EDIT', 'en', 'Last Edit' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_MAKE_CONTENT_ON_PAGE', 'it', 'Prosegui scegliendo anche una Pagina di pubblicazione temporanea' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_MAKE_CONTENT_ON_PAGE', 'en', 'Continue choosing a Page for on-the-fly publishing' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_LINK_TO_PAGE', 'it', 'Scegli una  Pagina' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_LINK_TO_PAGE', 'en', 'Choose a Page' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_FOR_THE_CONTENT', 'it', 'per il Contenuto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_FOR_THE_CONTENT', 'en', 'for the Content' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_PAGE_TREE', 'it', 'Albero delle Pagine' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_PAGE_TREE', 'en', 'Page Tree' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIRM', 'it', 'Conferma' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIRM', 'en', 'Confirm' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_URL', 'it', 'Inserisci un URL valido (esempio: http://www.miosito.it/)' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CONFIGURE_URL', 'en', 'Insert a valid URL (ex: http://www.miosito.it/)' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_URL', 'it', 'URL' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_URL', 'en', 'URL' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_PREVIOUS_LINK', 'it', 'In precedenza era stato configurato un' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_PREVIOUS_LINK', 'en', 'Previously configured as' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_EDIT_CONTENT', 'it', 'Modifica' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_EDIT_CONTENT', 'en', 'Edit' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_REMOVE_CONTENT', 'it', 'Elimina' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_REMOVE_CONTENT', 'en', 'Remove' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LABEL_NEW_CONTENT', 'it', 'Crea un nuovo contenuto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_LABEL_NEW_CONTENT', 'en', 'Create a new content' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CHOOSE_ATTACH', 'it', 'Scegli un allegato' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CHOOSE_ATTACH', 'en', 'Choose an attachment' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CHOOSE_IMAGE', 'it', 'Scegli un''immagine' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CHOOSE_IMAGE', 'en', 'Choose an image' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CATEGORY', 'it', 'Categoria' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_CATEGORY', 'en', 'Category' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_JOIN', 'it', 'Associa' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_JOIN', 'en', 'Join' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_JOIN_TO', 'it', 'Associa al Contenuto' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpfastcontentedit_JOIN_TO', 'en', 'Join to Content' );

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_LABEL_NEW_RESOURCE', 'en', 'New Resource');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_LABEL_NEW_RESOURCE', 'it', 'Nuova Risorsa');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_LABEL_NEW_IMAGE', 'en', 'New Image');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_LABEL_NEW_IMAGE', 'it', 'Nuova Immagine');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_LABEL_NEW_ATTACH', 'en', 'New Attachment');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_LABEL_NEW_ATTACH', 'it', 'Nuova Allegato');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_BACK_TO_CONTENT', 'en', 'Back to the Content');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_BACK_TO_CONTENT', 'it', 'Torna al Contenuto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_REQUIRED_SHORT', 'en', '*');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_REQUIRED_SHORT', 'it', '*');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_REQUIRED_FULL', 'en', 'Required');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_REQUIRED_FULL', 'it', 'Obbligatorio');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_INFO', 'en', 'Info');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_INFO', 'it', 'Info');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_FILE', 'en', 'File');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_FILE', 'it', 'File');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_CATEGORIES_MANAGEMENT', 'en', 'Categories Management');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_CATEGORIES_MANAGEMENT', 'it', 'Gestione delle Categorie');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_RESOURCES_CATEGORIES_SUMMARY', 'en', 'The table shows the categories associated with the resource. For each category shows the description (also known as the title), and you can remove it from the table.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_RESOURCES_CATEGORIES_SUMMARY', 'it', 'La tabella mostra le Categorie associate alla Risorsa. Per ogni categoria è indicata la descrizione (nota anche come titolo), ed è possibile rimuoverla dalla tabella.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_RESOURCES_CATEGORIES', 'en', 'Categories');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_RESOURCES_CATEGORIES', 'it', 'Categorie');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_RESOURCES_CATEGORY', 'en', 'Category');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpfastcontentedit_RESOURCES_CATEGORY', 'it', 'Categoria');
