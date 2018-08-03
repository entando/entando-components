INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('seo_page_1', 'homepage', 7, 'free');
INSERT INTO pages (code, parentcode, pos, groupcode) VALUES ('seo_page_2', 'homepage', 8, 'free');

INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('seo_page_1', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Seo Page 1</property>
<property key="it">Pagina Seo 1</property>
</properties>
', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
  <useextradescriptions>false</useextradescriptions>
  <descriptions>
    <property key="en">EN Description SeoPage 1</property>
    <property key="it">Descrizione IT SeoPage 1</property>
  </descriptions>
  <keywords>
    <property key="en" useDefaultLang="true" >keyEN1.1,keyEN1.2</property>
    <property key="it">keyIT1.1,keyIT1.2,keyIT1.3,keyIT1.4</property>
  </keywords>
  <complexParameters>
    <parameter key="key1">VALUE_1</parameter>
    <parameter key="key2">VALUE_2</parameter>
    <parameter key="key5">
      <property key="fr">VALUE_5 FR</property>
      <property key="en">VALUE_5 EN</property>
      <property key="it">VALUE_5 IT</property>
    </parameter>
    <parameter key="key6">VALUE_6</parameter>
    <parameter key="key3">
      <property key="en">VALUE_3 EN</property>
      <property key="it">VALUE_3 IT</property>
    </parameter>
    <parameter key="key4">VALUE_4</parameter>
  </complexParameters>
</config>', '2018-06-20 16:31:31');
INSERT INTO pages_metadata_draft (code, titles, modelcode, showinmenu, extraconfig, updatedat) VALUES ('seo_page_2', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Seo Page 1</property>
<property key="it">Pagina Seo 1</property>
</properties>
', 'home', 1, '<?xml version="1.0" encoding="UTF-8"?>
<config>
  <useextratitles>false</useextratitles>
  <charset>utf-8</charset>
  <mimeType>text/html</mimeType>
  <useextradescriptions>false</useextradescriptions>
  <descriptions>
    <property key="en">EN Description SeoPage 2</property>
    <property key="it">Descrizione IT SeoPage 2</property>
  </descriptions>
  <keywords>
    <property key="en">keyEN2.1,keyEN2.2,keyEN2.3</property>
    <property key="it">keyIT2.1,keyIT2.2</property>
  </keywords>
  <complexParameters>
    <lang code="it">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_IT</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
    <lang code="en">
      <meta key="key5">VALUE_5_IT</meta>
      <meta key="key3" attributeName="name" useDefaultLang="false" >VALUE_3_EN</meta>
      <meta key="key2" attributeName="property" useDefaultLang="true" />
    </lang>
  </complexParameters>
</config>', '2018-06-26 17:31:31');
