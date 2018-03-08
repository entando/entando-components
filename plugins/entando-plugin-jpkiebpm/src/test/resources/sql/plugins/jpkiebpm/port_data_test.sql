INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpkiebpm_config', 'KIE-BPM service configuration',
'<?xml version="1.0" encoding="UTF-8"?>
<kiaBpmConfigFactory>
   <kieBpmConfigeMap>
      <entry>
         <key>localhost</key>
         <value>
            <active>true</active>
            <username>krisv</username>
            <password>krisv</password>
            <hostname>localhost</hostname>
            <schema>http</schema>
            <port>8080</port>
            <webapp>kie-server</webapp>
         </value>
      </entry>
   </kieBpmConfigeMap>
</kiaBpmConfigFactory>');