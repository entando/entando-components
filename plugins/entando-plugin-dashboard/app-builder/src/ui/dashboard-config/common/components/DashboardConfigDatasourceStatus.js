import React from 'react';
import PropType from 'prop-types';
import { Label, Button } from 'patternfly-react';

import FormattedMessageLocal from 'ui/i18n/FormattedMessage';

const DashboardConfigDatasourceStatus = ({ datasourceCode, status, testConnection }) => (
  <div className="DashboardConfigDatasourceStatus">
    <Label bsStyle={status === 'online' ? 'success' : 'danger'}>
      {
        status === 'online' ? (<span className="fa fa-check" />) :
        <span className="fa fa-times" />
      }
    </Label>
    <Button
      className="DashboardConfigDatasourceStatus__test-btn"
      type="button"
      bsStyle="default"
      onClick={() => testConnection(datasourceCode)}
    >
      <FormattedMessageLocal id="plugin.config.testConnection" />
    </Button>
  </div>
);

DashboardConfigDatasourceStatus.propTypes = {
  status: PropType.string,
  testConnection: PropType.func,
  datasourceCode: PropType.string.isRequired,
};

DashboardConfigDatasourceStatus.defaultProps = {
  status: null,
  testConnection: null,
};

export default DashboardConfigDatasourceStatus;
