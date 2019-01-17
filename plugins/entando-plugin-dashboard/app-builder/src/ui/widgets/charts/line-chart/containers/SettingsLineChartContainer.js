import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsLineChart from "../components/SettingsLineChart";

const selector = formValueSelector("form-dashboard-line-chart");

const mapStateToProps = state => ({
  axisY2Show: selector(state, "axis.y2.show"),
  axisXType: selector(state, "axis.x.type"),
  selectedColumns: selector(state, "selectedColumns") || []
});

const SettingsLineChartContainer = connect(
  mapStateToProps,
  null
)(SettingsLineChart);

export default SettingsLineChartContainer;
