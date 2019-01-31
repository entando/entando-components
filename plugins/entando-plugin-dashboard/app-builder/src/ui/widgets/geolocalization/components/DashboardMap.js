import React from "react";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardMapFormContainer from "ui/widgets/geolocalization/containers/DashboardMapFormContainer";

const DashboardMap = ({onSubmit}) => (
  <DashboardWidgetConfigPage>
    <DashboardMapFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

export default DashboardMap;
