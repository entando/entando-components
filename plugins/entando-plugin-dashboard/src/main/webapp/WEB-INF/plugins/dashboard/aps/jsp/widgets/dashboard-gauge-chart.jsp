<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <%@ taglib prefix="wp" uri="/aps-core"%>

    <!-- Load c3.css -->
    <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.css">
      <!-- Load d3.js and c3.js -->
      <script src="https://d3js.org/d3.v5.min.js"></script>
      <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.min.js"></script>

      <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/charts/gauge/gauge.css">
        <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/charts/gauge/gauge.js"></script>

        <wp:currentWidget param="config" configParam="config" var="configGaugeChart"/>

        <script>
          $(document).ready(() => {
            console.log('jQuery ready Gauge chart');
            const config = $ {
              configGaugeChart
            };
            console.log('Config Gauge chart :', config);
            $('#title-gauge-chart').html(config.title.en);
            const gaugeChart = new org.entando.dashboard.GaugeChart("#gauge-chart", config);
            const url = "<wp:info key=" systemParam " paramName=" applicationBaseURL "/>api/plugins/dashboard/server/" + config['serverName'] + "/datasource/" + config['datasource'] + "/data";
            $.ajax({
              url,
              beforeSend: (xhr) => {
                const accessToken = '<c:out value="${sessionScope.currentUser.accessToken}"/>';
                if (accessToken) {
                  xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
                }
              }
            }).done((json) => {
              gaugeChart.update(json.payload);
            }).fail((xhr) => {
              const str = "status:" + xhr.status + " text:" + xhr.statusText + " error: " + xhr.responseJSON.errors[0].message;
              alert(str);
            });
          });
        </script>

        <div id="dashboard-gauge-chart" class="container-fluid">
          <h2 id="title-gauge-chart"></h2>
          <div id="gauge-chart"></div>
        </div>
