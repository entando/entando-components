import React from 'react';
import PropTypes from 'prop-types';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';
import DashboardGaugeChartFormContainer from 'ui/widgets/charts/gauge-chart/containers/DashboardGaugeChartFormContainer';

const DashboardGaugeChart = ({ onSubmit }) => (
  <DashboardWidgetConfigPage>
    <DashboardGaugeChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

DashboardGaugeChart.propTypes = {
  onSubmit: PropTypes.func,
};

DashboardGaugeChart.defaultProps = {
  onSubmit: () => {},
};

export default DashboardGaugeChart;
