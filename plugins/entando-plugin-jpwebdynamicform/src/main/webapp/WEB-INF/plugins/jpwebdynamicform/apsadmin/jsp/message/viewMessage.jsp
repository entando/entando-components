<%@ taglib prefix="s" uri="/struts-tags" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />" 
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />">
            <s:text name="title.messageManagement" />&#32;/&#32;
        </a>
        <s:text name="title.messageManagement.details" />&#32;/&#32;
        <s:text name="title.message.original" />
    </span>
</h1>
<div id="main">
    <s:set name="id" value="message.id" />
    <s:set name="typeDescr" value="message.typeDescr" />

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