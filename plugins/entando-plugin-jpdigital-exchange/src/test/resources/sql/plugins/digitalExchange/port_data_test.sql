 INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'digitalExchanges', 'Available Digital Exchange instances', '<?xml version="1.0" encoding="UTF-8"?>
<digitalExchanges>
  <digitalExchange>
    <name>DE 1</name>
    <url>https://de1.entando.com/</url>
    <timeout>1000</timeout>
    <active>true</active>
  </digitalExchange>
  <digitalExchange>
    <name>DE 2</name>
    <url>https://de2.entando.com/</url>
    <timeout>1000</timeout>
    <active>true</active>
  </digitalExchange>
</digitalExchanges>');
INSERT INTO sysconfig (version, item, descr, config) VALUES ('test', 'digitalExchangeCategories', 'Supported Digital Exchange categories', 'pageModels,fragments,widgets,contentModels,contentTypes');
