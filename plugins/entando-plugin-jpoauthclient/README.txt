Insert into web.xml

	...
	...
	<servlet>
        <servlet-name>Callback</servlet-name>
        <servlet-class>org.entando.entando.plugins.jpoauthclient.aps.servlet.Callback</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Callback</servlet-name>
        <url-pattern>/OAuth/Callback</url-pattern>
    </servlet-mapping>
	...
	...