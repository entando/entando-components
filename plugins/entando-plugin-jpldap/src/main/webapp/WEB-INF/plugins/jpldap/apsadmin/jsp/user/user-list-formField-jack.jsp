<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="form-group">
    <label for="jpldap_userType" class="control-label col-sm-2 text-right"><s:text name="jpldap.label.userType"/></label>
    <div class="col-sm-5 input-group">
            <wpsf:select id="jpldap_userType" name="userType" 
                         list="#{0: getText('label.all'),1: getText('jpldap.label.entandoUser'),2: getText('jpldap.label.ldapUser')}" cssClass="form-control" />
    </div>
</div>