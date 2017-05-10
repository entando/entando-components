<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="jpversioning.admin.menu"/></li>
    <li class="page-title-container"><s:text name="title.jpversioning.resources.attaches" /></li>
</ol>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-12 col-md-6">
            <h1 class="page-title-container">
                <s:text name="jpversioning.admin.menu"/>
                <span class="pull-right">
                <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                   data-content="TO be inserted" data-placement="left" data-original-title="">
                    <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-12 col-md-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li>
                    <a href="<s:url namespace="/do/jpversioning/Content/Versioning" action="list" />"><s:text name="jpversioning.menu.contentList"/></a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Image</s:param></s:url>" ><s:text name="jpversioning.menu.images" /></a>
                </li>
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Attach</s:param></s:url>" ><s:text name="jpversioning.menu.attaches" /></a>
                </li>
            </ul>
        </div>
    </div>
</div>

<br/>

<div id="main">
    <s:form action="search" cssClass="form-horizontal">
        <p class="sr-only"><wpsf:hidden name="resourceTypeCode" /></p>

        <s:include value="inc/searchForm.jsp"/>
    </s:form>

    <div class="subsection-light">
        <s:form action="search">
            <p class="sr-only">
                <wpsf:hidden name="text" />
                <wpsf:hidden name="resourceTypeCode" />
            </p>

            <wpsa:subset source="trashedResources" count="10" objectName="groupResource" advanced="true" offset="5">
                <s:set var="group" value="#groupResource" />
                <div class="pager">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
                <ul>
                    <s:iterator var="resourceid">
                        <li class="list-group-item" >
                            <s:set var="resource" value="%{getTrashedResource(#resourceid)}"></s:set>
                            <s:set var="resourceInstance" value="%{#resource.getInstance()}"></s:set>

                            <s:set var="url" >
                                <s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
                                    <s:param name="resourceId" value="#resourceid"/>
                                    <s:param name="size" value="0"/>
                                    <s:param name="langCode" value=""/>
                                </s:url>
                            </s:set>

                            <div class="row">
                                <div class="col-lg-12">

                                    <div class="text-right no-margin">
                                        <a href="<s:url action="remove" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="label.remove" />: <s:property value="#resource.descr" /> "><span class="icon fa fa-times-circle text-warning"></span></a>
                                    </div>
                                    <a href="<s:url action="restore" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="jpversioning.label.restore" />" ><span class="icon fa fa-reply"></span></a>

                                    <s:set var="fileDescr" value="#resource.descr" />
                                    <s:if test='%{#fileDescr.length()>90}'>
                                        <s:set var="fileDescr" value='%{#fileDescr.substring(0,30)+"..."+#fileDescr.substring(#fileDescr.length()-30)}' />
                                        <abbr title="<s:property value="#resource.descr" />">
                                            <s:property value="#fileDescr" /></abbr>
                                    </s:if>
                                    <s:else>
                                        <s:property value="#resource.descr" />
                                    </s:else>
                                    <s:set var="fileName" value="#resourceInstance.fileName" />
                                    <a href="<c:out value="${url}" escapeXml="false"/>" title="<s:text name="label.download" />">
                                        <s:if test='%{#fileName.length()>25}'>
                                            <s:set var="fileName" value='%{#fileName.substring(0,10)+"..."+#fileName.substring(#fileName.length()-10)}' />
                                            <code class="margin-small-bottom"><abbr title="<s:property value="#resourceInstance.fileName" />"><s:property value="#fileName" /></abbr></code>
                                        </s:if>
                                        <s:else>
                                            <code><s:property value="#fileName" /></code>
                                        </s:else>
                                    </a>
                                    <span class="badge">
						<s:property value="%{#resourceInstance.fileLength.replaceAll(' ', '&nbsp;')}"
                                    escapeXml="false"
                                    escapeHtml="false"
                                    escapeJavaScript="false" />
						</span>
                                </div>
                            </div>
                        </li>
                    </s:iterator>
                </ul>
                <div class="pager clear">
                    <s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
                </div>
            </wpsa:subset>

        </s:form>
    </div>
</div>