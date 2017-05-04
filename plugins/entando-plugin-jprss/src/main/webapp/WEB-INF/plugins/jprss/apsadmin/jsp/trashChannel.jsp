<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block">
		<a href="<s:url namespace="/do/jprss/Rss" action="list" />"><s:text name="jprss.title.rssManagement" /></a>
		&#32;/&#32;
		<s:text name="jprss.title.userManagement.channelTrash" />
	</span>
</h1>

<div id="main">
    <s:form action="delete" namespace="/do/jprss/Rss" cssClass="form-horizontal">
        <p class="noscreen"><wpsf:hidden name="id"/></p>
        <div class="alert alert-warning">
            <p>
                <s:text name="note.deleteChannel.areYouSure" />&#32;
                <code><s:property value="title"/></code>?
            </p>
            <div class="text-center margin-large-top">
                <wpsf:submit type="button" cssClass="btn btn-warning btn-lg">
                    <span class="icon fa fa-times-circle"></span>&#32;
                    <s:text name="%{getText('label.remove')}"/>
                </wpsf:submit>
                <p class="text-center margin-small-top">
                    <s:text name="note.channel.trash.goBack" />&nbsp;
                    <a class="btn btn-link" href="<s:url namespace="/do/jprss/Rss" action="list"/>">
                        <s:text name="jprss.title.rssManagement" />
                    </a>
                </p>
            </div>
        </div>
    </s:form>
</div>