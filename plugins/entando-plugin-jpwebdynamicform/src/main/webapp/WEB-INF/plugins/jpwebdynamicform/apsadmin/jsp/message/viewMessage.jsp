<%@ taglib prefix="s" uri="/struts-tags" %>
<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpwebdynamicform.menu.integration"/></li>
    <li>
        <s:text name="jpwebdynamicform.menu.uxcomponents"/>
    </li>
    <li>
        <a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />">
            <s:text name="title.messageManagement" />
        </a>
    </li>
    <li class="page-title-container">
        <s:text name="title.messageManagement.details" />
    </li>
</ol>
<h1 class="page-title-container">
    <div>
        <s:text name="title.messageManagement.details" />
        <span class="pull-right">
            <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
               data-content="TO be inserted" data-placement="left" data-original-title="">
                <i class="fa fa-question-circle-o" aria-hidden="true"></i>
            </a>
        </span>
    </div>
</h1>
<div class="text-right">
    <div class="form-group-separator"></div>
</div>
<br>

<div id="main">
    <s:set var="id" value="message.id" />
    <s:set var="typeDescr" value="message.typeDescr" />

    <s:if test="%{answers.size()==0}">
        <div class="panel panel-default">
            <div class="panel-body">
                <s:text name="title.messageManagement.details.info" />:&#32;<em><s:property value="#typeDescr"/></em>&#32;(<code><s:text name="%{#id}"/></code>)
            </div>
        </div>
        <s:include value="inc/include_messageDetails.jsp" />
    </s:if>
    <s:else>
        <div class="panel panel-default">
            <div class="panel-body">
                <s:text name="title.messageManagement.details.info" />:&#32;<em><s:property value="#typeDescr"/></em>&#32;(<code><s:text name="%{#id}"/></code>)
            </div>
        </div>
        <s:include value="inc/include_messageDetails.jsp" />
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="margin-none"><s:text name="title.message.answers" /></h3>
            </div>                    
            <div class="panel-body">
                    <table class="table table-bordered">
                <s:iterator var="answer" value="answers">
                        <tr>
                            <th class="col-sm-4"><s:date name="#answer.sendDate" format="dd/MM/yyyy - HH:mm"/></th>
                            <td><s:property value="%{#answer.text}" /></td>
                        </tr>
                </s:iterator>
                    </table>
            </div>
        </div>
    </s:else>
</div>