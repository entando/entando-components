import React from 'react';
import PropTypes from 'prop-types';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';
import DashboardTableFormContainer from 'ui/widgets/table/containers/DashboardTableFormContainer';

const DashboardTable = ({ onSubmit }) => (
  <DashboardWidgetConfigPage>
    <DashboardTableFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

DashboardTable.propTypes = {
  onSubmit: PropTypes.func.isRequired,
};

export default DashboardTable;
