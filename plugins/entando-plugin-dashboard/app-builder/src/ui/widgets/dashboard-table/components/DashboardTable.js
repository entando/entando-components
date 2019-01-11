import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardTableFormContainer from "ui/widgets/dashboard-table/containers/DashboardTableFormContainer";

const DashboardTable = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardTableFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardTable;
