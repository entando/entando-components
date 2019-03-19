import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsChartPie from "../components/SettingsChartPie";

import {getDatasourceColumns} from "state/main/selectors";

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    datasourceSelected: selector(state, "datasource"),
    optionColumns: getDatasourceColumns(state),
    optionColumnXSelected: selector(state, "columns.x") || [],
    optionColumnYSelected: selector(state, "columns.y") || []
  };
};

const SettingsChartPieContainer = connect(
  mapStateToProps,
  null
)(SettingsChartPie);

export default SettingsChartPieContainer;
