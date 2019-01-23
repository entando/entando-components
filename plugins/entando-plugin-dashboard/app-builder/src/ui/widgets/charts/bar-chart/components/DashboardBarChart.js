import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardBarChartFormContainer from "ui/widgets/charts/bar-chart/containers/DashboardBarChartFormContainer";

const DashboardBarChart = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardBarChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardBarChart;
