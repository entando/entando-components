<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpwebdynamicform.menu.integration"/></li>
    <li>
        <s:text name="jpwebdynamicform.menu.uxcomponents"/>
    </li>
    <li class="page-title-container">
        <s:text name="%{'title.' + entityManagerName + '.management'}"/>
    </li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpwebdynamicform.name"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true"
                       title="" data-content="<s:text name="jpwebdynamicform.menu.messageTypes.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />">
                        <s:text name="jpwebdynamicform.menu.messages"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpwebdynamicform/Message/Config" action="list" />">
                        <s:text name="jpwebdynamicform.menu.config"/>
                    </a>
                </li>
                <li class="active">
                    <a href="<s:url namespace="/do/jpwebdynamicform/Entity" action="viewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>">
                        <s:text name="%{'title.' + entityManagerName + '.management'}"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br>

<s:include value="/WEB-INF/apsadmin/jsp/entity/include/entity-type-list-body.jsp"/>
