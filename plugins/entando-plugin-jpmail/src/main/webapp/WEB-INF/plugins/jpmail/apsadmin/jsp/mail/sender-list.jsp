<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.eMailManagement" />&#32;/&#32;
        <s:text name="title.eMailManagement.sendersConfig" />
    </span>
</h1>
<div id="main">	
    <p>
        <a class="btn btn-default margin-base-bottom" href="<s:url action="newSender" />" ><span class="icon fa fa-plus-circle"> <s:text name="label.senders.new" /></span></a>
    </p>

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <s:if test="%{senderCodes.size()==0}">
        <p><s:text name="label.senders.none" /></p>
    </s:if>
    <s:else>
        <table class="table table-bordered">
            <tr>
                <th class="text-center text-nowap col-xs-6 col-sm-3 col-md-3 col-lg-3 "><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
                <th><s:text name="code" /></th>
                <th><s:text name="mail" /></th>
            </tr>
            <s:iterator value="%{config.senders.entrySet()}" var="sender">
                <tr>
                    <td class="text-center text-nowrap">
                        <div class="btn-group btn-group-xs">
                            <a class="btn btn-default" href="<s:url action="editSender" >
                                   <s:param name="code" value="#sender.key" /></s:url>" 
                               title="<s:text name="label.edit" />: <s:property value="#sender.value" />">
                                <span class="icon fa fa-pencil-square-o"></span>                                                    
                            </a>        
                        </div>
                        <div class="btn-group btn-group-xs">
                            <a class="btn btn-warning" href="<s:url action="trashSender" >
                                   <s:param name="code" value="#sender.key" /></s:url>" 
                               title="<s:text name="label.remove" />: <s:property value="#sender.value" />">
                                <span class="icon fa fa-times-circle-o"></span>
                            </a>
                        </div>        
                    </td>
                    <td><code><s:property value="#sender.key"/></code></td>
                    <td><s:property value="#sender.value" /></td>
                </tr>
            </s:iterator>
        </table>
    </s:else>
</div>