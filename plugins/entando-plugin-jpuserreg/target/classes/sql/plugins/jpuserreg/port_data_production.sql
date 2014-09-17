INSERT INTO sysconfig (version, item, descr, config) VALUES ('production', 'jpuserreg_Config', 
'Configuration for the plugin User Registration',
'<userRegConfig>
	<tokenValidity minutes="60"/>
	<sender code="CODE1" />
	<userAuthDefaults />
	<activation pageCode="activation">
		<template lang="it">
			<subject><![CDATA[Il tuo account è stato creato]]></subject>
			<body><![CDATA[
Gentile {name} {surname},
grazie per esserti registrato.
Per attivare il tuo account è necessario seguire il seguente link: 
{link}
Cordiali Saluti.
			]]></body>
		</template>
		<template lang="en">
			<subject><![CDATA[Your account has been created]]></subject>
			<body><![CDATA[
Dear {name} {surname}, 
thank you for registering.
To activate your account you need to click on the link: 
{link}
Best regards.
			]]></body>
		</template>
	</activation>
	<reactivation pageCode="reactivation">
		<template lang="it">
			<subject><![CDATA[[Entando]: Ripristino password]]></subject>
			<body><![CDATA[
Gentile {name} {surname}, 
il tuo userName è {userName}.
Per riattivare il tuo account è necessario seguire il seguente link: 
{link}
Cordiali Saluti.
			]]></body>
		</template>
		<template lang="en">
			<subject><![CDATA[[Entando]: Password recover]]></subject>
			<body><![CDATA[
Dear {name} {surname}, 
your username is {userName}.
To recover your password you need to click on the link: 
{link}
Best regards.
			]]></body>
		</template>
	</reactivation>
</userRegConfig>');
   
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpuserreg_loginUserReg', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Login Form - With registration management</property>
<property key="it">Form di login - Con gestione registrazione</property>
</properties>', NULL, 'jpuserreg', NULL, NULL, 1);
    
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpuserreg_Registration', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Registration</property>
<property key="it">Registrazione Utente</property>
</properties>', '<config>
	<parameter name="typeCode">Code of the Profile Type</parameter>
	<action name="userRegConfig"/>
</config>', 'jpuserreg', NULL, NULL, 1 );

INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked )
	VALUES ( 'jpuserreg_profile_choice', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Registration - with profile choice</property>
<property key="it">Registrazione Utente - con scelta del profilo</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/listTypes</property>
</properties>', 1 );

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpuserreg_Activation', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Activation</property>
<property key="it">Attivazione Utente</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/initActivation.action</property>
</properties>', 1);
   
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpuserreg_Recover', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Recover</property>
<property key="it">Recupero Utenza</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/initRecover.action</property>
</properties>', 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpuserreg_Reactivation', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Reactivation</property>
<property key="it">Riattivazione Utente</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/initReactivation.action</property>
</properties>', 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) 
VALUES ('jpuserreg_Suspension', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Suspension</property>
<property key="it">Sospensione Utente</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/initSuspension.action</property>
</properties>', 1);

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PROFILETYPE', 'it', 'Tipo di Profilo' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PROFILETYPE', 'en', 'Profile Type' );

INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpuserreg_CHOOSE_TYPE', 'it', 'Scegli e continua');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpuserreg_CHOOSE_TYPE', 'en', 'Select and continue');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SAVE', 'it', 'Salva' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SAVE', 'en', 'Save' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SEND', 'it', 'Invia' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SEND', 'en', 'Send' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SUSPEND', 'it', 'Sospendi' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SUSPEND', 'en', 'Suspend' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REQUIRED', 'it', 'Obbligatorio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REQUIRED', 'en', 'Required' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REQUIRED_FIELD', 'it', 'Campo obbligatorio' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REQUIRED_FIELD', 'en', 'Required field' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REGISTRATION', 'it', 'Registrazione Utente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REGISTRATION', 'en', 'User Registration' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REGISTRATION_CONFIRM_MSG', 'it', 'Registrazione effettuata correttamente.<br/>A breve riceverà una eMail contenente un link di conferma.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REGISTRATION_CONFIRM_MSG', 'en', 'User registration successful.<br/>Shortly you''ll receive an eMail with a confirmation link.' ); 

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION', 'it', 'Attivazione Utente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION', 'en', 'User Activation' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION_CONFIRM_MSG', 'it', 'Attivazione effettuata correttamente.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION_CONFIRM_MSG', 'en', 'User activation successful.' ); 

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION_INFO_MSG', 'it', 'Eseguite il login e aggiornate il vostro profilo.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION_INFO_MSG', 'en', 'Execute the log-in and update your profile.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_RECOVERY', 'it', 'Recupero Password' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_RECOVERY', 'en', 'Password Recovery' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_RECOVER_SUCCESS_MSG', 'it', 'Utenza recuperata correttamente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_RECOVER_SUCCESS_MSG', 'en', 'Password recovering successful' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_RECOVER_REQUEST_MSG', 'it', 'Recupero Utenza' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_RECOVER_REQUEST_MSG', 'en', 'User Recovery' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REACTIVATION_PASSWORD_LOST_MSG', 'it', 'Recupero Utenza da nome utente' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REACTIVATION_PASSWORD_LOST_MSG', 'en', 'User Recovery from username' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REACTIVATION_USERNAME_LOST_MSG', 'it', 'Recupero Utenza da indirizzo eMail' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REACTIVATION_USERNAME_LOST_MSG', 'en', 'User Recovery from eMail address' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION_ERROR_MSG', 'it', 'Si è verificato un errore eseguendo l''attivazione dell''utenza. Probabilmente si stà eseguendo l''operazione con dati già utilizzati o fuori tempo massimo' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACTIVATION_ERROR_MSG', 'en', 'An error occurred while activating the user. Probably your request is too old and expired.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REACTIVATION_ERROR_MSG', 'it', 'Si è verificato un errore eseguendo la riattivazione dell''utenza. Probabilmente si stà eseguendo l''operazione con dati già utilizzati o fuori tempo massimo' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REACTIVATION_ERROR_MSG', 'en', 'An error occurred while reactivating your account. Probably your request is too old and expired.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SUSPENDING_CONFIRM_MSG', 'it', 'Sospensione Utenza' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SUSPENDING_CONFIRM_MSG', 'en', 'Account Suspension' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_USERNAME', 'it', 'Username' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_USERNAME', 'en', 'Username' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_NAME', 'it', 'Nome' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_NAME', 'en', 'Name' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SURNAME', 'it', 'Cognome' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_SURNAME', 'en', 'Surname' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_EMAIL', 'it', 'eMail' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_EMAIL', 'en', 'eMail' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_EMAIL_CONFIRM', 'it', 'eMail Confirm' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_EMAIL_CONFIRM', 'en', 'eMail confirm' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_LANG', 'it', 'Lingua' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_LANG', 'en', 'Language' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD', 'it', 'Password' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD', 'en', 'Password' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_CONFIRM', 'it', 'Conferma Password' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_CONFIRM', 'en', 'Password Confirmation' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PRIVACY_AGREEMENT', 'it', 'Consenso privacy' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PRIVACY_AGREEMENT', 'en', 'Privacy agreement' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ERROR_USER_LOGGED', 'it', 'Utente attualmente loggato, non è possibile utilizzare la funzionalità di recupero password. E'' necessario sloggarsi.' );
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ERROR_USER_LOGGED', 'en', 'User already logged, you can''t use recover password functionality. You must log out.' );

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACCOUNT_SUSPENSION', 'it', 'Sospensione Account');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ACCOUNT_SUSPENSION', 'en', 'Account Suspension');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REGISTRATION_LINK', 'it', 'Registrazione');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_REGISTRATION_LINK', 'en', 'Registration');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_RECOVER', 'it', 'Recupero Password');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PASSWORD_RECOVER', 'en', 'Password Recover');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ERROR_USER_ADMIN_OR_NO_ENTANDO_USER', 'it', 'Gli amministratori e gli utenti non locali di Entando non possono sospendere il proprio account.');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_ERROR_USER_ADMIN_OR_NO_ENTANDO_USER', 'en', 'Administrators and the users that aren''t Entando local users can''t suspend their account.');

INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PROFILE_CONFIGURATION', 'it', 'Il tuo profilo');
INSERT INTO localstrings ( keycode, langcode, stringvalue ) VALUES ( 'jpuserreg_PROFILE_CONFIGURATION', 'en', 'Your profile');