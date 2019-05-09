import React from 'react';
import PropTypes from 'prop-types';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';
import DashboardMapFormContainer from 'ui/widgets/geolocalization/containers/DashboardMapFormContainer';

const DashboardMap = ({ onSubmit }) => (
  <DashboardWidgetConfigPage>
    <DashboardMapFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

DashboardMap.propTypes = {
  onSubmit: PropTypes.func,
};

DashboardMap.defaultProps = {
  onSubmit: null,
};

export default DashboardMap;
