<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<fieldset>
	<legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
	<div class="accordion_element">
		<p><label for="jpldap_userType" class="basic-mint-label"><s:text name="jpldap.label.userType"/>:</label>
		<wpsf:select useTabindexAutoIncrement="true" id="jpldap_userType" name="userType" list="#{
			0: getText('label.all'),
			1: getText('jpldap.label.entandoUser'),
			2: getText('jpldap.label.ldapUser')
		}" />
		</p>
	</div>
</fieldset>