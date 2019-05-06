import React, { Component } from 'react';
import PropTypes from 'prop-types';
import 'patternfly/dist/js/patternfly-settings';
import 'patternfly/dist/js/patternfly-settings-charts';
import c3 from 'c3';
import { isEqual } from 'lodash';

const { patternfly } = window;
const c3ChartDefaults = patternfly.c3ChartDefaults();

const CHART_CONFIG = {
  AREA_CHART: {
    type: 'area',
    displayName: 'AreaChart',
    className: 'area-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultAreaConfig(),
  },
  BAR_CHART: {
    type: 'bar',
    displayName: 'BarChart',
    className: 'bar-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultBarConfig(),
  },
  DONUT_CHART: {
    type: 'donut',
    displayName: 'DonutChart',
    className: 'donut-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultDonutConfig(),
  },
  GAUGE_CHART: {
    type: 'gauge',
    displayName: 'GauceChart',
    className: 'gauge-chart-pf',
    defaultConfig: {
      ...c3ChartDefaults.getDefaultDonutConfig(),
      gauge: { width: 50 },
    },
  },
  LINE_CHART: {
    type: 'line',
    displayName: 'LineChart',
    className: 'line-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultLineConfig(),
  },
  SPLINE_CHART: {
    type: 'spline',
    displayName: 'LineChart',
    className: 'line-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultLineConfig(),
  },
  PIE_CHART: {
    type: 'pie',
    displayName: 'PieChart',
    className: 'pie-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultPieConfig(),
  },
  SINGLE_AREA_CHART: {
    type: 'area',
    displayName: 'SingleAreaChart',
    className: 'area-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultSingleAreaConfig(),
  },
  SINGLE_LINE_CHART: {
    type: 'line',
    displayName: 'SingleLineChart',
    className: 'line-chart-pf',
    defaultConfig: c3ChartDefaults.getDefaultSingleLineConfig(),
  },
};

class Chart extends Component {
  componentDidMount() {
    // When the component mounts the first time we update the chart.
    this.updateChart();
  }

  componentDidUpdate(prevProps) {
    // When we receive a new prop then we update the chart again.
    if (!isEqual(prevProps, this.props)) {
      this.updateChart();
    }
  }

  updateChart() {
    const { idChart, config, columns } = this.props;
    const { type, defaultConfig } = CHART_CONFIG[this.props.type];
    c3.generate({
      bindto: `#${idChart}`,
      data: {
        columns,
        type,
      },
      ...defaultConfig,
      ...config,
    });
  }
  render() {
    const { className } = CHART_CONFIG[this.props.type];
    const { idChart } = this.props;
    return <div id={idChart} className={className} />;
  }
}
Chart.propTypes = {
  idChart: PropTypes.string,
  config: PropTypes.shape({}),
  columns: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.number,
  ]))),
  type: PropTypes.string.isRequired,
};
Chart.defaultProps = {
  idChart: 'chart',
  config: {},
  columns: [],

};
export default Chart;
