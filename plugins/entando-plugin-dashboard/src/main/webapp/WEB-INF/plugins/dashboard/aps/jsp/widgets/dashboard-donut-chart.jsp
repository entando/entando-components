<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

<!-- Load c3.css -->
  <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.css">
<!-- Load d3.js and c3.js -->
<script src="https://d3js.org/d3.v5.min.js"></script>
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.min.js"></script>

<link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/charts/donut/donut.css">
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/charts/donut/donut.js"></script>

<wp:currentWidget param="config" configParam="config" var="configDonutChart" />

<script>
$(document).ready(() => {
  const JSON_DATA =[
    {
      timestamp: "2019-01-01 09:10:00",
      timestamp1: "2019-01-11 08:00:00",
      temperature: 300,
      temperature1: 150
    },
    {
      timestamp: "2019-01-03 09:20:00",
      timestamp1: "2019-01-12 09:30:00",
      temperature: 200,
      temperature1: 175
    },
    {
      timestamp: "2019-01-07 09:30:00",
      timestamp1: "2019-01-20 10:30:00",
      temperature: 100,
      temperature1: 50
    }
  ]

  console.log('jQuery ready Donut chart');
  const config = ${configDonutChart};
  console.log('Config Donut chart :', config);
  $('#title-donut-chart').html(config.title.en);
  const donutChart = new org.entando.dashboard.DonutChart("#donut-chart", config);
  donutChart.update(JSON_DATA);

});
</script>

<div id="dashboard-donut-chart" class="container-fluid">
  <h2 id="title-donut-chart"></h2>
  <div id="donut-chart"></div>
</div>
