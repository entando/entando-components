import React from 'react';
import PropTypes from 'prop-types';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';
import DashboardBarChartFormContainer from 'ui/widgets/charts/bar-chart/containers/DashboardBarChartFormContainer';

const DashboardBarChart = ({ onSubmit }) => (
  <DashboardWidgetConfigPage>
    <DashboardBarChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);
DashboardBarChart.propTypes = {
  onSubmit: PropTypes.func,
};
DashboardBarChart.defaultProps = {
  onSubmit: () => {},
};


export default DashboardBarChart;
