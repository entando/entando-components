import React from "react";
import {Label, Button} from "patternfly-react";

import FormattedMessageLocal from "ui/i18n/FormattedMessage";

const DashboardConfigDatasourceStatus = ({status, testConnection}) => (
  <div className="DashboardConfigDatasourceStatus">
    <Label bsStyle="success">OK</Label>
    <Button
      className="DashboardConfigDatasourceStatus__test-btn"
      type="button"
      bsStyle="default"
      onClick={testConnection}
    >
      <FormattedMessageLocal id="plugin.config.testConnection" />
    </Button>
  </div>
);

export default DashboardConfigDatasourceStatus;
