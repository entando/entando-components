INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-login_form_inspinia', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Dropdown Sign In</property>
<property key="it">Dropdown Sign In</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-login_form_inspinia', 'entando-widget-login_form_inspinia', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<li class=" dropdown
    <#if (accountExpired?? && accountExpired == true) || (wrongAccountCredential?? && wrongAccountCredential == true)>open</#if> ">
    <#if (Session.currentUser != "guest")>
  
    <a class="btn  text-left dropdown-toggle" href="#" data-toggle="dropdown">
        ${Session.currentUser}
        <span class="caret"></span>
    </a>
    <ul class="dropdown-menu">
        <li>
            <@wp.ifauthorized permission="enterBackend">
            <a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<@wp.info key="currentLang" />">
               <i class="fa fa-cube"></i>      
                <@wp.i18n key="ESLF_ADMINISTRATION" />
            </a>
            </@wp.ifauthorized>
        </li>
        <div class="divider"></div>
        <li> 
            <a class="btn" href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/logout.action">
               <i class="fa fa-sign-out"></i>           
                <@wp.i18n key="ESLF_SIGNOUT" />
            </a>
        </li>
        <@wp.pageWithWidget var="editProfilePageVar" widgetTypeCode="userprofile_editCurrentUser" />
        <#if (editProfilePageVar??) >
        <li>
            <a href="<@wp.url page="${editProfilePageVar.code}" />" ><@wp.i18n key="ESLF_PROFILE_CONFIGURATION" /></a>
        </li>
        </#if>
    </ul>
    <#else>
 
    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
        <@wp.i18n key="ESLF_SIGNIN" />
        <span class="caret"></span>
    </a>
    <ul class="dropdown-menu">
        <li>
            <form class="m-t" style="padding:10px;" method="POST">
                <#if (accountExpired?? && accountExpired == true)>
                <div class="alert alert-error">
                    <button class="close" data-dismiss="alert">x</button>
                    <@wp.i18n key="ESLF_USER_STATUS_EXPIRED" />
                </div>
                </#if>
                <#if (wrongAccountCredential?? && wrongAccountCredential == true)>
                <div class="alert alert-error">
                    <button class="close" data-dismiss="alert">x</button>
                    <@wp.i18n key="ESLF_USER_STATUS_CREDENTIALS_INVALID" />
                </div>
                </#if>
                <div class="form-group">
                    <input type="text" name="username" class="form-control" placeholder="<@wp.i18n key="ESLF_USERNAME" />">
                </div>
                <div class="form-group">
                    <input type="password" name="password" class="form-control"  placeholder="<@wp.i18n key="ESLF_PASSWORD" />">
                </div>
                <input type="submit" class="btn btn-primary block full-width m-b" value="<@wp.i18n key="ESLF_SIGNIN" />" />
            </form>
        </li>
    </ul>
    </#if>
</li>', 1);

INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_WELCOME', 'en', 'Hello');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_WELCOME', 'it', 'Ciao');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_ADMINISTRATION', 'en', 'Administration');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_ADMINISTRATION', 'it', 'Amministrazione');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_SIGNOUT', 'en', 'Sign out');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_SIGNOUT', 'it', 'Esci');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_SIGNIN', 'en', 'Sign in');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_SIGNIN', 'it', 'Accedi');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_USER_STATUS_EXPIRED', 'en', 'Your account has expired!');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_USER_STATUS_EXPIRED', 'it', 'Credenziali scadute!');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_USER_STATUS_CREDENTIALS_INVALID', 'en', 'Wrong username or password!');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_USER_STATUS_CREDENTIALS_INVALID', 'it', 'Le credenziali non sono corrette!');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_USERNAME', 'en', 'Username');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_USERNAME', 'it', 'Utente');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_PASSWORD', 'en', 'Password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_PASSWORD', 'it', 'Password');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_PROFILE_CONFIGURATION', 'en', 'Edit profile');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('ESLF_PROFILE_CONFIGURATION', 'it', 'Modifica profilo');
