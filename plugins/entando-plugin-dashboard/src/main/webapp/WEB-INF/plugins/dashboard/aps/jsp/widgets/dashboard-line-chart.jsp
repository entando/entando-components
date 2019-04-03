<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <%@ taglib prefix="wp" uri="/aps-core"%>

    <!-- Load c3.css -->
    <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.css">
      <!-- Load d3.js and c3.js -->
      <script src="https://d3js.org/d3.v5.min.js"></script>
      <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.min.js"></script>

      <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/charts/line/line.css">
        <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/charts/line/data/data.js"></script>
        <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/charts/line/line.js"></script>

        <wp:currentWidget param="config" configParam="config" var="configLineChart"/>

        <script>
          $(document).ready(() => {
            console.log('jQuery ready Line chart');
            const config = $ {
              configLineChart
            };
            console.log('Config Line chart :', config);
            $('#title-chart').html(config.title.en);
            const lineChart = new org.entando.dashboard.LineChart("#line-chart", config);
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
              lineChart.update(json.payload);
            }).fail((xhr) => {
              const str = "status:" + xhr.status + " text:" + xhr.statusText + " error: " + xhr.responseJSON.errors[0].message;
              alert(str);
            });
          });
        </script>

        <div id="dashboard-line-chart" class="container-fluid">
          <h2 id="title-chart"></h2>
          <div id="line-chart"></div>
        </div>
