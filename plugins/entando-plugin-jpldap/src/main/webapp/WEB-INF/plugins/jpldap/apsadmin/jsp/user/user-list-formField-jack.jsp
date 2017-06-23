<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<label for="jpldap_userType" class="control-label col-sm-3 ">
    <s:text name="jpldap.label.userType"/>
</label>
<div class="col-sm-8">
    <wpsf:select id="jpldap_userType" cssClass="form-control"  name="userType" 
                 list="#{0: getText('label.all'),1: getText('jpldap.label.entandoUser'),2: getText('jpldap.label.ldapUser')}" />
</div>
