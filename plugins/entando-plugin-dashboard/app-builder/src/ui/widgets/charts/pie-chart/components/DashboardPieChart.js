import React from 'react';
import PropTypes from 'prop-types';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';
import DashboardPieChartFormContainer from 'ui/widgets/charts/pie-chart/containers/DashboardPieChartFormContainer';

const DashboardPieChart = ({ onSubmit }) => (
  <DashboardWidgetConfigPage>
    <DashboardPieChartFormContainer onSubmit={onSubmit} />
  </DashboardWidgetConfigPage>
);

DashboardPieChart.propTypes = {
  onSubmit: PropTypes.func,
};

DashboardPieChart.defaultProps = {
  onSubmit: () => {},
};

export default DashboardPieChart;
