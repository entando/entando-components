import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsLineChart from "../components/SettingsLineChart";

import {getDatasourceColumns} from "state/main/selectors";

const selector = formValueSelector("form-dashboard-line-chart");

const mapStateToProps = state => ({
  optionColumns: getDatasourceColumns(state),
  axisY2Show: selector(state, "axis.y2.show"),
  axisXType: selector(state, "axis.x.type"),
  selectedColumnsY: selector(state, "selectedColumnsY") || [],
  selectedColumnsY2: selector(state, "selectedColumnsY2") || [],
  optionColumnYSelected: selector(state, "columns.y") || [],
  optionColumnY2Selected: selector(state, "columns.y2") || []
});

const SettingsLineChartContainer = connect(
  mapStateToProps,
  null
)(SettingsLineChart);

export default SettingsLineChartContainer;
