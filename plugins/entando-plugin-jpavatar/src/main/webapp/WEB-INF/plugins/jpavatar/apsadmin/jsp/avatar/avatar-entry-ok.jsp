<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.avatarManagement" />
    </span>
</h1>
<div id="main">
    <div class="alert alert-success">
        <s:text name="jpavatar.label.saveOK" />&#32;
        <a class="alert-link" href="<s:url action="edit" />"><s:text name="label.back.to.management" /></a>
    </div>
</div>