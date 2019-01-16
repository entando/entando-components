import React, {Component} from "react";
import "patternfly/dist/js/patternfly-settings";
import "patternfly/dist/js/patternfly-settings-charts";
import c3 from "c3";

const {patternfly} = window;
const c3ChartDefaults = patternfly.c3ChartDefaults();

const CHART_CONFIG = {
  AREA_CHART: {
    type: "area",
    displayName: "AreaChart",
    className: "area-chart-pf",
    defaultConfig: c3ChartDefaults.getDefaultAreaConfig()
  },
  BAR_CHART: {
    type: "bar",
    displayName: "BarChart",
    className: "bar-chart-pf",
    defaultConfig: c3ChartDefaults.getDefaultBarConfig()
  },
  DONUT_CHART: {
    type: "donut",
    displayName: "DonutChart",
    className: "donut-chart-pf",
    defaultConfig: c3ChartDefaults.getDefaultDonutConfig()
  },
  LINE_CHART: {
    type: "line",
    displayName: "LineChart",
    className: "line-chart-pf",
    defaultConfig: c3ChartDefaults.getDefaultLineConfig()
  },
  PIE_CHART: {
    type: "pie",
    displayName: "PieChart",
    className: "pie-chart-pf",
    defaultConfig: c3ChartDefaults.getDefaultPieConfig()
  },
  SINGLE_AREA_CHART: {
    type: "area",
    displayName: "SingleAreaChart",
    className: "area-chart-pf",
    defaultConfig: c3ChartDefaults.getDefaultSingleAreaConfig()
  },
  SINGLE_LINE_CHART: {
    type: "line",
    displayName: "SingleLineChart",
    className: "line-chart-pf",
    defaultConfig: c3ChartDefaults.getDefaultSingleLineConfig()
  }
};

class Chart extends Component {
  componentDidMount() {
    // When the component mounts the first time we update
    // the chart.
    this.updateChart();
  }

  componentDidUpdate() {
    // When we receive a new prop then we update the chart again.
    this.updateChart();
  }

  updateChart() {
    const {config} = this.props;
    const {type, defaultConfig} = CHART_CONFIG[this.props.type];
    c3.generate({
      bindto: "#chart",
      data: {
        columns: this.props.columns,
        type: type
      },
      ...defaultConfig,
      ...config
    });
  }
  render() {
    const {className} = CHART_CONFIG[this.props.type];
    return <div id="chart" className={className} />;
  }
}
export default Chart;
