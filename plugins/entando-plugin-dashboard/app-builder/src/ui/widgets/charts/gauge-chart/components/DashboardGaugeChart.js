import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardGaugeChartFormContainer from "ui/widgets/charts/gauge-chart/containers/DashboardGaugeChartFormContainer";

const DashboardGaugeChart = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardGaugeChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardGaugeChart;
