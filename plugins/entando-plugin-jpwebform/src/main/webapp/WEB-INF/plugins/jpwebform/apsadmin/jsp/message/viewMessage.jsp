<%@ taglib prefix="s" uri="/struts-tags" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="list" />" 
           title="<s:text name="note.goToSomewhere" />:&#32;<s:text name="title.messageManagement" />">
            <s:text name="title.messageManagement" />
        </a>&#32;/&#32;
        <s:text name="title.messageManagement.details" />&#32;<s:property value="#id"/>
    </span>
</h1>

<s:set var="id" value="message.id" />
<s:set var="typeDescr" value="message.typeDescr" />
<div id="main">
    <s:include value="inc/include_messageDetails.jsp" />
    <s:if test="%{answers.size()==0}">
    </s:if>
    <s:else>
        <h3 class="margin-more-top"><s:text name="title.message.answers" /></h3>
        <s:iterator var="answer" value="answers">
            <table class="table table-bordered">
                <tr>
                    <th><s:date name="#answer.sendDate" format="dd/MM/yyyy - HH:mm"/></th>
                    <td><s:property value="%{#answer.text}" /></td>
                </tr>
            </table>
        </s:iterator>
    </s:else>
</div>