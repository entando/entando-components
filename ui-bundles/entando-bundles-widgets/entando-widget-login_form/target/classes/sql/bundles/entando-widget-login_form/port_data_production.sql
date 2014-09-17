INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-login_form', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Dropdown Sign In</property>
<property key="it">Dropdown Sign In</property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-login_form', 'entando-widget-login_form', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<@wp.headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />
<@wp.headInfo type="JS" info="entando-misc-bootstrap/bootstrap.min.js" />

<ul class="nav pull-right">
	<li class="span2 dropdown
<#if (accountExpired?? && accountExpired == true) || (wrongAccountCredential?? && wrongAccountCredential == true)>open</#if>
">

	<#if (Session.currentUser != "guest")>
			<div class="btn-group">
				<button class="btn span2 text-left dropdown-toggle" data-toggle="dropdown">
					<@c.out value="${Session.currentUser}" />
					<span class="caret pull-right"></span>
				</button>
				<ul class="dropdown-menu pull-right well-small">
					<li class="padding-medium-vertical">

						<@wp.ifauthorized permission="enterBackend">
						<p>
							<a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<@wp.info key="currentLang" />"><span class="icon-wrench"></span> <@wp.i18n key="ESLF_ADMINISTRATION" /></a>
						</p>
						</@wp.ifauthorized>
						<div class="divider"></div>
						<p class="help-block text-right">
							<a class="btn" href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/logout.action"><@wp.i18n key="ESLF_SIGNOUT" /></a>
						</p>
						<@wp.pageWithWidget var="editProfilePageVar" widgetTypeCode="userprofile_editCurrentUser" />
						<#if (editProfilePageVar??) >
						<p class="help-block text-right">
							<a href="<@wp.url page="${editProfilePageVar.code}" />" ><@wp.i18n key="ESLF_PROFILE_CONFIGURATION" /></a>
						</p>
						</#if>
					</li>
				</ul>
			</div>	
		<#else>
			<a class="dropdown-toggle text-right" data-toggle="dropdown" href="#"><@wp.i18n key="ESLF_SIGNIN" /> <span class="caret"></span></a>
			<ul class="dropdown-menu well-small">
				<li>
					<form class="form-vertical" method="POST">
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
						<input type="text" name="username" class="input-large" placeholder="<@wp.i18n key="ESLF_USERNAME" />">
						<input type="password" name="password" class="input-large" placeholder="<@wp.i18n key="ESLF_PASSWORD" />">
						<p class="text-right">
							<input type="submit" class="btn btn-primary" value="<@wp.i18n key="ESLF_SIGNIN" />" />
						</p>
					</form>
				</li>
			</ul>
		</#if>
	</li>
</ul>', 1);

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