<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jpnewsletter_management" >
	<li class="list-group-item">

		<span class="list-group-item-value"
			  style = "padding-left: 5px; padding-right: 15px; margin: 0 20px;
			  color: #d1d1d1; font-size: 12px; font-weight: inherit;">
			<s:text name="jpnewsletter.admin.menu" />
		</span>

		<ul class="nav nav-pills nav-stacked" style = "padding-left: 5px; padding-right: 15px; margin: 0 10px;">
			<li class="list-group-item">

				<a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter" />">
					<span class="list-group-item-value">
						<s:text name="jpnewsletter.admin.list"/>
					</span>

				</a>
			</li>
			<li class="list-group-item">
				<a href="<s:url action="list" namespace="/do/jpnewsletter/Newsletter/Queue" />">
					<span class="list-group-item-value">
						<s:text name="jpnewsletter.admin.queue"/>
					</span>
				</a>
			</li>
			<li class="list-group-item">
				<a href="<s:url action="list" namespace="/do/jpnewsletter/Subscriber" />" >
					<span class="list-group-item-value">
						<s:text name="jpnewsletter.admin.subscribersList" />
					</span>
				</a>
			</li>
			<wp:ifauthorized permission="jpnewsletter_config" >
				<li class="list-group-item">
					<a href="<s:url action="edit" namespace="/do/jpnewsletter/Newsletter/Config" />" >
						<span class="list-group-item-value">
							<s:text name="jpnewsletter.admin.config" />
						</span>
					</a>
				</li>
			</wp:ifauthorized>
		</ul>
	</li>
</wp:ifauthorized>
