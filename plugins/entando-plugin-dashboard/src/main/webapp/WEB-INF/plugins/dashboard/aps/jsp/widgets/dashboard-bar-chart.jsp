<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core"%>

<!-- Load c3.css -->
  <link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.css">
<!-- Load d3.js and c3.js -->
<script src="https://d3js.org/d3.v5.min.js"></script>
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/library/c3-0.6.12/c3.min.js"></script>

<link rel="stylesheet" href="<wp:resourceURL />plugins/dashboard/static/css/widgets/charts/bar/bar.css">
<script type="text/javascript" src="<wp:resourceURL />plugins/dashboard/static/js/widgets/charts/bar/bar.js"></script>

<wp:currentWidget param="config" configParam="config" var="configBarChart" />

<script>
$(document).ready(() => {
  console.log('jQuery ready Bar chart');
  const config = ${configBarChart};
  console.log('Config Bar chart :', config);
  $('#title-bar-chart').html(config.title.en);
  const barChart = new org.entando.dashboard.BarChart("#bar-chart", config);
  const url = "<wp:info key="systemParam" paramName="applicationBaseURL"/>api/plugins/dashboard/server/"+config['serverName']+"/datasource/"+config['datasource']+"/data";
  $.ajax({
    url,
    beforeSend: (xhr)=> {
      const accessToken = '<c:out value="${sessionScope.currentUser.accessToken}"/>';
      if (accessToken) {
          xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
      }
    }
  }).done((json)=>{
    barChart.update(json.payload);
  });

});
</script>

<div id="dashboard-bar-chart" class="container-fluid">
  <h2 id="title-bar-chart"></h2>
  <div id="bar-chart"></div>
</div>
