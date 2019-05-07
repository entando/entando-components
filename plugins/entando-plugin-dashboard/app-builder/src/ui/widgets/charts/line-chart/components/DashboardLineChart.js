import React from 'react';
import PropTypes from 'prop-types';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';
import DashboardLineChartFormContainer from 'ui/widgets/charts/line-chart/containers/DashboardLineChartFormContainer';

const DashboardLineChart = ({ onSubmit }) => (
  <DashboardWidgetConfigPage>
    <DashboardLineChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

DashboardLineChart.propTypes = {
  onSubmit: PropTypes.func,
};

DashboardLineChart.defaultProps = {
  onSubmit: {},
};


export default DashboardLineChart;
