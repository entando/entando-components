INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked )
	VALUES ( 'jpwebdynamicform_message_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Dynamic Web Forms - Publish the form for a Message Type</property>
<property key="it">Dynamic Web Forms - Pubblica il form di un tipo di Messaggio</property>
</properties>', '<config>
	<parameter name="typeCode">Code of the Message Type</parameter>
	<parameter name="formProtectionType">Protection type of the form</parameter>
	<action name="webdynamicformConfig"/>
</config>', 'jpwebdynamicform', NULL, NULL, 1 );


INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked )
	VALUES ( 'jpwebdynamicform_message_choice', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Dynamic Web Forms - Choice of a type of Message</property>
<property key="it">Dynamic Web Forms - Scelta tipo di Messaggio</property>
</properties>', NULL, 'jpwebdynamicform', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpwebdynamicform/Message/User/listTypes</property>
</properties>', 1 );



INSERT INTO sysconfig ( version, item, descr, config ) VALUES ( 'production', 'jpwebdynamicform_messageTypes', 'Definizione dei Tipi di Messaggio', '<messagetypes>
    <messagetype typecode="PER" typedescr="Person Form" >
        <attributes>
            <attribute name="Name" attributetype="Monotext" searcheable="true" required="true" />
            <attribute name="Surname" attributetype="Monotext" searcheable="true" required="true" />
            <attribute name="Address" attributetype="Monotext" searcheable="false" required="false" />
            <attribute name="eMail" attributetype="Monotext" searcheable="false" required="true" >
                <regexp><![CDATA[(.*<.+@.+\.[a-z]+>)|(.+@.+\.[a-z]+)]]></regexp>
            </attribute>
            <attribute name="Note" attributetype="Monotext" searcheable="false" required="true" />
            <attribute name="Date" attributetype="Date" searcheable="false" required="true" />
            <attribute name="Number" attributetype="Number" searcheable="false" required="true" />
            <attribute name="Enumerator" attributetype="Enumerator" searcheable="false" required="true" ><![CDATA[Opzione A,Opzione B,Opzione C,Opzione D,Opzione E,Opzione F,Opzione G]]></attribute>
            <attribute name="Text" attributetype="Text" searcheable="false" required="true" />
            <attribute name="Longtext" attributetype="Longtext" searcheable="false" required="true" />
            <attribute name="Boolean" attributetype="Boolean" searcheable="false" required="true" />
            <attribute name="CheckBox" attributetype="CheckBox" searcheable="false" required="true" />
            <attribute name="ThreeState" attributetype="ThreeState" searcheable="false" required="true" />
        </attributes>
    </messagetype>
    <messagetype typecode="COM" typedescr="Company Form" >
        <attributes>
            <attribute name="Company" attributetype="Monotext" searcheable="true" required="true" />
            <attribute name="Address" attributetype="Monotext" searcheable="false" required="false" />
            <attribute name="eMail" attributetype="Monotext" searcheable="false" required="true" >
                <regexp><![CDATA[(.*<.+@.+\.[a-z]+>)|(.+@.+\.[a-z]+)]]></regexp>
            </attribute>
            <attribute name="Note" attributetype="Monotext" searcheable="false" required="true" />
        </attributes>
    </messagetype>
</messagetypes>' );

INSERT INTO sysconfig ( version, item, descr, config ) VALUES ( 'production', 'jpwebdynamicform_messageNotifierConfig', 'Configurazione del servizio di notifica Messaggi', '<messagetypes>
    <messagetype typeCode="PER" senderCode="CODE1" mailAttrName="eMail" >
        <recipients>
            <to><![CDATA[Address 1 <address@notexistant1.itte>]]></to>
            <to><![CDATA[Address 2 <address@notexistant2.itte>]]></to>
            <cc><![CDATA[Address 3 <address@notexistant3.itte>]]></cc>
            <bcc><![CDATA[Address 4 <address@notexistant4.itte>]]></bcc>
        </recipients>
        <model>
            <!-- CORPO DELLA MAIL -->
            <body><![CDATA[Corpo della Mail come template di Velocity]]></body>

            <!-- OGGETTO DELLA MAIL -->
            <subject><![CDATA[Oggetto della mail come template di Velocity]]></subject>
        </model>
    </messagetype>
    <messagetype typeCode="COM" senderCode="CODE2" mailAttrName="eMail" store="false" >
        <recipients>
            <to><![CDATA[Address 1 <address@notexistant1.itte>]]></to>
            <to><![CDATA[Address 2 <address@notexistant2.itte>]]></to>
            <to><![CDATA[Address 3 <address@notexistant3.itte>]]></to>
            <cc><![CDATA[Address 4 <address@notexistant4.itte>]]></cc>
            <cc><![CDATA[Address 5 <address@notexistant5.itte>]]></cc>
            <bcc><![CDATA[Address 6 <address@notexistant6.itte>]]></bcc>
        </recipients>
        <model>
            <!-- CORPO DELLA MAIL -->
            <body><![CDATA[Corpo della mail come template di Velocity]]></body>

            <!-- OGGETTO DELLA MAIL -->
            <subject><![CDATA[Oggetto della mail come template di Velocity]]></subject>
        </model>
    </messagetype>
</messagetypes>' );


INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INFO', 'en', 'The fields marked with * are required.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INFO', 'it', 'I campi contrassegnati dal simbolo * sono obbligatori.');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_FULL', 'en', 'Mandatory');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_FULL', 'it', 'Obbligatorio');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_SHORT', 'en', '*');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MANDATORY_SHORT', 'it', '*');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL', 'en', 'Max Length');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_FULL', 'it', 'Lunghezza massima');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT', 'en', 'max');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MAXLENGTH_SHORT', 'it', 'max');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_FULL', 'en', 'Min Length');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_FULL', 'it', 'Lunghezza minima');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT', 'en', 'min');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ENTITY_ATTR_FLAG_MINLENGTH_SHORT', 'it', 'min');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INVIA', 'en', 'Send');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_INVIA', 'it', 'Invia');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_SAVE_CONFIRMATION', 'it', 'Il messaggio è stato inviato correttamente. Se vuoi inviaci un''altra');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_SAVE_CONFIRMATION', 'en', 'The message has been sent successfully. If you need, send us another');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_REQUEST_LINK', 'en', 'request');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGE_REQUEST_LINK', 'it', 'richiesta');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_SELECT', 'en', 'Select');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_SELECT', 'it', 'Seleziona');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGETYPE', 'it', 'Scegliere un tipo di messaggio');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_MESSAGETYPE', 'en', 'Choose a message type');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_CHOOSE_TYPE', 'it', 'Scegli e continua');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_CHOOSE_TYPE', 'en', 'Select and continue');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS', 'it', 'Errori durante la compilazione del modulo');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS', 'en', 'Errors occured');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_YES', 'it', 'Si');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_YES', 'en', 'Yes');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_NO', 'it', 'No');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_NO', 'en', 'No');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_BOTH_YES_AND_NO', 'it', 'Indifferente');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_BOTH_YES_AND_NO', 'en', 'Both');

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS_HAPPENED', 'it', 'Si sono verificati degli errori!');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpwebdynamicform_ERRORS_HAPPENED', 'en', 'An error has happened!');
