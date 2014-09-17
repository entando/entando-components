INSERT INTO pagemodels (code, descr, frames, plugincode) VALUES ('entando-page-myportalplus-basic', 'My Portal - Basic', '<frames>
  <frame pos="0" locked="true">
    <descr>Navbar 1</descr>
    <defaultWidget code="entando-widget-language_choose" />
  </frame>
  <frame pos="1" locked="true">
    <descr>Navbar 2</descr>
    <defaultWidget code="entando-widget-navigation_bar">
      <properties>
        <property key="navSpec">code(homepage)</property>
      </properties>
    </defaultWidget>
  </frame>
  <frame pos="2" locked="true">
    <descr>Navbar 3</descr>
    <defaultWidget code="entando-widget-search_form" />
  </frame>
  <frame pos="3" locked="true">
    <descr>Navbar 4</descr>
    <defaultWidget code="entando-widget-login_form" />
  </frame>
  <frame pos="4" locked="true">
    <descr>Toolbar 1</descr>
  </frame>
  <frame pos="5" column="1" locked="false"><descr>First Column I</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="6" column="1" locked="false"><descr>First Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="7" column="1" locked="false"><descr>First Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="8" column="2" locked="false"><descr>Second Column I</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="9" column="2" locked="false"><descr>Second Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="10" column="2" locked="false"><descr>Second Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="11" column="3" locked="false"><descr>Third Column I</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="12" column="3" locked="false"><descr>Third Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="13" column="3" locked="false"><descr>Third Column II</descr><defaultWidget code="jpmyportalplus_void" /></frame>
  <frame pos="14" locked="true">
    <descr>Footer 1</descr>
  </frame>
  <frame pos="15" locked="true">
    <descr>Footer 2</descr>
  </frame>
  <frame pos="16" locked="true">
    <descr>Footer 3</descr>
  </frame>
  <frame pos="17" locked="true">
    <descr>Footer 4</descr>
  </frame>
  <frame pos="18" locked="true">
    <descr>Footer 5</descr>
  </frame>
</frames>', 'jpmyportalplus');


INSERT INTO jpmyportalplus_modelconfig(code, config) VALUES ('entando-page-myportalplus-basic', '<frames>
  <frame pos="0" locked="true"></frame>
  <frame pos="1" locked="true"></frame>
  <frame pos="2" locked="true"></frame>
  <frame pos="3" locked="true"></frame>
  <frame pos="4" locked="true"></frame>
  <frame pos="5" column="1" locked="false"></frame>
  <frame pos="6" column="1" locked="false"></frame>
  <frame pos="7" column="1" locked="false"></frame>
  <frame pos="8" column="2" locked="false"></frame>
  <frame pos="9" column="2" locked="false"></frame>
  <frame pos="10" column="2" locked="false"></frame>
  <frame pos="11" column="3" locked="false"></frame>
  <frame pos="12" column="3" locked="false"></frame>
  <frame pos="13" column="3" locked="false"></frame>
  <frame pos="14" locked="true"></frame>
  <frame pos="15" locked="true"></frame>
  <frame pos="16" locked="true"></frame>
  <frame pos="17" locked="true"></frame>
  <frame pos="18" locked="true"></frame>
</frames>');