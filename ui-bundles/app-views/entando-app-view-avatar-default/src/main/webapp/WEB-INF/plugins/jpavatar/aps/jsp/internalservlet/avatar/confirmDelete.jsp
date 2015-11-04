<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1><wp:i18n key="jpavatar_TITLE" /></h1>
<div class="media">
    <span class="pull-left">
        <jpavatar:avatar var="currentAvatar" returnDefaultAvatar="true"  />
        <img class="media-object img-polaroid" src="<s:url action="avatarStream" namespace="/do/currentuser/avatar"><s:param name="gravatarSize">34</s:param></s:url>" />
    </span>
    <form class="media-body" action="<wp:action path="/ExtStr2/do/jpavatar/Front/Avatar/delete.action" />" method="post">
        <p>
            <wp:i18n key="jpavatar_CONFIRM_DELETE" />&#32;
            <s:submit cssClass="btn btn-danger" type="button">
                <wp:i18n key="jpavatar_DELETE" />
            </s:submit>
        </p>
    </form>
</div>
