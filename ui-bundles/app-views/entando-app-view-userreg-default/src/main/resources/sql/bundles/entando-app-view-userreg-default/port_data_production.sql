INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserreg_loginUserReg', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Login Form - With registration management</property>
<property key="it">Form di login - Con gestione registrazione</property>
</properties>', NULL, 'jpuserreg', NULL, NULL, 1);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserreg_Registration', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Registration</property>
<property key="it">Registrazione Utente</property>
</properties>', '<config>
	<parameter name="typeCode">Code of the Profile Type</parameter>
	<action name="userRegConfig"/>
</config>', 'jpuserreg', NULL, NULL, 1 );
INSERT INTO widgetcatalog ( code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ( 'jpuserreg_profile_choice', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Registration - with profile choice</property>
<property key="it">Registrazione Utente - con scelta del profilo</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/listTypes</property>
</properties>', 1 );
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserreg_Activation', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Activation</property>
<property key="it">Attivazione Utente</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/initActivation.action</property>
</properties>', 1);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserreg_Recover', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Recover</property>
<property key="it">Recupero Utenza</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/initRecover.action</property>
</properties>', 1);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserreg_Reactivation', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">User Reactivation</property>
<property key="it">Riattivazione Utente</property>
</properties>', NULL, 'jpuserreg', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpuserreg/UserReg/initReactivation.action</property>
</properties>', 1);
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpuserreg_Suspension', '<?xml version="1.0" encoding="UTF-8"?>
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




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_activate_user', 'jpuserreg_Activation', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>
<h1><@wp.i18n key="jpuserreg_REGISTRATION"/></h1>
<form action="<@wp.action path="/ExtStr2/do/jpuserreg/UserReg/activate.action"/>" method="post" class="form-horizontal">
    <@s.if test="hasFieldErrors()">
        <div class="alert alert-block">
            <h2><@wp.i18n key="ERRORS" /></h2>
            <ul>
                <@s.iterator value="fieldErrors">
                    <@s.iterator value="value">
                        <li><@s.property /></li>
                    </@s.iterator>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <@s.if test="hasActionErrors()">
        <div class="alert alert-block">
            <h2><@wp.i18n key="ERRORS" /></h2>
            <ul>
                <@s.iterator value="actionErrors">
                    <li><@s.property /></li>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <p class="noscreen">
        <@wpsf.hidden name="token" value="%{token}" />
    </p>
    <div class="control-group">
        <label for="password" class="control-label"><@wp.i18n key="jpuserreg_PASSWORD"/> &nbsp;<abbr class="icon icon-asterisk" title="<@wp.i18n key="jpuserreg_REQUIRED" />">
                <span class="noscreen"><@wp.i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
        <div class="controls">
            <@wpsf.password useTabindexAutoIncrement=true name="password" id="password" maxlength="20" />
        </div>
    </div>
    <div class="control-group">
        <label for="passwordConfirm" class="control-label"><@wp.i18n key="jpuserreg_PASSWORD_CONFIRM"/> &nbsp;<abbr class="icon icon-asterisk" title="<@wp.i18n key="jpuserreg_REQUIRED" />">
                <span class="noscreen"><@wp.i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
        <div class="controls">
            <@wpsf.password useTabindexAutoIncrement=true name="passwordConfirm" id="passwordConfirm" maxlength="20" />
        </div>
    </div>
    <p class="form-actions">
        <input type="submit" value="<@wp.i18n key="jpuserreg_SAVE" />" class="btn btn-primary" />
    </p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_confirmed_user_activation', 'jpuserreg_Activation', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpuserreg_REGISTRATION"/></h1>
<p class="alert alert-success">
<@wp.i18n key="jpuserreg_ACTIVATION_CONFIRM_MSG" /><br />
<@wp.i18n key="jpuserreg_ACTIVATION_INFO_MSG" />
</p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_confirmed_user_reactivation', 'jpuserreg_Reactivation', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpuserreg_REGISTRATION" /></h1>
<p class="alert alert-success"><@wp.i18n key="jpuserreg_PASSWORD_RECOVER_SUCCESS_MSG" /></p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_reactivate_user', 'jpuserreg_Reactivation', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>
<h1><@wp.i18n key="jpuserreg_PASSWORD_RECOVERY" /></h1>
<form action="<@wp.action path="/ExtStr2/do/jpuserreg/UserReg/reactivate.action"/>" method="post" class="form-horizontal" >
    <@s.if test="hasFieldErrors()">
        <div class="alert alert-block">
            <h2><@wp.i18n key="ERRORS" /></h2>
            <ul>
                <@s.iterator value="fieldErrors">
                    <@s.iterator value="value">
                        <li><@s.property/></li>
                    </@s.iterator>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <@s.if test="hasActionErrors()">
        <div class="alert alert-block">
            <h2><@wp.i18n key="ERRORS" /></h2>
            <ul>
                <@s.iterator value="actionErrors">
                    <li><@s.property/></li>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <p class="noscreen">
        <@wpsf.hidden name="token" value="%{token}" />
    </p>
    <div class="control-group">
        <label for="password" class="control-label"><@wp.i18n key="jpuserreg_PASSWORD"/>&nbsp;<abbr class="icon icon-asterisk" title="<@wp.i18n key="jpuserreg_REQUIRED" />">
                <span class="noscreen"><@wp.i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
        <div class="controls">
            <@wpsf.password useTabindexAutoIncrement=true name="password" id="password" maxlength="20" />
        </div>
    </div>
    <div class="control-group">
        <label for="passwordConfirm" class="control-label"><@wp.i18n key="jpuserreg_PASSWORD_CONFIRM"/>&nbsp;<abbr class="icon icon-asterisk" title="<@wp.i18n key="jpuserreg_REQUIRED" />">
                <span class="noscreen"><@wp.i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
        <div class="controls">
            <@wpsf.password useTabindexAutoIncrement=true name="passwordConfirm" id="passwordConfirm" maxlength="20" />
        </div>
    </div>
    <p class="form-actions">
        <input type="submit" value="<@wp.i18n key="jpuserreg_SAVE" />" cssClass="btn btn-primary" />
    </p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_profile_choose', 'jpuserreg_profile_choice', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>
<h1><@wp.i18n key="jpuserreg_REGISTRATION" /></h1>
<form action="<@wp.action path="/ExtStr2/do/jpuserreg/UserReg/initRegistration"/>" method="post" class="form-horizontal" >
      <div class="control-group">
        <label for="profileTypeCode" class="control-label"><@wp.i18n key="jpuserreg_PROFILETYPE" />:</label>
        <div class="controls">
            <select name="profileTypeCode" tabindex="<@wpsa.counter />" id="profileTypeCode" class="text">
                <@s.iterator value="profileTypes" var="profileType" >
                    <@s.set name="optionDescr">jpuserreg_TITLE_<@s.property value="#profileType.typeCode"/></@s.set>
                    <option value="<@s.property value="#profileType.typeCode"/>"><@wp.i18n key="${optionDescr}" /></option>
                </@s.iterator>
            </select>
        </div>
    </div>
    <p class="form-actions">
    <@s.set name="labelChoose"><@wp.i18n key="jpuserreg_CHOOSE_TYPE" /></@s.set>
    <@wpsf.submit useTabindexAutoIncrement=true value="%{#labelChoose}" cssClass="btn btn-primary"/>
    </p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_confirm_suspend', 'jpuserreg_Suspension', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>
<h1><@wp.i18n key="jpuserreg_SUSPENDING_CONFIRM_MSG"/></h1>
<p><@wp.i18n key="jpuserreg_SUSPENDING_CONFIRM_INTRO"/>You will be logged out and redirect to home page</p>
<@s.if test="hasFieldErrors()">
    <div class="alert alert-block">
        <h2><@wp.i18n key="ERRORS" /></h2>
        <ul>
            <@s.iterator value="fieldErrors">
                <@s.iterator value="value">
                    <@s.set name="label" ><@s.property/></@s.set>
                    <li><@s.property /></li>
                </@s.iterator>
            </@s.iterator>
        </ul>
    </div>
</@s.if>
<@s.if test="hasActionErrors()">
    <div class="alert alert-block">
        <h2><@wp.i18n key="ERRORS" /></h2>
        <ul>
            <@s.iterator value="actionErrors">
                <@s.set name="label" ><@s.property/></@s.set>
                <li><@s.property /></li>
            </@s.iterator>
        </ul>
    </div>
</@s.if>
<form method="post" action="<@wp.action path="/ExtStr2/do/jpuserreg/UserReg/suspend.action" />"  class="form-horizontal" >
    <div class="control-group">
        <label for="password" class="control-label"><@wp.i18n key="jpuserreg_PASSWORD"/>&nbsp;<abbr class="icon icon-asterisk" title="<@wp.i18n key="jpuserreg_REQUIRED" />"><span class="noscreen"><@wp.i18n key="jpuserreg_REQUIRED" /></span></abbr></label>
        <div class="controls">
            <@wpsf.password useTabindexAutoIncrement="true" name="password" required="true" id="password" />
        </div>
    </div>
    <p class="form-actions">
        <input type="submit" value="<@wp.i18n key="jpuserreg_SUSPEND"/>" cssClass="btn btn-primary"/>
    </p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_recover_user', 'jpuserreg_Recover', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>
<h1><@wp.i18n key="jpuserreg_REACTIVATION_PASSWORD_LOST_MSG"/></h1>
<form method="post" action="<@wp.action path="/ExtStr2/do/jpuserreg/UserReg/recoverFromUsername.action" />" class="form-horizontal" >
      <@s.if test="hasFieldErrors()">
        <div class="alert alert-block">
            <h2><@wp.i18n key="ERRORS" /></h2>
            <ul>
                <@s.iterator value="fieldErrors">
                    <@s.iterator value="value">
                        <@s.set name="label"><@s.property /></@s.set>
                        <li><@s.property /></li>
                    </@s.iterator>
                </@s.iterator>
            </ul>
        </div>
    </@s.if> 
    <@s.if test="hasActionErrors()">
        <div class="alert alert-block">
            <h2><@wp.i18n key="ERRORS" /></h2>
            <ul>
                <@s.iterator value="actionErrors">
                    <@s.set name="label"><@s.property /></@s.set>
                    <li><@s.property /></li>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <div class="control-group">
        <label for="username" class="control-label"><@wp.i18n key="jpuserreg_USERNAME"/></label>
        <div class="controls">
            <@wpsf.textfield useTabindexAutoIncrement=true name="username" id="username" />
        </div>
    </div>
    <p class="form-actions">
        <input type="submit" value="<@wp.i18n key="jpuserreg_SEND"/>" class="btn btn-primary" />
    </p>
</form>
<h1><@wp.i18n key="jpuserreg_REACTIVATION_USERNAME_LOST_MSG"/></h1>
<form method="post" action="<@wp.action path="/ExtStr2/do/jpuserreg/UserReg/recoverFromEmail.action" />" class="form-horizontal" >
    <div class="control-group">
        <label for="email" class="control-label"><@wp.i18n key="jpuserreg_EMAIL"/></label>
        <div class="controls">
            <@wpsf.textfield useTabindexAutoIncrement=true name="email" id="email" />
        </div>
    </div>
    <p class="form-actions">	
        <input type="submit" value="<@wp.i18n key="jpuserreg_SEND" />" class="btn btn-primary" />
    </p>
</form>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_confirmed_recover_request', 'jpuserreg_Recover', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpuserreg_REACTIVATION_PASSWORD_LOST_MSG" /></h1>
<p class="alert alert-success"><@wp.i18n key="jpuserreg_RECOVER_REQUEST_MSG" /></p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_activate_user_error', 'jpuserreg_Activation', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpuserreg_REGISTRATION"/></h1>
<div class="alert alert-block"><@wp.i18n key="jpuserreg_ACTIVATION_ERROR_MSG" /></div>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_reactivate_user_error', 'jpuserreg_Reactivation', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpuserreg_PASSWORD_RECOVERY" /></h1>
<div class="alert alert-block"><@wp.i18n key="jpuserreg_REACTIVATION_ERROR_MSG" /></div>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_loginUserReg', 'jpuserreg_loginUserReg', 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<@wp.fragment code="login_form" escapeXml=false />
<#if (Session.currentUser.username == "guest")>
    <@wp.pageWithWidget widgetTypeCode="jpuserreg_Registration" var="jpuserregRegistrationPageVar" listResult=false />
    <#if (jpuserregRegistrationPageVar??) >
    <p><a href="<@wp.url page="${jpuserregRegistrationPageVar.code}" />" ><@wp.i18n key="jpuserreg_REGISTRATION" /></a></p>
    </#if>
    <@wp.pageWithWidget widgetTypeCode="jpuserreg_Recover" var="jpuserregRecoverPageVar" listResult=false />
    <#if (jpuserregRecoverPageVar??) >
    <p><a href="<@wp.url page="${jpuserregRecoverPageVar.code}" />" ><@wp.i18n key="jpuserreg_PASSWORD_RECOVER" /></a></p>
    </#if>
</#if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_confirmed_user_registration', NULL, 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpuserreg_REGISTRATION" /></h1>
<p class="alert alert-success">
    <@wp.i18n key="jpuserreg_REGISTRATION_CONFIRM_MSG" escapeXml=false />
</p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpuserreg_is_error', NULL, 'jpuserreg', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpuserreg_REGISTRATION"/></h1>
<div class="alert alert-block">
<@wp.i18n key="jpuserreg_ERROR_USER_ADMIN_OR_NO_ENTANDO_USER"/>
</div>', 1);
