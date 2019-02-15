import React from "react";
import {Col} from "patternfly-react";

import DashboardWidgetTitleContainer from "ui/widgets/common/form/containers/DashboardWidgetTitleContainer";
import ServerNameFormContainer from "ui/widgets/common/form/containers/ServerNameFormContainer";

const MapFirstStepContent = () => {
  return (
    <div className="MapFirstStepContent">
      <Col xs={12} className="MapFirstStepContent__widget-title">
        <DashboardWidgetTitleContainer />
      </Col>

      <Col xs={12} className="MapFirstStepContent__container-server-name">
        <ServerNameFormContainer labelSize={2} />
      </Col>
    </div>
  );
};

export default MapFirstStepContent;
