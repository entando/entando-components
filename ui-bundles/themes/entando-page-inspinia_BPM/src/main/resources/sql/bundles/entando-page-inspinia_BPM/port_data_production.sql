INSERT INTO pagemodels (code, descr, frames, plugincode, templategui) VALUES ('entando-page-inspinia_BPM', 'Inspinia - BPM Case Management widgets', '<frames>
	<frame pos="0">
		<descr>bpm-case-instance-selector</descr>
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
    </head>
    <body class="bpm-inspinia">
        <div class="pace pace-inactive">
            <div class="pace-progress" data-progress-text="100%" data-progress="99" style="transform: translate3d(100%, 0px, 0px);">
                <div class="pace-progress-inner"></div>
            </div>
            <div class="pace-activity"></div>
        </div>
        <header class="header-fixed">
            <div class="header-limiter">
                <h1>
                    <a href="#">Entando Case Management Widgets<span></span></a>
                </h1>
                <span class=" text-right user-logged">Entando Admin</span>
            </div>
        </header>
        <div  class="container-bpm" style="margin:10px;">
            <div class="row ">
                <div class="col-md-12">
                    <@wp.show frame=0 />
                </div>
            </div>

            <div class="row white-bg">
                <div class="col-md-12">
                    <@wp.show frame=1 />
                </div>
            </div>
            <div class="row white-bg">
                <div class="col-md-12">
                    <@wp.show frame=2 />
                </div>
            </div>
            <div class="row white-bg">
                <div class="col-md-6">
                    <@wp.show frame=3 />
                </div>
                <div class="col-md-6">
                    <@wp.show frame=4 />
                </div>
            </div>
            <div class="row white-bg">
                <div class="col-md-12">
                    <@wp.show frame=5 />
                </div>
            </div>
            <div class="row white-bg">
                <div class="col-md-6">
                    <@wp.show frame=6 />
                </div>
                <div class="col-md-6">
                    <@wp.show frame=7 />
                </div>
            </div>
        </div>
    </body>
</html>');
