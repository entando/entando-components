import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsChartPie from "../components/SettingsChartPie";

import {getDatasourceColumns} from "state/main/selectors";

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    optionColumns: getDatasourceColumns(state),
    optionColumnSelected: selector(state, "columns") || []
  };
};

const SettingsChartPieContainer = connect(
  mapStateToProps,
  null
)(SettingsChartPie);

export default SettingsChartPieContainer;
