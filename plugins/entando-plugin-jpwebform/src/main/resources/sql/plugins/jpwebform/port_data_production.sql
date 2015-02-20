INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpwebform_messageNotifierConfig', 'Configurazione del servizio di notifica Messaggi', '<?xml version="1.0" encoding="UTF-8"?>
<messagetypes />
');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpwebform_messageTypes', 'Definizione dei Tipi di Messaggio', '<?xml version="1.0" encoding="UTF-8"?>
<formtypes>
	<formtype typecode="COM" typedescr="Company Form" version="1" repeatable="true" ignoreVersionEdit="true" ignoreVersionDisplay="false">
		<attributes>
			<attribute name="Company" attributetype="Monotext" description="company">
				<validations>
					<required>true</required>
				</validations>
			</attribute>
			<attribute name="Address" attributetype="Monotext" description="Address" />
			<attribute name="Email" attributetype="Monotext" description="Email" />
			<attribute name="Note" attributetype="Monotext" description="Note" />
		</attributes>
	</formtype>
	<formtype typecode="PER" typedescr="Personal" version="1" repeatable="true" ignoreVersionEdit="true" ignoreVersionDisplay="false">
		<attributes>
			<attribute name="Name" attributetype="Monotext" description="Name" />
			<attribute name="Surname" attributetype="Monotext" description="Surname" />
			<attribute name="Birthdate" attributetype="Date" description="Birthdate" />
		</attributes>
	</formtype>
</formtypes>

');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpwebform_stepsConfig', 'Configurazione steps form', '<?xml version="1.0" encoding="UTF-8"?>
<formtypes>
	<formtype code="PER" confirmGui="true" builtConfirmGui="true" builtEndPointGui="true">
		<step code="1" builtGui="true">
			<attribute name="Name" view="false" />
			<attribute name="Surname" view="false" />
			<attribute name="Birthdate" view="false" />
		</step>
		<step code="2" builtGui="true">
			<attribute name="Name" view="true" />
			<attribute name="Surname" view="true" />
			<attribute name="Birthdate" view="true" />
		</step>
	</formtype>
	<formtype code="COM" confirmGui="false" builtConfirmGui="false" builtEndPointGui="true">
		<step code="1" builtGui="true">
			<attribute name="Company" view="false" />
			<attribute name="Address" view="false" />
			<attribute name="Email" view="false" />
			<attribute name="Note" view="false" />
		</step>
		<step code="2" builtGui="true">
			<attribute name="Company" view="true" />
			<attribute name="Address" view="true" />
			<attribute name="Email" view="true" />
			<attribute name="Note" view="true" />
		</step>
	</formtype>
</formtypes>

');

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpwebform_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Web Forms - Publish a form</property>
<property key="it">Web Forms - Pubblica un form</property>
</properties>', '<config>
	<parameter name="typeCode">Code of the Message Type</parameter>
	<action name="webformConfig"/>
</config>', 'jpwebform', NULL, NULL, 1, NULL);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpwebform_form_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Web Forms - Show a list of forms</property>
<property key="it">Web Forms - Mostra la lista di form</property>
</properties>', '<config>
    <parameter name="status">Status (not present: all forms, true: completed forms, false: uncompleted forms)</parameter>
    <parameter name="maxElemForItem">Elements per page</parameter>
    <parameter name="maxElements">Max total elements</parameter>
    <parameter name="title_{lang}">Showlet Title in lang {lang}</parameter>
    <parameter name="pageLink">The code of the Page to link</parameter>
    <parameter name="linkDescr_{lang}">Link description in lang {lang}</parameter>
    <action name="formListViewerConfig"/>
