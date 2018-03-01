<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%--<wp:internalServlet actionPath="/ExtStr2/do/bpm/FrontEnd/CaseProgressBar/view" />--%>
<div class="row">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <h1>Hello BPMS World!</h1>
        <br />
        Data:
        <s:property value="frontEndMilestonesData"/>

        <br />
        <br />
        <s:property value="test1"/>
    </div>
    <div class="col-md-2"></div>
</div>