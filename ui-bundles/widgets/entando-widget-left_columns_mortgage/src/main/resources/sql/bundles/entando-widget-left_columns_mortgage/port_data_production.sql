INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-left_columns_mortgage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
    <property key="en">entando-widget-left_columns_mortgage</property>
    <property key="it">entando-widget-left_columns_mortgage </property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-left_columns_mortgage', 'entando-widget-left_columns_mortgage', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />
<div class="title-box-icon">
    <h2 class="title">Mortgage Process</h2>
    <div class="progress-dots">
        <div class="row row-spacer">
            <div  class="col-md-3 ">
                <button type="button" class="btn btn-primary btn-circle">1</button>
            </div>
            <div  class="col-md-9 ">
                <p><strong>Fill-in the form</strong></p>
                <p>Answer some basic questions about your situation and see what you qualify for.</p>
            </div>
        </div>
        <div class="row row-spacer">
            <div  class="col-md-3 ">
                <button type="button" class="btn btn-primary btn-circle">2</button>
            </div>
            <div  class="col-md-9 ">
                <p><strong>Wait a week</strong></p>
                <p>We make the commitment to review your mortgage application within 7 days.</p>
            </div>
        </div>
        <div class="row row-spacer">
            <div  class="col-md-3 ">
                <button type="button" class="btn btn-primary btn-circle">3</button>
            </div>
            <div  class="col-md-9 ">
                <p><strong>Get the funds</strong></p>
                <p>You''ll receive your funds directly to your bank in as little as a week!</p>
            </div>
        </div>
        <div class="row row-big-spacer text-center">
            <div class="col-md-12 ">
                <p class="title-mortgage"><strong>$ 40 Billion +</strong></p>
                <p class="subtitle">Borrowed</p>
            </div>
            <div class="col-md-12 ">
                <p class="title-mortgage"><strong>1.2 Million +</strong></p>
                <p class="subtitle">Customers</p>
            </div>
            <div class="col-md-12 ">
                <div class="stars"></div>
                <p class="subtitle">Avarage Customer Rating</p>
            </div>
        </div>
    </div>
</div>', 1);
