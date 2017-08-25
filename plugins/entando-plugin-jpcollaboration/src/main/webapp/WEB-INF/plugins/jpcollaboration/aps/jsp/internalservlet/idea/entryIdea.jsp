<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jpcrwsrc" uri="/jpcrowdsourcing-aps-core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<%--
<s:set var="instanceCodeVar" scope="request" value="idea.instanceCode" />
<jpcrwsrc:pageWithWidget var="ideaList_page" showletTypeCode="jpcrowdsourcing_ideaInstance" configParam="instanceCode" configValue="${instanceCodeVar}" listResult="false"/>
--%>
<div class="ibox float-e-margins">
    <div class="jpcrowdsourcing entryIdea">
        <div class="ibox-title">
            <h5><wp:i18n key="jpcollaboration_NEW_IDEA_TITLE" /></h5>
        </div>	

        <div class="ibox-content">
            <form action="<wp:action path="/ExtStr2/do/collaboration/FrontEnd/Idea/NewIdea/save.action" />" method="post" accept-charset="UTF-8" >
                <s:if test="null != #parameters.entryIdea_form">
                    <s:if test="hasActionErrors()">
                        <div class="alert alert-error">
                            <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                            <p class="alert-heading"><wp:i18n key="ERRORS" /></p>
                            <ul>
                                <s:iterator value="actionErrors">
                                    <li><s:property escapeHtml="false" /></li>
                                    </s:iterator>
                            </ul>
                        </div>
                    </s:if>
                    <s:if test="hasActionMessages()">
                        <div class="alert alert-danger alert-dismissable">
                            <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>

                            <wp:i18n key="MESSAGES" /></p>
                            <ul>
                                <s:iterator value="actionMessages">
                                    <li><s:property /></li>
                                    </s:iterator>
                            </ul>
                        </div>
                    </s:if>
                    <s:if test="hasFieldErrors()">
                        <div class="alert alert-danger alert-dismissable">
                            <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                            <wp:i18n key="ERRORS" />
                            <ul>
                                <s:iterator value="fieldErrors">
                                    <s:iterator value="value">
                                        <li><s:property escapeHtml="false" /></li>
                                        </s:iterator>
                                    </s:iterator>
                            </ul>
                        </div>
                    </s:if>
                </s:if>

                <p class="noscreen">
                    <c:set var="instanceCodeVar" scope="request"><s:property value="idea.instanceCode" /></c:set>
                <jpcrwsrc:pageWithWidget var="ideaList_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceCodeVar}" listResult="false"/>
                <input type="hidden" name="saveidea_destpage" value="${ideaList_page.code}" />
                <wpsf:hidden name="idea.username" value="%{currentUser.username}" />
                <wpsf:hidden name="entryIdea_form" value="entryIdea_form" />
                <s:token name="entryIdea"/>
                </p>

                <div class="form-group">
                    <label for="idea_instanceCode" class="control-label"><wp:i18n key="jpcollaboration_INSTANCE" /></label>
                    <div class="controls">
                        <wpsf:select cssClass="form-control" list="ideaInstances" name="idea.instanceCode" id="idea_instanceCode" listKey="code" listValue="code" />
                    </div>
                </div>

                <div class="form-group">
                    <c:set var="field"><wp:i18n key="jpcollaboration_LABEL_TITLE" /></c:set>
                    <label for="idea_title" class="control-label"><wp:i18n key="jpcollaboration_LABEL_TITLE" /></label>
                    <div class="controls">
                        <wpsf:textfield name="idea.title" id="idea_title" value="%{idea.title}" cssClass="form-control" placeholder="${field}"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="idea_descr" class="control-label"><wp:i18n key="jpcollaboration_LABEL_DESCR" /></label>
                    <div class="controls">
                        <wpsf:textarea name="idea.descr" id="idea_descr" value="%{idea.descr}" cssClass="form-control" cols="40" rows="3" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="" class="control-label"><wp:i18n key="jpcollaboration_TAGS_TITLE" /></label>
                    <!--<div class="controls">-->
                    <div class="input-append">

                        <c:set var="jquery_libraries">
                            <sj:head jqueryui="true"/>
                            <script type="text/javascript">
                                <!--//--><![CDATA[//><!--
                                    jQuery.noConflict();
                                //--><!]]>
                            </script>
                        </c:set>
                        <wp:headInfo type="JS_JQUERY" var="jquery_libraries" />

                        <sj:autocompleter id="tag" name="tag" cssClass="form-control"  list="%{tagsArray}"  />


                        <s:set var="labelJoin"><wp:i18n key="jpcollaboration_TAG_JOIN" /></s:set><br><br>
                        <wpsf:submit action="joinCategory" value="%{#labelJoin}" cssClass="btn btn-sm btn-info" id="join_tag"/>
                        <script type="text/javascript">
                            //<![CDATA[//>
                            jQuery("#join_tag").click(function (event) {
                                var checktag = jQuery.isEmptyObject(jQuery("#tag_widget").val());
                                if (checktag)
                                event.preventDefault();
                                    });
                            //<!]]>
                            </script>                                         
                    </div>
                    <!--</div>-->
                </div>
                <s:if test="!(null == tags || tags.size == 0)">
                    <div class="well">
                        <ul class="dd-list">
                            <s:iterator value="tags" var="tag">
                                <s:set value="getCategory(#tag)" var="currentCat" />
                                <s:if test="null != #currentCat">
                                    <li>

                                        <wpsf:hidden name="tags" value="%{#currentCat.code}" />
                                        <wpsa:actionParam action="removeCategory" var="removeTagAction"><wpsa:actionSubParam name="tag" value="%{#currentCat.code}" /></wpsa:actionParam>
                                        <s:set var="labelRemove"><wp:i18n key="jpcollaboration_TAG_REMOVE" /></s:set>
                                            <span class="btn btn-success btn-sm ">
                                                <i class="fa fa-tag" aria-hidden="true"></i>&nbsp;&nbsp;
                                                <strong><s:property value="#currentCat.title" /></strong>
                                            &nbsp;&nbsp;<wpsf:submit action="%{#removeTagAction}" type="button" theme="simple" cssClass="btn btn-warning btn-xs " title="%{#labelRemove}" >
                                                <i class="fa fa-times"></i>							
                                            </wpsf:submit>
                                            <%-- value="%{#labelRemove}"  --%>
                                        </span>
                                    </li>
                                </s:if>
                            </s:iterator>
                        </ul>
                    </div>
                </s:if>
                <div>
                    <s:set var="labelSubmit"><wp:i18n key="jpcollaboration_SUBMIT_IDEA" /></s:set>
                    <wpsf:submit  value="%{#labelSubmit}" cssClass="btn btn-sm btn-primary pull-right" />
                </div><br><br>
            </form>
        </div>
    </div>
</div>
