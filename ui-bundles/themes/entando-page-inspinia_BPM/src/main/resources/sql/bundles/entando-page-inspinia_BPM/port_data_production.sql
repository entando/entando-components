INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ('entando-page-inspinia_BPM', 'Inspinia - BPM Case Management widgets', '<frames>
	<frame pos="0">
		<descr>Header sample</descr>
		<sketch x1="0" y1="0" x2="11" y2="0" />
		<defaultWidget code="bpm-case-instance-selector">
			<properties>
				<property key="channel">1</property>
				<property key="frontEndCaseData">{"container-id":"itorders_1.0.0-SNAPSHOT","knowledge-source-id":"1"}</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="1">
		<descr>Case details</descr>
		<sketch x1="0" y1="1" x2="11" y2="1" />
		<defaultWidget code="bpm-case-details">
			<properties>
				<property key="channel">1</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="2">
		<descr>Progress Bar basic with label on center</descr>
		<sketch x1="0" y1="2" x2="11" y2="2" />
		<defaultWidget code="bpm-case-progress-status">
			<properties>
				<property key="channel">1</property>
				<property key="frontEndMilestonesData">{"container-id":"itorders_1.0.0-SNAPSHOT","name":"Order for IT hardware","knowledge-source-id":"1","case-id-prefix":"IT","stages":[],"id":"itorders.orderhardware","milestones":[{"milestone-id":"_DCD97847-6E3C-4C5E-9EE3-221C04BE42ED","milestone-name":"Milestone 1: Order placed","milestone-mandatory":false,"visible":true,"percentage":20},{"milestone-id":"_343B90CD-AA19-4894-B63C-3CE1906E6FD1","milestone-name":"Milestone 2: Order shipped","milestone-mandatory":false,"visible":true,"percentage":20},{"milestone-id":"_52AFA23F-C087-4519-B8F2-BABCC31D68A6","milestone-name":"Milestone 3: Delivered to customer","milestone-mandatory":false,"visible":true,"percentage":20},{"milestone-id":"_483CF785-96DD-40C1-9148-4CFAFAE5778A","milestone-name":"Hardware spec ready","milestone-mandatory":false,"visible":true,"percentage":20},{"milestone-id":"_79953D58-25DB-4FD6-94A0-DFC6EA2D0339","milestone-name":"Manager decision","milestone-mandatory":false,"visible":true,"percentage":20}],"ui":{"progress-bar-type":"stacked","additionalSettings":["show-milestones","show-number-of-tasks"]},"version":"1.0"}</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="3">
		<descr>Rules</descr>
		<sketch x1="0" y1="3" x2="5" y2="6" />
		<defaultWidget code="bpm-case-roles">
			<properties>
				<property key="channel">1</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="4">
		<descr>Comments</descr>
		<sketch x1="6" y1="3" x2="11" y2="6" />
		<defaultWidget code="bpm-case-comments">
			<properties>
				<property key="channel">1</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="5">
		<descr>Task Tables</descr>
		<sketch x1="0" y1="7" x2="11" y2="10" />
	</frame>
	<frame pos="6">
		<descr>Chart</descr>
		<sketch x1="0" y1="11" x2="5" y2="14" />
		<defaultWidget code="bpm-case-chart">
			<properties>
				<property key="channel">1</property>
			</properties>
		</defaultWidget>
	</frame>
	<frame pos="7">
		<descr>Process Diagram</descr>
		<sketch x1="6" y1="11" x2="11" y2="14" />
		<defaultWidget code="bpm-case-file">
			<properties>
				<property key="channel">1</property>
			</properties>
		</defaultWidget>
	</frame>
</frames>', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>
            <@wp.currentPage param="title" /> - <@wp.i18n key="PORTAL_TITLE" />
        </title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta name="description" content="" />
        <meta name="author" content="" />
        <link rel="icon" href="<@wp.info key="systemParam" paramName="applicationBaseURL" />
              favicon.png" type="image/png" />
          
              <@c.import url="/WEB-INF/aps/jsp/models/inc/content_inline_editing.jsp" />
              <@c.import url="/WEB-INF/aps/jsp/models/inc/header-inclusions.jsp" />

       <script>
            $(document).ready(function () {
                var showHeaderAt = 100;
                var win = $(window),
                        body = $('body');
                if (win.width() > 400) {
                    win.on('scroll', function (e) {
                        if (win.scrollTop() > showHeaderAt) {
                            body.addClass('fixed');
                        } else {
                            body.removeClass('fixed');
                        }
                    });
                }
            });
        </script>
    
    <style>
    .header-fixed {
        background-color: #292c2f;
        box-shadow: 0 1px 1px #ccc;
        padding: 20px 40px;
        height: 80px;
        color: #ffffff;
        box-sizing: border-box;
        top: -100px;
        -webkit-transition: top 0.3s;
        transition: top 0.3s;
    }

    .header-fixed .header-limiter {

        text-align: center;
        margin: 0 auto;
    }

    .header-fixed-placeholder {
        height: 80px;
        display: none;
    }

    .header-fixed .header-limiter h1 {
        float: left;
        line-height: 40px;
        margin: 0;
    }

    .header-fixed .header-limiter h1 span {
        color: #fff;
    }

    .header-fixed .header-limiter a {
        color: #da3333;
        text-decoration: none;
    }

    .header-fixed .header-limiter nav {
        font: 16px Arial, Helvetica, sans-serif;
        line-height: 40px;
        float: right;
    }

    .header-fixed .header-limiter nav a {
        display: inline-block;
        padding: 0 5px;
        text-decoration: none;
        color: #ffffff;
        opacity: 0.9;
    }

    .header-fixed .header-limiter nav a:hover {
        opacity: 1;
    }

    .header-fixed .header-limiter nav a.selected {
        color: #608bd2;
        pointer-events: none;
        opacity: 1;
    }

    body.fixed .header-fixed {
        padding: 10px 40px;
        height: 50px;
        position: fixed;
        width: 100%;
        top: 0;
        left: 0;
        z-index: 1;
    }

    body.fixed .header-fixed-placeholder {
        display: block;
    }

    body.fixed .header-fixed .header-limiter h1 {
        font-size: 24px;
        line-height: 30px;
    }

    body.fixed .header-fixed .header-limiter nav {
        line-height: 28px;
        font-size: 13px;
    }

    @media all and (max-width: 600px) {
        .header-fixed {
            padding: 20px 0;
            height: 75px;
        }

        .header-fixed .header-limiter h1 {
            float: none;
            margin: -8px 0 10px;
            text-align: center;
            font-size: 24px;
            line-height: 1;
        }

        .header-fixed .header-limiter nav {
            line-height: 1;
            float: none;
        }

        .header-fixed .header-limiter nav a {
            font-size: 13px;
        }

        body.fixed .header-fixed {
            display: none;
        }
    }

    body {
        margin: 0;
        padding: 0;
        height: 1500px;
    }

    </style>

    </head>
    <body class="pace-done">
        <div class="pace pace-inactive">
            <div class="pace-progress" data-progress-text="100%" data-progress="99" style="transform: translate3d(100%, 0px, 0px);">
                <div class="pace-progress-inner"></div>
            </div>
            <div class="pace-activity"></div>
        </div>
        <div id="wrapper">
            <div id="page-wrapper" class="gray-bg dashbard-1">
                <div class="row white-bg">
                    <div class="col-md-3">&nbsp;</div>
                    <div class="col-md-6">
                        <div class="white-bg">
                            <@wp.show frame=0 />
                        </div>
                    </div>
                    <div class="col-md-3">&nbsp;</div>
                </div>

                <div class="row white-bg">
                    <div class="col-md-12">
                        <div class="white-bg">
                            <@wp.show frame=1 />
                        </div>
                    </div>
                </div>

                <div class="row white-bg">
                    <div class="col-md-12">
                        <div class="white-bg">
                            <@wp.show frame=2 />
                        </div>
                    </div>
                </div>

                <div class="row white-bg">
                    <div class="col-md-6">
                        <div class="white-bg">
                            <@wp.show frame=3 />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="white-bg">
                            <@wp.show frame=4 />
                        </div>
                    </div>
                </div>

                <div class="row white-bg">
                    <div class="col-md-12">
                        <div class="white-bg">
                            <@wp.show frame=5 />
                        </div>
                    </div>
                </div>

                <div class="row white-bg">
                    <div class="col-md-6">
                        <div class="white-bg">
                            <@wp.show frame=6 />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="white-bg">
                            <@wp.show frame=7 />
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>');
