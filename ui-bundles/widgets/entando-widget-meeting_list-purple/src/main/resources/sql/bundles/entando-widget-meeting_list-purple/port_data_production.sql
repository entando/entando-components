INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('entando-widget-meeting_list-purple', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
    <property key="en">entando-widget-meeting_list-purple</property>
    <property key="it">entando-widget-meeting_list-purple </property>
</properties>', NULL, NULL, NULL, NULL, 1, NULL);

INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('entando-widget-meeting_list-purple', 'entando-widget-meeting_list-purple', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.info key="langs" var="langsVar" />
<@wp.info key="currentLang" var="currentLangVar" />
<div class="ibox-content inspinia-timeline">
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="fa fa-briefcase"></i>
                6:00 am
                <br>
                <small class="text-navy">2 hour ago</small>
            </div>
            <div class="col-xs-7 content no-top-border">
                <p class="m-b-xs"><strong>Meeting</strong></p>

                <p>Conference on the sales results for the previous year. Monica please examine sales trends in marketing and products.</p>

            </div>
        </div>
    </div>
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="fa fa-file-text"></i>
                7:00 am
                <br>
                <small class="text-navy">3 hour ago</small>
            </div>
            <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Send documents to Mike</strong></p>
                <p>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since.</p>
            </div>
        </div>
    </div>
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="fa fa-coffee"></i>
                8:00 am
                <br>
            </div>
            <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Coffee Break</strong></p>
                <p>
                    Go to shop and find some products.
                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s.
                </p>
            </div>
        </div>
    </div>
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="fa fa-phone"></i>
                11:00 am
                <br>
                <small class="text-navy">21 hour ago</small>
            </div>
            <div class="col-xs-7 content">
                <p class="m-b-xs"><strong>Phone with Jeronimo</strong></p>
                <p>
                    Lorem Ipsum has been the industry''s standard dummy text ever since.
                </p>
            </div>
        </div>
    </div>
</div>', 1);
