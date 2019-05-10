import React from 'react';
import PropType from 'prop-types';
import { Label } from 'patternfly-react';


const DashboardConfigDatasourceStatus = ({ status }) => (
  <div className="DashboardConfigDatasourceStatus">
    <Label bsStyle={status && status.status === 'online' ? 'success' : 'danger'}>
      {
        status && status.status === 'online' ? (<span className="fa fa-check" />) :
        <span className="fa fa-times" />
      }
    </Label>

  </div>
);

DashboardConfigDatasourceStatus.propTypes = {
  status: PropType.string,
};

DashboardConfigDatasourceStatus.defaultProps = {
  status: null,
};

export default DashboardConfigDatasourceStatus;
