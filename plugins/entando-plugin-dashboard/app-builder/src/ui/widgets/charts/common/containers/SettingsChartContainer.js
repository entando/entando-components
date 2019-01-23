import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsChart from "../components/SettingsChart";

import {getDatasourceColumns} from "state/main/selectors";

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    optionColumns: getDatasourceColumns(state),
    axisY2Show: selector(state, "axis.y2.show"),
    axisXType: selector(state, "axis.x.type"),
    selectedColumnsY: selector(state, "selectedColumnsY") || [],
    selectedColumnsY2: selector(state, "selectedColumnsY2") || [],
    optionColumnYSelected: selector(state, "columns.y") || [],
    optionColumnY2Selected: selector(state, "columns.y2") || []
  };
};

const SettingsChartContainer = connect(
  mapStateToProps,
  null
)(SettingsChart);

export default SettingsChartContainer;
