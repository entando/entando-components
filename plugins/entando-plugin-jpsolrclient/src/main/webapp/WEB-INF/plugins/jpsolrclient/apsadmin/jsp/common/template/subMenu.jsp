<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:ifauthorized permission="superuser" var="isSuperuser"/>
<c:if test="${isSuperuser}">
	<li class="openmenu"><a href="#" rel="fagiano_jpwebform" id="menu_jpwebform" class="subMenuToggler"><s:text name="jpsolrclient.menu.admin" /></a>
		<div class="menuToggler" id="fagiano_jpwebform"><div class="menuToggler-2"><div class="menuToggler-2">
					<ul>
						<li>
							<a href="<s:url namespace="/do/jpsolrclient/Config" action="intro"></s:url>" >
								<s:text name="jpsolrclient.menu.config" />
							</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</li>
</c:if>