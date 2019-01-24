INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ('test_page_model', 'DE Test Page Model', '<frames>
    <frame pos="0">
        <descr>Sample Frame</descr>
    </frame>
</frames>', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
    <head>
        <title><@wp.currentPage param="title" /></title>
        <link rel="stylesheet" href="<@wp.resourceURL />components/test_page_model/test.css" />
    </head>
    <body>
        <h1><@wp.currentPage param="title" /></h1>
        <a href="<@wp.url page="homepage" />">Home</a><br>
        <div>
            <h1><@wp.i18n key="TEST_LABEL" /></h1>
            <@wp.show frame=0 />
        </div>
    </body>
</html>');
