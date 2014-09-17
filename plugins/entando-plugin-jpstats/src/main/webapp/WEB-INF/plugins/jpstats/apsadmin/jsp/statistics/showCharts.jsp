<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"> <a  href="<s:url action="entryPoint" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpstats.header.statistics" />"><s:text name="jpstats.header.statistics" /></a>&#32;/&#32;<s:text name="jpstats.title.stats.detail" /></span>
</h1>
<div id="main">
		<%--Error management--%>
				<s:if test="hasFieldErrors()">
					 <div class="alert alert-danger alert-dismissable fade in">
								<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
								<h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
								<ul class="unstyled margin-small-top">
										<s:iterator value="fieldErrors">
											 <s:iterator value="value"><li><s:property escape="false" /></li></s:iterator>
									 </s:iterator>
								</ul>
						</div>
				</s:if>
	<p>
		<s:text name="jpstats.message.intro.stats"  />&#32;<s:date name="startDate" format="dd/MM/yyyy" />&#32;&mdash;&#32;<s:date name="endDate" format="dd/MM/yyyy" />.
	</p>
	<%-- Hits --%>
	<s:if test="selectedTypes.contains('hits')">
				<s:set name="hitsProducer" value="hitsTimeData"></s:set>
    <s:if test="#hitsProducer != null">
        <s:set var="xaxisLabelVar" value="%{getText('label.axis.intervallo')}" scope="page" />
				<s:set var="yaxisLabelVar" value="%{getText('label.axis.hits')}" scope="page" />
				<cewolf:chart id="hitsChart" type="verticalXYBar" xaxislabel="${xaxisLabelVar}" yaxislabel="${yaxisLabelVar}" showlegend="false"><cewolf:colorpaint color="#FFFFFF" />
					<cewolf:data>
						<cewolf:producer id="hitsProducer" />
					</cewolf:data>
				</cewolf:chart>
				<p><img src="<cewolf:imgurl chartid="hitsChart" height="500" width="800" renderer="cewolf" mime="image/png" />"  class="img-rounded img-responsive"></p>
    </s:if>
		</s:if>
	<%-- Top pages --%>
	<s:if test="selectedTypes.contains('topPages')">
			<s:set name="topPagesProducer" value="mostVisitedPagestimeData"></s:set>
			<s:if test="#topPagesProducer != null">
							 <s:set var="xaxisLabelVar" value="%{getText('label.axis.pagecode')}" scope="page" />
				<s:set var="yaxisLabelVar" value="%{getText('label.axis.hits')}" scope="page" />
				<cewolf:chart id="topPagesChart" type="horizontalBar" xaxislabel="${xaxisLabelVar}" yaxislabel="${yaxisLabelVar}" showlegend="false"><cewolf:colorpaint color="#FFFFFF" />
					<cewolf:data>
						<cewolf:producer id="topPagesProducer" />
					</cewolf:data>
				</cewolf:chart>
								<p><img src="<cewolf:imgurl chartid="topPagesChart" height="500" width="800" renderer="cewolf" mime="image/png" />"  class="img-rounded img-responsive"></p>
			</s:if>
	</s:if>
	<%-- TopContents --%>
	<s:if test="selectedTypes.contains('topContents')">
				<s:set name="topContentsProducer" value="topContentsDataset"></s:set>
		<s:if test="#topContentsProducer != null">
			<s:set var="xaxisLabelVar" value="%{getText('label.axis.content')}" scope="page" />
			<s:set var="yaxisLabelVar" value="%{getText('label.axis.hits')}" scope="page" />
			<cewolf:chart id="topContentsChart" type="horizontalBar" xaxislabel="${xaxisLabelVar}" yaxislabel="${yaxisLabelVar}" showlegend="false"><cewolf:colorpaint color="#FFFFFF" />
				<cewolf:data>
					<cewolf:producer id="topContentsProducer" />
				</cewolf:data>
			</cewolf:chart>
						<p><img src="<cewolf:imgurl chartid="topContentsChart" height="500" width="800" renderer="cewolf" mime="image/png" />"  class="img-rounded img-responsive"></p>
		</s:if>
	</s:if>
	<%-- Averages --%>
	<s:if test="selectedTypes.contains('averages')">
		<table class="table table-bordered ">
						<caption class="sr-only"><s:text name="Averages table" /></caption>
						<tr>
								<th scope="col" class="text-center"><s:text name="jpstats.statistics.AverageTimeSite.label" /></th>
				<th scope="col" class="text-center"><s:text name="jpstats.statistics.AverageTimePage.label" /></th>
				<th scope="col"><s:text name="jpstats.statistics.NumPageSession.label" /></th>
								<th scope="col"><s:text name="jpstats.statistics.ip.label" /></th>
						</tr>
						<tr>
								<td class="text-center"><s:property value="averageTimeSite"/> </td>
								<td class="text-center"><s:property value="averageTimePage"/> </td>
								<td class="text-right"><s:property value="numPageSession"/></td>
								<td class="text-right"><s:property value="ipByDateInterval"/> </td>
						</tr>
		</table>
	</s:if>
</div>