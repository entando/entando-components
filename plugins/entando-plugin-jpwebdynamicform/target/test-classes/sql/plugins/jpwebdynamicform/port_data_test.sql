INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpwebdynamicform_messageTypes', 'Definizione dei Tipi di Messaggio', '<messagetypes>
    <messagetype typecode="PER" typedescr="Person Form" >
        <attributes>
            <attribute name="Name" attributetype="Monotext" searcheable="true" required="true" />
            <attribute name="Surname" attributetype="Monotext" searcheable="true" required="true" />
            <attribute name="Address" attributetype="Monotext" searcheable="false" required="false" />
            <attribute name="eMail" attributetype="Monotext" searcheable="false" required="true" >
                <regexp><![CDATA[(.*<.+@.+.[a-z]+>)|(.+@.+.[a-z]+)]]></regexp>
            </attribute>
            <attribute name="Note" attributetype="Monotext" searcheable="false" required="true" />
        </attributes>
    </messagetype>
    <messagetype typecode="COM" typedescr="Company Form" >
        <attributes>
            <attribute name="Company" attributetype="Monotext" searcheable="true" required="true" />
            <attribute name="Address" attributetype="Monotext" searcheable="false" required="false" />
            <attribute name="eMail" attributetype="Monotext" searcheable="false" required="true" >
                <regexp><![CDATA[(.*<.+@.+.[a-z]+>)|(.+@.+.[a-z]+)]]></regexp>
            </attribute>
            <attribute name="Note" attributetype="Monotext" searcheable="false" required="true" />
        </attributes>
    </messagetype>
</messagetypes>');

INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'jpwebdynamicform_messageNotifierConfig', 'Configurazione del servizio di notifica Messaggi', '<messagetypes>
    <messagetype typeCode="PER" senderCode="CODE1" mailAttrName="eMail" notifiable="true" >
        <recipients>
            <to><![CDATA[Address 1 <address@notexistant1.it>]]></to>
            <to><![CDATA[Address 2 <address@notexistant2.itte>]]></to>
            <cc><![CDATA[Address 3 <address@notexistant3.itte>]]></cc>
            <bcc><![CDATA[Address 4 <address@notexistant4.itte>]]></bcc>
        </recipients>
        <model>
            <!-- CORPO DELLA MAIL -->
            <body><![CDATA[Corpo della mail PER]]></body>
            <!-- OGGETTO DELLA MAIL -->
            <subject><![CDATA[Oggetto della mail PER]]></subject>
        </model>
    </messagetype>
    <messagetype typeCode="COM" senderCode="CODE2" mailAttrName="eMail" store="false" >
        <recipients>
            <to><![CDATA[Address 1 <address@notexistant1.itte>]]></to>
            <to><![CDATA[Address 2 <address@notexistant2.itte>]]></to>
            <to><![CDATA[Address 3 <address@notexistant3.itte>]]></to>
            <cc><![CDATA[Address 4 <address@notexistant4.itte>]]></cc>
            <cc><![CDATA[Address 5 <address@notexistant5.itte>]]></cc>
            <bcc><![CDATA[Address 6 <address@notexistant6.itte>]]></bcc>
        </recipients>
        <model>
            <!-- CORPO DELLA MAIL -->
            <body><![CDATA[Corpo della mail COM]]></body>
            <!-- OGGETTO DELLA MAIL -->
            <subject><![CDATA[Oggetto della mail COM]]></subject>
        </model>
    </messagetype>
</messagetypes>');