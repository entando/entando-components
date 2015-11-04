INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jpavatar_avatar', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Avatar - Show the current user avatar</property>
<property key="it">Avatar - Mostra l''avatar dell''utente corrente</property>
</properties>', NULL, 'jpavatar', 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/jpavatar/Front/Avatar/edit.action</property>
</properties>
', 1);




INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_YOU_HAVE_NO_AVATAR', 'en', 'You don''t have an avatar');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_YOU_HAVE_NO_AVATAR', 'it', 'Non hai nessun avatar');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_CURRENT_AVATAR', 'it', 'Avatar corrente');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_CURRENT_AVATAR', 'en', 'Current Avatar');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_CONFIRM_DELETE', 'en', 'Do You really want to remove your avatar?');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_CONFIRM_DELETE', 'it', 'Vuoi davvero eliminare l''avatar?');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_AVATAR_SAVED', 'en', 'Avatar successfully saved');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_AVATAR_SAVED', 'it', 'Avatar salvato correttamente');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_TITLE', 'it', 'Avatar');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_TITLE', 'en', 'Avatar');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_DELETE', 'it', 'Rimuovi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_DELETE', 'en', 'Remove');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_UPLOAD', 'it', 'Carica un''immagine');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_UPLOAD', 'en', 'Upload an image');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_GO_UPLOAD', 'it', 'Carica');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_GO_UPLOAD', 'en', 'Upload');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_OK_THANKYOU', 'it', 'Ok');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_OK_THANKYOU', 'en', 'Ok');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_PLEASE_LOGIN','it','E'' necessario effettuare l''accesso');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpavatar_PLEASE_LOGIN','en','Please login');




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpavatar_is_entryAvatar', 'jpavatar_avatar', 'jpavatar', NULL, '<#assign jpavatar=JspTaglibs["/jpavatar-apsadmin-core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>

<@s.set var="avatarResourceVar" value="avatarResource" scope="page" />
<h1><@wp.i18n key="jpavatar_TITLE" /></h1>
<#if (Session.currentUser != "guest") >
    <@s.if test="hasActionErrors()">
        <div class="alert alert-error">
            <h2><@s.text name="message.title.ActionErrors" /></h2>
            <ul class="unstyled">
                <@s.iterator value="actionErrors">
                    <li><@s.property escape=false /></li>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <@s.if test="hasFieldErrors()">
        <div class="alert alert-error">
            <h2><@s.text name="message.title.FieldErrors" /></h2>
            <ul class="unstyled">
                <@s.iterator value="fieldErrors">
                    <@s.iterator value="value">
                        <li><@s.property escape=false /></li>
                    </@s.iterator>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <@s.if test="hasActionMessages()">
        <div class="alert alert-warning">
            <h2><@s.text name="messages.confirm" /></h2>
            <ul class="unstyled">
                <@s.iterator value="actionMessages">
                    <li><@s.property escape=false /></li>
                </@s.iterator>
            </ul>
        </div>
    </@s.if>
    <@jpavatar.avatar var="currentAvatar" returnDefaultAvatar="true" avatarStyleVar="styleVar" />
    <div class="media">
        <span class="pull-left">
            <img class="media-object img-polaroid" src="<@s.url action="avatarStream" namespace="/do/currentuser/avatar"><@s.param name="gravatarSize">34</@s.param></@s.url>" />
        </span>
        <div class="media-body">
            <#if (styleVar?? && styleVar != ''local'') >
                <p class="media-heading"><@wp.i18n key="jpavatar_CURRENT_AVATAR" /></p>
            </#if>
            <#if (styleVar?? && styleVar == ''local'') >
                <#if (avatarResourceVar??) >
                    <p class="media-heading"><@wp.i18n key="jpavatar_CURRENT_AVATAR" /></p>
                    <form action="<@wp.action path="/ExtStr2/do/jpavatar/Front/Avatar/bin.action" />" method="post">
                        <p>
                            <@s.submit cssClass="btn btn-warning" type="button">
                                <@wp.i18n key="jpavatar_DELETE" />
                            </@s.submit>
                        </p>
                    </form>
                <#else>
                    <p class="media-heading">
                        <span class="text-warning"><@wp.i18n key="jpavatar_YOU_HAVE_NO_AVATAR" /></span>
                    </p>
                    <form action="<@wp.action path="/ExtStr2/do/jpavatar/Front/Avatar/save.action" />" method="post" enctype="multipart/form-data">
                        <p>
                            <label for="jpavatar_upload"><@wp.i18n key="jpavatar_UPLOAD" /></label>
                            <@s.file name="avatar" id="jpavatar_upload" />
                        </p>
                        <p>
                            <@s.submit type="button" cssClass="btn btn-primary">
                                <@wp.i18n key="jpavatar_GO_UPLOAD" />
                            </@s.submit>
                        </p>
                    </form>
                </#if>
            </#if>
        </div>
    </div>
<#else>
    <p><@wp.i18n key="jpavatar_PLEASE_LOGIN" /></p>
</#if>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jpavatar_is_confirmDelete', 'jpavatar_avatar', 'jpavatar', NULL, '<#assign jpavatar=JspTaglibs["/jpavatar-apsadmin-core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<h1><@wp.i18n key="jpavatar_TITLE" /></h1>
<div class="media">
    <span class="pull-left">
        <@jpavatar.avatar var="currentAvatar" returnDefaultAvatar="true" />
        <img class="media-object img-polaroid" src="<@s.url action="avatarStream" namespace="/do/currentuser/avatar"><@s.param name="gravatarSize">34</@s.param></@s.url>" />
    </span>
    <form class="media-body" action="<@wp.action path="/ExtStr2/do/jpavatar/Front/Avatar/delete.action" />" method="post">
        <p>
            <@wp.i18n key="jpavatar_CONFIRM_DELETE" />&#32;
            <@s.submit cssClass="btn btn-danger" type="button">
                <@wp.i18n key="jpavatar_DELETE" />
            </@s.submit>
        </p>
    </form>
</div>', 1);
