import React from 'react';
import PropTypes from 'prop-types';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';
import DashboardDonutChartFormContainer from 'ui/widgets/charts/donut-chart/containers/DashboardDonutChartFormContainer';

const DashboardDonutChart = ({ onSubmit }) => (
  <DashboardWidgetConfigPage>
    <DashboardDonutChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

DashboardDonutChart.propTypes = {
  onSubmit: PropTypes.func,
};

DashboardDonutChart.defaultProps = {
  onSubmit: () => {},
};
export default DashboardDonutChart;
