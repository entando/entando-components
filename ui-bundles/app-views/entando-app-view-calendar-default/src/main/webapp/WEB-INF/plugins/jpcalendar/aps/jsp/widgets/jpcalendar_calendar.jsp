<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cal" uri="/jpcalendar-aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<cal:calendar nomeGruppo="jpcalendarCal" datePattern="yyyyMMdd" />
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yyyyMMdd" var="today" />
<wp:pageWithWidget var="jpcalendar_dailyEventsVar" widgetTypeCode="jpcalendar_dailyEvents" />
<div class="row-fluid">
	<div class="span12">
		<form class="form-inline text-center" action="<wp:url />" method="post">
			<div class="input-prepend input-append">
				<a href="<wp:url>
							<wp:parameter name="month" ><c:out value="${prevMonth}"/></wp:parameter>
							<wp:parameter name="year" ><c:out value="${prevYear}"/></wp:parameter>
						</wp:url>"
					title="<wp:i18n key="jpcalendar_MONTH_PREVIOUS" />"
					class="add-on">
						<span class="noscreen sr-only"><wp:i18n key="jpcalendar_MONTH_PREVIOUS" /></span>
						<span class="icon icon-step-backward"></span>
				</a>
				<label class="noscreen sr-only">
					<wp:i18n key="jpcalendar_MONTH_CHOOSE" />
				</label>
				<select name="month">
					<option <c:if test="${selectedMonth == '0'}">selected="selected"</c:if> value="0">
						<wp:i18n key="jpcalendar_MONTH_JANUARY" />
					</option>
					<option <c:if test="${selectedMonth == '1'}">selected="selected"</c:if> value="1">
						<wp:i18n key="jpcalendar_MONTH_FEBRUARY" />
					</option>
					<option <c:if test="${selectedMonth == '2'}">selected="selected"</c:if> value="2">
						<wp:i18n key="jpcalendar_MONTH_MARCH" />
					</option>
					<option <c:if test="${selectedMonth == '3'}">selected="selected"</c:if> value="3">
						<wp:i18n key="jpcalendar_MONTH_APRIL" />
					</option>
					<option <c:if test="${selectedMonth == '4'}">selected="selected"</c:if> value="4">
						<wp:i18n key="jpcalendar_MONTH_MAY" />
					</option>
					<option <c:if test="${selectedMonth == '5'}">selected="selected"</c:if> value="5">
						<wp:i18n key="jpcalendar_MONTH_JUNE" />
					</option>
					<option <c:if test="${selectedMonth == '6'}">selected="selected"</c:if> value="6">
						<wp:i18n key="jpcalendar_MONTH_JULY" />
					</option>
					<option <c:if test="${selectedMonth == '7'}">selected="selected"</c:if> value="7">
						<wp:i18n key="jpcalendar_MONTH_AUGUST" />
					</option>
					<option <c:if test="${selectedMonth == '8'}">selected="selected"</c:if> value="8">
						<wp:i18n key="jpcalendar_MONTH_SEPTEMBER" />
					</option>
					<option <c:if test="${selectedMonth == '9'}">selected="selected"</c:if> value="9">
						<wp:i18n key="jpcalendar_MONTH_OCTOBER" />
					</option>
					<option <c:if test="${selectedMonth == '10'}">selected="selected"</c:if> value="10">
						<wp:i18n key="jpcalendar_MONTH_NOVEMBER" />
					</option>
					<option <c:if test="${selectedMonth == '11'}">selected="selected"</c:if> value="11">
						<wp:i18n key="jpcalendar_MONTH_DECEMBER" />
					</option>
				</select>
				<a href="<wp:url>
							<wp:parameter name="month" ><c:out value="${nextMonth}"/></wp:parameter>
							<wp:parameter name="year" ><c:out value="${nextYear}"/></wp:parameter>
						</wp:url>"
					title="<wp:i18n key="jpcalendar_MONTH_NEXT" />"
					class="add-on">
						<span class="noscreen sr-only"><wp:i18n key="jpcalendar_MONTH_NEXT" /></span>
						<span class="icon icon-step-forward"></span>
				</a>
			</div>
			&ensp;
			<div class="input-append">
				<label class="noscreen sr-only">
					<wp:i18n key="jpcalendar_YEAR_CHOOSE" />
				</label>
				<select name="year">
					<c:forEach items="${yearsForSelect}" var="yearOpt">
						<option <c:if test="${selectedYear == yearOpt}">selected="selected"</c:if> value="<c:out value="${yearOpt}" />"><c:out value="${yearOpt}" /></option>
					</c:forEach>
				</select>
				<button title="<wp:i18n key="jpcalendar_SEARCH_GO" />" type="submit" class="btn btn-primary">
					<span class="noscreen sr-only"><wp:i18n key="jpcalendar_SEARCH_GO" /></span>
					<span class="icon icon-search"></span>
				</button>
			</div>
		</form>
	</div>
</div>
<div class="row-fluid">
	<div class="span12">
		<table class="table table-hover" summary="<wp:i18n key="jpcalendar_SUMMARY" />">
			<caption class="sr-only noscreen"><wp:i18n key="jpcalendar_CAPTION" /></caption>
			<tr>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_NUMBER" /></th>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_MONDAY" /></th>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_TUESDAY" /></th>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_WEDNESDAY" /></th>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_THURSDAY" /></th>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_FRIDAY" /></th>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_SATURDAY" /></th>
				<th scope="col"><wp:i18n key="jpcalendar_WEEK_SUNDAY" /></th>
			</tr>
			<c:forEach items="${jpcalendarCal.calendario}" var="week" varStatus="status">
				<tr>
					<th scope="row">
						<c:choose>
							<c:when test="${jpcalendarCal.settimane[status.count-1] == jpcalendarCal.currentWeek}">
								<c:out value="${jpcalendarCal.settimane[status.count-1]}"/>
							</c:when>
							<c:otherwise>
								<c:out value="${jpcalendarCal.settimane[status.count-1]}"/>
							</c:otherwise>
						</c:choose>
					</th>
					<c:forEach items="${week}" var="dayItem" varStatus="dayStatus">
						<c:if test="${!(empty dayItem)}">
							<td class="text-center <c:if test="${(dayItem.formattedDate == today)}"> today text-info </c:if><c:if test="${(dayItem.hasEvents)}"> hasEvents </c:if>">
								<c:choose>
									<c:when test="${dayItem.hasEvents}">
										<a class="badge badge-<c:out value="${dayItem.formattedDate == today?'warning':'info'}" />"
											href="<wp:url page="${jpcalendar_dailyEventsVar.code}" >
													<wp:parameter name="selectedDate" ><c:out value="${dayItem.formattedDate}"/></wp:parameter></wp:url>"><c:out value="${dayItem.day}"/></a>
									</c:when>
									<c:when test="${dayItem.formattedDate == today}">
										<span class="badge"><c:out value="${dayItem.day}"/></span>
									</c:when>
									<c:otherwise><c:out value="${dayItem.day}"/></c:otherwise>
								</c:choose>
							</td>
						</c:if>
						<c:if test="${dayItem == null}"><td></td></c:if>
					</c:forEach>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>

