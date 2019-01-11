import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardTableFormContainer from "ui/widgets/dashboard-table/containers/DashboardTableFormContainer";

const DashboardLineChart = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardTableFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardLineChart;
