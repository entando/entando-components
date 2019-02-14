import React from "react";
import PropType from "prop-types";
import {Label, Button} from "patternfly-react";

import FormattedMessageLocal from "ui/i18n/FormattedMessage";

const DashboardConfigDatasourceStatus = ({status, testConnection}) => (
  <div className="DashboardConfigDatasourceStatus">
    <Label bsStyle="default">{status}</Label>
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

DashboardConfigDatasourceStatus.PropType = {
  status: PropType.string,
  testConnection: PropType.func
};

DashboardConfigDatasourceStatus.defaultProps = {
  status: null,
  testConnection: null
};

export default DashboardConfigDatasourceStatus;