</config>', 'jpwebform', NULL, NULL, 1, NULL);

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_INFO', 'en', 'The fields marked with * are required.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_INFO', 'it', 'I campi contrassegnati dal simbolo * sono obbligatori.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MANDATORY_FULL', 'en', 'Mandatory');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MANDATORY_FULL', 'it', 'Obbligatorio');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MANDATORY_SHORT', 'en', '*');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MANDATORY_SHORT', 'it', '*');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL', 'en', 'Max Length');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL', 'it', 'Lunghezza massima');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT', 'en', 'max');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT', 'it', 'max');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MINLENGTH_FULL', 'en', 'Min Length');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MINLENGTH_FULL', 'it', 'Lunghezza minima');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT', 'en', 'min');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT', 'it', 'min');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_INVIA', 'en', 'Send');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_INVIA', 'it', 'Invia');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_MESSAGE_SAVE_CONFIRMATION', 'it', 'Il messaggio è stato inviato correttamente. Se vuoi inviaci un''altra');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_MESSAGE_SAVE_CONFIRMATION', 'en', 'The message has been sent successfully. If you need, send us another');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_MESSAGE_REQUEST_LINK', 'en', 'request');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_MESSAGE_REQUEST_LINK', 'it', 'richiesta');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SELECT', 'en', 'Select');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SELECT', 'it', 'Seleziona');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_MESSAGETYPE', 'it', 'Scegliere un tipo di messaggio');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_MESSAGETYPE', 'en', 'Choose a message type');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_CHOOSE_TYPE', 'it', 'Scegli e continua');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_CHOOSE_TYPE', 'en', 'Select and continue');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ERRORS', 'it', 'Errori durante la compilazione del modulo');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ERRORS', 'en', 'Errors occured');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_YES', 'it', 'Si');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_YES', 'en', 'Yes');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_NO', 'it', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_NO', 'en', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_BOTH_YES_AND_NO', 'it', 'Indifferente');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_BOTH_YES_AND_NO', 'en', 'Both');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ERRORS_HAPPENED', 'it', 'Si sono verificati degli errori!');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ERRORS_HAPPENED', 'en', 'An error has happened!');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_HELP_SHORT', 'en', '?');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ENTITY_ATTR_FLAG_HELP_SHORT', 'it', '?');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LIST_VIEWER_EMPTY', 'en', 'No form.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LIST_VIEWER_EMPTY', 'it', 'Nessun form.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_RESUME_FORM', 'en', 'Resume');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_RESUME_FORM', 'it', 'Riprendi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_COMPLETED', 'en', 'Completed');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_COMPLETED', 'it', 'Completato');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_REPEATABLE', 'en', 'Can be compiled more than once');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_REPEATABLE', 'it', 'Compilabile più di una volta');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SEND_DATE', 'en', 'Send Date');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SEND_DATE', 'it', 'Data di invio');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_COMPLETED_NO', 'en', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_COMPLETED_NO', 'it', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_COMPLETED_YES', 'en', 'Yes');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_COMPLETED_YES', 'it', 'Si');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_REPEATABLE_NO', 'en', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_REPEATABLE_NO', 'it', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_REPEATABLE_YES', 'en', 'Yes');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_REPEATABLE_YES', 'it', 'Si');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_FORM_ERROR_TITLE', 'en', 'An error occured while proceeding with your form.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_FORM_ERROR_TITLE', 'it', 'Si è verificato un errore durante la spedizione del form');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_FORM_VALIDATION_TITLE', 'en', 'In order to proceed, compile the fields correctly');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_FORM_VALIDATION_TITLE', 'it', 'Per procedere compilare correttamente i campi.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ERROR_NOT_REPEATABLE', 'en', 'You have already compiled this form.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_ERROR_NOT_REPEATABLE', 'it', 'Hai già compilato questo form.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_ATTRIBUTE_LISTEMPTY', 'en', 'Empty List');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_ATTRIBUTE_LISTEMPTY', 'it', 'Lista vuota');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_FILEATTRIBUTE_LABEL_FILE', 'en', 'Upload a file');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_FILEATTRIBUTE_LABEL_FILE', 'it', 'Carica file');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_COM_1_1', 'en', '1');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_COM_1_1', 'it', '1');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_COM_1_1', 'en', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_COM_1_1', 'it', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Company', 'en', 'Company');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Company', 'it', 'Company');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Address', 'en', 'Address');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Address', 'it', 'Address');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Email', 'en', 'Email');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Email', 'it', 'Email');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Note', 'en', 'Note');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_1_Note', 'it', 'Note');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_COM_1_2', 'en', '2');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_COM_1_2', 'it', '2');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_BACK_COM_1_2', 'en', 'back');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_BACK_COM_1_2', 'it', 'back');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_COM_1_2', 'en', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_COM_1_2', 'it', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Company', 'en', 'Company');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Company', 'it', 'Company');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Address', 'en', 'Address');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Address', 'it', 'Address');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Email', 'en', 'Email');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Email', 'it', 'Email');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Note', 'en', 'Note');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_COM_1_2_Note', 'it', 'Note');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_PER_1_1', 'en', '1');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_PER_1_1', 'it', '1');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_PER_1_1', 'en', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_PER_1_1', 'it', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_1_Name', 'en', 'Name');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_1_Name', 'it', 'Name');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_1_Surname', 'en', 'Surname');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_1_Surname', 'it', 'Surname');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_1_Birthdate', 'en', 'Birthdate');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_1_Birthdate', 'it', 'Birthdate');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_PER_1_2', 'en', '2');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_TITLE_PER_1_2', 'it', '2');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_BACK_PER_1_2', 'en', 'back');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_BACK_PER_1_2', 'it', 'back');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_PER_1_2', 'en', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_SUBMIT_PER_1_2', 'it', 'submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_2_Name', 'en', 'Name');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_2_Name', 'it', 'Name');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_2_Surname', 'en', 'Surname');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_2_Surname', 'it', 'Surname');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_2_Birthdate', 'en', 'Birthdate');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebform_LABEL_PER_1_2_Birthdate', 'it', 'Birthdate');