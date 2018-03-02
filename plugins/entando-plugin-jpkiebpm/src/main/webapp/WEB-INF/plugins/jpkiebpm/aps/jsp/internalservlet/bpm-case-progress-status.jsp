<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <h1>Hello BPMS World!</h1>
        <br />

        <!--Select Case Instanse Option-->
        <s:form action="selectCaseInstance" namespace="/do/bpm/FrontEnd/CaseProgressBar" class="form-horizontal">
            <div class="form-horizontal">
                <div class="form-group">
                    <label class="control-label col-xs-4" for="processPath">Select a Case Instance</label>
                    <div class="col-xs-6">
                        <s:select list="cases" id="casePath" name="casePath">
                        </s:select>

                        <s:hidden name="frontEndMilestonesData" id="frontEndMilestonesData"></s:hidden>
                        </div>
                        <div class="col-xs-2">
                        <s:submit type="button" action="changeForm" value="Select"
                                  cssClass="btn btn-primary pull-right" />
                    </div>
                </div>
            </div>
        </s:form>

        <!--//Select Case Instanse Option-->
        <br />
        <br />
        <br />
        <br />
        <br />
        Case Definition with Milestones Configurations:

        <br />
        <s:property value="frontEndMilestonesData"/>

        <br />
        <br />       

        Case Instance List:

        <br />
        <s:property value="cases"/>

        <br />
        <br />
        Selected Case Instance Milestone:
        <br />

        <s:property value="caseInstanceMilestones"/>
    </div>
    <div class="col-md-2"></div>
</div>