<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <%@ taglib prefix="wp" uri="/aps-core"%>

    <!-- Load c3.css -->
    <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.css">
      <!-- Load d3.js and c3.js -->
      <script src="https://d3js.org/d3.v5.min.js"></script>
      <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.min.js"></script>

      <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/charts/donut/donut.css">
        <script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/charts/donut/donut.js"></script>

        <wp:currentWidget param="config" configParam="config" var="configDonutChart"/>

        <script>
          $(document).ready(() => {
            console.log('jQuery ready Donut chart');
            const config = $ {
              configDonutChart
            };
            console.log('Config Donut chart :', config);
            $('#title-donut-chart').html(config.title.en);
            const url = "<wp:info key=" systemParam " paramName=" applicationBaseURL "/>api/plugins/dashboard/server/" + config['serverName'] + "/datasource/" + config['datasource'] + "/data";
            const donutChart = new org.entando.dashboard.DonutChart("#donut-chart", config);
            $.ajax({
              url,
              beforeSend: (xhr) => {
                const accessToken = '<c:out value="${sessionScope.currentUser.accessToken}"/>';
                if (accessToken) {
                  xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
                }
              }
            }).done((json) => {
              donutChart.update(json.payload);
            }).fail((xhr) => {
              const str = "status:" + xhr.status + " text:" + xhr.statusText + " error: " + xhr.responseJSON.errors[0].message;
              alert(str);
            });
          });
        </script>

        <div id="dashboard-donut-chart" class="container-fluid">
          <h2 id="title-donut-chart"></h2>
          <div id="donut-chart"></div>
        </div>
