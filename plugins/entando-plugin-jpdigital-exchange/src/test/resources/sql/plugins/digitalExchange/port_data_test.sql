 INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'digitalExchanges', 'Available Digital Exchange instances', '<?xml version="1.0" encoding="UTF-8"?>
<digitalExchanges>
  <digitalExchange>
    <id>DE_1</id>
    <name>DigitalExchange 1</name>
    <url>https://de1.entando.com/</url>
    <key>client-key</key>
    <secret>y9rSwMSXW9zU9ma9vxqiXw==</secret>
    <timeout>1000</timeout>
    <active>true</active>
  </digitalExchange>
  <digitalExchange>
    <id>DE_2</id>
    <name>DigitalExchange 2</name>
    <url>https://de2.entando.com/</url>
    <key>client-key</key>
    <secret>y9rSwMSXW9zU9ma9vxqiXw==</secret>
    <timeout>1000</timeout>
    <active>true</active>
  </digitalExchange>
</digitalExchanges>');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'digitalExchangeCategories', 'Supported Digital Exchange categories', 'widget,pageModel,component,fragment,api,contentModel,contentType');
