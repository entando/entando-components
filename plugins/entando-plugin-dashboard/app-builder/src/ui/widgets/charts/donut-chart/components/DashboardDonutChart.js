import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardDonutChartFormContainer from "ui/widgets/charts/donut-chart/containers/DashboardDonutChartFormContainer";

const DashboardDonutChart = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardDonutChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardDonutChart;
