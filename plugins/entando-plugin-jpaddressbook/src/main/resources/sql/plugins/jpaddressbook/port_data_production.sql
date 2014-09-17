INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpaddressbook_vcardConfig', 'Configurazione campi vcard', '<?xml version="1.0" encoding="UTF-8"?>
<vcardfields>
	<vcardfield code="NAME" description="Nome" enabled="true" profileAttribute="" />
	<vcardfield code="SURNAME" description="Cognome" enabled="true" profileAttribute="" />
	<vcardfield code="TITLE" description="Titolo" enabled="true" profileAttribute="PaeseResidenza" />
	<vcardfield code="ROLE" description="Ruolo Lavorativo" enabled="false" profileAttribute="" />
	<vcardfield code="ORG" description="Azienda" enabled="false" profileAttribute="" />
	<vcardfield code="BDAY" description="Data di Nascita" enabled="false" profileAttribute="" />
	<vcardfield code="ADDR_HOME_COUNTRY" description="Paese Residenza" enabled="false" profileAttribute="" />
	<vcardfield code="ADDR_HOME_REGION" description="Regione Residenza" enabled="false" profileAttribute="" />
	<vcardfield code="ADDR_HOME_STREET" description="Indirizzo Residenza" enabled="false" profileAttribute="" />
	<vcardfield code="ADDR_HOME_CITY" description="Citta'' Residenza" enabled="false" profileAttribute="" />
	<vcardfield code="ADDR_HOME_CAP" description="Cap Citta'' Residenza" enabled="false" profileAttribute="" />
	<vcardfield code="TEL_CELL" description="Telefono Cellulare" enabled="false" profileAttribute="" />
	<vcardfield code="TEL_HOME" description="Telefono Casa" enabled="false" profileAttribute="" />
	<vcardfield code="TEL_WORK" description="Telefono Lavoro" enabled="false" profileAttribute="" />
	<vcardfield code="TEL_FAX" description="Telefono Fax" enabled="false" profileAttribute="" />
	<vcardfield code="EMAIL_FIRST" description="Prima Email" enabled="false" profileAttribute="" />
	<vcardfield code="EMAIL_SECOND" description="Seconda Email" enabled="false" profileAttribute="" />
	<vcardfield code="EMAIL_THIRD" description="Terza Email" enabled="false" profileAttribute="" />
	<vcardfield code="NOTE" description="Note" enabled="false" profileAttribute="" />
	<vcardfield code="URL" description="Site" enabled="false" profileAttribute="" />
</vcardfields>
');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpaddressbook_search_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Address Book - Search Form</property>
<property key="it">Rubrica Interna - Form Ricerca</property>
</properties>', NULL, 'jpaddressbook', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpaddressbook/Front/AddressBook/searchIntro.action</property>
</properties>', 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpaddressbook_search_result', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Address Book - Search Risult</property>
<property key="it">Rubrica Interna - Risultati Ricerca</property>
</properties>', NULL, 'jpaddressbook', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpaddressbook/Front/AddressBook/resultIntro.action</property>
</properties>', 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpaddressbook_search_form_advanced', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Address Book - Advanced Search Form</property>
<property key="it">Rubrica Interna - Form Ricerca Avanzata</property>
</properties>', NULL, 'jpaddressbook', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpaddressbook/Front/AddressBook/searchIntroAdvanced.action</property>
</properties>', 1);


INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TRASH_CONTACT','it','Rimozione Contatto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TRASH_CONTACT','en','Delete Contact');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_PREV','it','Precedenti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_PREV','en','Previous');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_NEXT','it','Successivi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_NEXT','en','Next');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH_RESULTS_INTRO','it','Sono stati trovati');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH_RESULTS_INTRO','en','Found');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH_RESULTS_OUTRO','it','contatti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH_RESULTS_OUTRO','en','contacts');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_PAGE','it','Pagina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_PAGE','en','Page');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_VIEW_CONTACT','it','Vai ai dettagli');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_VIEW_CONTACT','en','Go to details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_BASICSEARCH','it','Ricerca Base');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_BASICSEARCH','en','Ricerca Base');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ADVANCEDSEARCH','it','Ricerca Avanzata');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ADVANCEDSEARCH','en','Advanced Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH_CONTACT','it','Ricerca Contatti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH_CONTACT','en','Search Contacts');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH','it','Cerca');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SEARCH','en','Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_CREATENEWCONTACT','it','Crea nuovo contatto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_CREATENEWCONTACT','en','Create new contact');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_FIRSTNAME','it','Nome');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_FIRSTNAME','en','First Name');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SURNAME','it','Cognome');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SURNAME','en','Surname');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_EMAIL','it','E-Mail');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_EMAIL','en','E-Mail');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ACTION','it','Azioni');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ACTION','en','Actions');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_START_VALUE','it','valore iniziale');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_START_VALUE','en','start value');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_END_VALUE','it','valore finale');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_END_VALUE','en','end value');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_YES_VALUE','it','Si');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_YES_VALUE','en','Yes');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_NO_VALUE','it','No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_NO_VALUE','en','No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_BOTH_VALUE','it','Si e no');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_ATTRIBUTE_BOTH_VALUE','en','Yes and No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_EDITACTION','it','Modifica');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_EDITACTION','en','Edit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_DELETEACTION','it','Elimina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_DELETEACTION','en','Delete');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_NOCONTACTSFOUND','it','Nessun contatto trovato.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_NOCONTACTSFOUND','en','No contact found.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TITLE','it','Rubrica Contatti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TITLE','en','Address Book');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_CONTACT_DETAILS','it','Dettagli contatto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_CONTACT_DETAILS','en','Contact details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_PUBLIC_CONTACT','it','Contatto pubblico');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_PUBLIC_CONTACT','en','Public Contact');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_GOTO_ADDRESSBOOK','it','Vai alla Rubrica Contatti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_GOTO_ADDRESSBOOK','en','Go to Address Book');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_RU_SURE_DELETE','it','Sei sicuro di voler cancellare il contatto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_RU_SURE_DELETE','en','Are You sure do you want to delete the contact');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_YESDELETE','it','Si, rimuovi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_YESDELETE','en','Yes, delete');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TITLE_NEWCONTACT','it','Nuovo Contatto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TITLE_NEWCONTACT','en','New Contact');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TITLE_EDITCONTACT','en','Edit Contact');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_TITLE_EDITCONTACT','it','Modifica Contatto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_U_NEED_TO_LOGIN','it','Per accedere a questo servizio è neccessario effettuare l''accesso.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_U_NEED_TO_LOGIN','en','In order to use this service please login.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SAVE_CONTACT','it','Salva Contatto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpaddressbook_SAVE_CONTACT','en','Save Contact');