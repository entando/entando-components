<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="jpstats.header.statistics" /></span>
</h1>
<div id="main">
		<s:form action="view">
			<%--Error management--%>
				<s:if test="hasFieldErrors()">
						<div class="alert alert-danger  alert-dismissable fade in">
								<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
								<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
								<ul class="unstyled margin-small-top">
										<s:iterator value="fieldErrors">
												<s:iterator value="value"><li><s:property escape="false" /></li></s:iterator>
										</s:iterator>
								</ul>
						</div>
				</s:if>
				<s:if test="hasActionErrors()">
						 <div class="alert alert-danger  alert-dismissable fade in">
									<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
								<h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
								<ul class="unstyled margin-small-top">
										<s:iterator value="actionErrors"><li><s:property  escape="false"/></li></s:iterator>
								</ul>
						</div>
				</s:if>
				<div class="form-group">
						<div class="row">
								<%--Label From--%>
									<div class="col-lg-6">
										<label for="jpstats_start_cal"><s:text name="jpstats.statistics.from.label" /></label>
										 <wpsf:textfield id="jpstats_start_cal" name="start" maxlength="254"  cssClass="text form-control datepicker" />
									 </div>
								<%--Label to--%>
									<div class="col-lg-6">
										<label for="jpstats_end_cal"><s:text name="jpstats.statistics.to.label" /></label>
										<wpsf:textfield id="jpstats_end_cal" name="end" maxlength="254" cssClass="text form-control datepicker" />
								 </div>
						</div>
				</div>
				<div class="form-group margin-large-bottom">
				 	<label class="sr-only"><s:text name="statistics.graphicType.label" /></label>
					<div class="btn-group" data-toggle="buttons">
					 	<s:iterator value="graphicType" var="entry" status="status">
							<%-- <option value="<s:property value="%{#entry.key}"/>"><s:text name="%{#entry.key}"/></option> --%>
							<label class="btn btn-default <s:if test="#status.first"> active </s:if>" for="<s:property value="%{'selectedTypes_'+#entry.key}" />" >
								<input
									type="radio"
									name="selectedTypes"
									id="<s:property value="%{'selectedTypes_'+#entry.key}" />"
									value="<s:property value="%{#entry.key}" />"
									<s:if test="#status.first"> checked="checked" </s:if>
									/>
								<s:text name="%{#entry.key}"/>
							</label>
						</s:iterator>
					</div>
				</div>
				<%-- Buttons  --%>
					<div class="form-group margin-large-top">
						<div class="btn-toolbar  margin-large-top">
								<wpsf:submit type="button" cssClass="btn btn-primary"><s:text name="jpstats.show.detail" /></wpsf:submit>
								<wpsf:submit type="button" action="buildReport" cssClass="btn btn-default"><s:text name="jpstats.statistics.report" /></wpsf:submit>
								<wpsf:submit type="button" action="trash" cssClass="btn btn-link">
									<span class="icon fa fa-times-circle"></span>&#32;
									<s:text name="jpstats.statistics.deleteStats" />
							 	</wpsf:submit>
						</div>
					</div>
		</s:form>
 </div>
<%--
<div>
<s:set name="hitsProducer" value="hitsTimeData"></s:set>
<s:if test="#hitsProducer != null">
	<cewolf:chart id="hitsChart" type="verticalXYBar" xaxislabel="Intervallo" yaxislabel="Hits" showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="hitsProducer" />
		</cewolf:data>
	</cewolf:chart>
	<cewolf:img chartid="hitsChart" height="200" width="400" renderer="cewolf" mime="image/png" />
</s:if>
<s:else>
<div style="height: 200px;width:400px;"></div>
</s:else>
</div>
--%>

<%--
<div>
<s:set name="topPagesProducer" value="mostVisitedPagestimeData"></s:set>
<s:if test="#topPagesProducer != null">
	<cewolf:chart id="topPagesChart" type="horizontalBar"
			xaxislabel="pagecode" yaxislabel="Number of Visits"
			showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="topPagesProducer" />
		</cewolf:data>
	</cewolf:chart>
	<cewolf:img chartid="topPagesChart" height="200" width="700" renderer="cewolf" mime="image/png" />
</s:if>
<s:else>
<div style="height: 200px;width:400px;"></div>
</s:else>
</div>
--%>

<%--
<div>
<s:set name="topContentsProducer" value="topContentsDataset"></s:set>
<s:if test="#topContentsProducer != null">
	<cewolf:chart id="topContentsChart" type="horizontalBar"
		xaxislabel="content" yaxislabel="hits"
		showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="topContentsProducer" />
		</cewolf:data>
	</cewolf:chart>
	<cewolf:img chartid="topContentsChart" height="200" width="700" renderer="cewolf" mime="image/png" />
</s:if>
<s:else>
<div style="height: 200px;width:400px;"></div>
</s:else>
</div>
--%>

<%--
<dl class="table-display">
	<dt><s:text name="statistics.AverageTimeSite.label" /></dt>
		<dd><s:property value="averageTimeSite"/></dd>
	<dt><s:text name="statistics.AverageTimePage.label" /></dt>
		<dd><s:property value="averageTimePage"/></dd>
	<dt><s:text name="statistics.NumPageSession.label" /></dt>
		<dd><s:property value="numPageSession"/></dd>
	<dt><s:text name="statistics.ip.label" /></dt>
		<dd><s:property value="ipByDateInterval"/></dd>
</dl>
--%>