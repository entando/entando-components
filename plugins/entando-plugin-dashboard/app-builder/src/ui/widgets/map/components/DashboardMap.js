import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardMapFormContainer from "ui/widgets/map/containers/DashboardMapFormContainer";

const DashboardMap = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardMapFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardMap;
