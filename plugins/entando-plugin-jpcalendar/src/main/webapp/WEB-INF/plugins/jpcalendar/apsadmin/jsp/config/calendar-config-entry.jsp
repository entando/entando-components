<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <s:text name="title.jpcalendar.configManagement" />
    </span>
</h1>

<div id="main">

    <s:form action="save" namespace="/do/jpcalendar/Config">

        <s:if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h4 class="margin-none"><s:text name="message.title.FieldErrors" /></h4>	
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
            </div>
        </s:if>
        <s:if test="hasActionMessages()">
            <div class="alert alert-info alert-dismissable">
                <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
                <h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>	
                <ul class="margin-base-vertical">
                    <s:iterator value="actionMessages">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                </ul>
            </div>
        </s:if>

        <s:if test="null == contentType">
            <fieldset class="col-xs-12">
                <legend><s:text name="title.contentInfo" /></legend>
                <div class="form-group">
                    <label for="contentType" ><s:text name="label.type"/></label>
                    <wpsf:select name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="form-control" />
                </div>
            </fieldset>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                        <wpsf:submit type="button" action="configContentType" cssClass="btn btn-primary btn-block" >
                            <s:text name="%{getText('label.continue')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>	
        </s:if>
        <s:else>

            <fieldset class="col-xs-12">
                <legend><s:text name="title.contentInfo" /></legend>
                <div class="form-group">
                    <label for="contentType" ><s:text name="label.type"/>:</label>
                    <wpsf:select  name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" cssClass="form-control" />	
                </div>
                <p class="noscreen">
                    <wpsf:hidden name="contentType" />
                </p>
            </fieldset>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                        <wpsf:submit action="changeContentType" type="button" cssClass="btn btn-primary btn-block" >
                            <s:text name="%{getText('label.change')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>

            <fieldset class="col-xs-12">
                <legend><s:text name="title.attributes" /></legend>
                <div class="form-group">
                    <label for="startDateAttributeName" ><s:text name="label.startDateAttribute"/></label>
                    <wpsf:select  name="startDateAttributeName" id="startDateAttributeName" 
                                  list="allowedDateAttributes" listKey="key" listValue="value" cssClass="form-control" />
                </div>
                <div class="form-group">
                    <label for="endDateAttributeName" ><s:text name="label.endDateAttribute"/></label>
                    <wpsf:select  name="endDateAttributeName" id="endDateAttributeName" 
                                  list="allowedDateAttributes" listKey="key" listValue="value" cssClass="form-control" />
                </div>
            </fieldset>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                        <wpsf:submit action="save" type="button" cssClass="btn btn-primary btn-block" >
                            <span class="icon fa fa-floppy-o"></span>&#32;
                            <s:text name="%{getText('label.save')}"/>
                        </wpsf:submit>
                    </div>
                </div>
            </div>

        </s:else>

    </s:form>

</div>