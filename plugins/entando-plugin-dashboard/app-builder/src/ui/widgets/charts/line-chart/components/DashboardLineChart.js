import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardLineChartFormContainer from "ui/widgets/charts/line-chart/containers/DashboardLineChartFormContainer";

const DashboardLineChart = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardLineChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardLineChart;
