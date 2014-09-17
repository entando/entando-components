Include into web.xml the new servlet:

*************************
	<!-- jpstats :: start -->
	<servlet>
		<servlet-name>CewolfServlet</servlet-name>
		<servlet-class>de.laures.cewolf.CewolfRenderer</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>CewolfServlet</servlet-name>
		<url-pattern>/cewolf/*</url-pattern>
	</servlet-mapping>
	<!-- jpstats :: END end -->
*************************








