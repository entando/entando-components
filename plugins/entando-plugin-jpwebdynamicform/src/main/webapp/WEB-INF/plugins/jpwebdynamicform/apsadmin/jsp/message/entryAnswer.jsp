<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpwebdynamicform.menu.integration"/></li>
    <li>
        <s:text name="jpwebdynamicform.menu.uxcomponents"/>
    </li>
	<li>
		<s:text name="jpwebdynamicform.name"/>
	</li>
	
    <li>
        <a href="<s:url action="list" />"
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />">
            <s:text name="title.messageManagement"/>
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.message.answers"/>
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.messageManagement.newAnswer"/>
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="<s:text name="title.messageManagement.newAnswer.help"/>" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>


<div id="main2">
    <s:set var="id" value="message.id"/>
    <s:set var="typeCode" value="message.typeCode"/>
    <div class="panel panel-default">
        <div class="panel-body">
            <s:text name="title.messageManagement.newAnswer.info"/>:&#32;<em><s:property
                value="message.typeDescr"/></em>&#32;(<code><s:property value="%{#id}"/></code>)
        </div>
    </div>
    <s:form action="sendAnswer" enctype="multipart/form-data">
        <s:hidden name="_csrf" value="%{csrfToken}"/>
        <s:include value="/WEB-INF/apsadmin/jsp/common/inc/messages.jsp" />
        <div>
            <s:include value="inc/include_messageDetails.jsp"/>
        </div>

        <legend><s:text name="label.answer"/></legend>
        <fieldset class="form-horizontal">
            <p class="noscreen">
                <wpsf:hidden name="id"/>
            </p>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="text"><s:text name="text"/></label>
                <div class="col-sm-10">
                    <wpsf:textarea name="text" id="text" cssClass="form-control" cols="60" rows="20"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label" for="attachment"><s:text name="attachment"/></label>
                <div class="col-sm-10">
                    <input type="file" id="attachment" name="attachment" value="%{getText('label.browse')}"/>
                </div>
            </div>
            <div class="form-group">
                <div class="col-xs-12">
                    <s:submit type="button" cssClass="btn btn-primary pull-right">
                        <s:text name="%{getText('label.send')}"/>
                    </s:submit>
                </div>
            </div>
        </fieldset>
    </s:form>
</div>
