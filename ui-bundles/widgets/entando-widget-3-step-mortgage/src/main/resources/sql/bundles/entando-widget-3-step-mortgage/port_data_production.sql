INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-3-step-mortgage', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
    <property key="en">3 step process demo Mortgage banner</property>
    <property key="it">3 step del Mutuo demo banner </property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-3-step-mortgage', 'entando-widget-3-step-mortgage', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />
<div class="text-center">
    <div class="title-box-icon">
        <h2 class="title-mortgage">Mortgage Process</h2>
        <div class="progress-dots">
            <div class="progress-dots-row">
                <div class="progress-dots-step">
                    <button type="button" class="btn btn-primary btn-circle">1</button>
                    <p><strong>Fill-in the form</strong></p>
                    <p>Answer some basic questions about your situation<br> and see what you qualify for.</p>
                </div>
                <div class="progress-dots-step">
                    <button type="button" class="btn btn-primary btn-circle">2</button>
                    <p><strong>Wait a week</strong></p>
                    <p>We make the commitment to review your<br> mortgage application within 7 days.</p>
                </div>
                <div class="progress-dots-step">
                    <button type="button" class="btn btn-primary btn-circle">3</button>
                    <p><strong>Get the funds</strong></p>
                    <p>You''ll receive your funds directly <br>to your bank in as little as a week!</p>
                </div> 
            </div>
        </div>
    </div>
</div>', 1);
