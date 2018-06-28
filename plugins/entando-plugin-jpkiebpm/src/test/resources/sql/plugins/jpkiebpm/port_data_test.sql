INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpkiebpm_config', 'KIE-BPM service configuration',
'<?xml version="1.0" encoding="UTF-8"?>
<kiaBpmConfigFactory>
   <kieBpmConfigeMap>
      <entry>
         <key>1</key>
         <value>
            <active>true</active>
            <id>1</id>
            <name>default</name>
            <username>krisv</username>
            <password>krisv</password>
            <hostname>localhost</hostname>
            <schema>http</schema>
            <port>8080</port>
            <timeout>1000</timeout>
            <webapp>kie-server</webapp>
         </value>
      </entry>
   </kieBpmConfigeMap>
</kiaBpmConfigFactory>');