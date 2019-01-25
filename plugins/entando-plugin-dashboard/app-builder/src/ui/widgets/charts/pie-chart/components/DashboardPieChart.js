import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardPieChartFormContainer from "ui/widgets/charts/pie-chart/containers/DashboardPieChartFormContainer";

const DashboardPieChart = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardPieChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardPieChart;
