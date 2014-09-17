<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />

<script type="text/javascript">
<!--//--><![CDATA[//><!--
	<s:set var="searcheableAttributes" value="searcheableAttributes" ></s:set>
	window.addEvent('domready', function() {
		<s:iterator var="attribute" value="#searcheableAttributes">
				<s:if test="#attribute.type == 'Date'">
					<s:set var="currentFieldId">jpaddressbook_contactFinding_<s:property value="#attribute.name" /></s:set>
					//<s:property value="currentFieldId" />
						var myCal_<s:property value="currentFieldId" />_dateStartFieldName = new Calendar({ <s:property value="currentFieldId" />_dateStartFieldName: 'd/m/Y' }, {
							navigation: 1,
							months: ['<s:text name="calendar.month.gen" />','<s:text name="calendar.month.feb" />','<s:text name="calendar.month.mar" />','<s:text name="calendar.month.apr" />','<s:text name="calendar.month.may" />','<s:text name="calendar.month.jun" />','<s:text name="calendar.month.jul" />','<s:text name="calendar.month.aug" />','<s:text name="calendar.month.sep" />','<s:text name="calendar.month.oct" />','<s:text name="calendar.month.nov" />','<s:text name="calendar.month.dec" />'],
							days: ['<s:text name="calendar.week.sun" />','<s:text name="calendar.week.mon" />','<s:text name="calendar.week.tue" />','<s:text name="calendar.week.wen" />','<s:text name="calendar.week.thu" />','<s:text name="calendar.week.fri" />','<s:text name="calendar.week.sat" />'],
							calendarTitle: "<s:text name="calendar.button.title" />",
							//prevTitle: '',
							//nextTitle: '',
							prevText: "<s:text name="calendar.label.prevText" />",	//Mese precedente
							nextText: "<s:text name="calendar.label.nextText" />",	//Mese successivo
							introText: "<s:text name="calendar.label.introText" />"	//Benvenuto nel calendario
						});

						var myCal_<s:property value="currentFieldId" />_dateEndFieldName = new Calendar({ <s:property value="currentFieldId" />_dateEndFieldName: 'd/m/Y' }, {
							navigation: 1,
							months: ['<s:text name="calendar.month.gen" />','<s:text name="calendar.month.feb" />','<s:text name="calendar.month.mar" />','<s:text name="calendar.month.apr" />','<s:text name="calendar.month.may" />','<s:text name="calendar.month.jun" />','<s:text name="calendar.month.jul" />','<s:text name="calendar.month.aug" />','<s:text name="calendar.month.sep" />','<s:text name="calendar.month.oct" />','<s:text name="calendar.month.nov" />','<s:text name="calendar.month.dec" />'],
							days: ['<s:text name="calendar.week.sun" />','<s:text name="calendar.week.mon" />','<s:text name="calendar.week.tue" />','<s:text name="calendar.week.wen" />','<s:text name="calendar.week.thu" />','<s:text name="calendar.week.fri" />','<s:text name="calendar.week.sat" />'],
							calendarTitle: "<s:text name="calendar.button.title" />",
							//prevTitle: '',
							//nextTitle: '',
							prevText: "<s:text name="calendar.label.prevText" />",	//Mese precedente
							nextText: "<s:text name="calendar.label.nextText" />",	//Mese successivo
							introText: "<s:text name="calendar.label.introText" />"	//Benvenuto nel calendario
						});

				</s:if>
		</s:iterator>
	});
//--><!]]></script>
