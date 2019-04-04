import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsChartDonut from "../components/SettingsChartDonut";

import {getDatasourceColumns} from "state/main/selectors";

export const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    datasourceSelected: selector(state, "datasource"),
    optionColumns: getDatasourceColumns(state),
    optionColumnXSelected: selector(state, "columns.x") || [],
    optionColumnYSelected: selector(state, "columns.y") || []
  };
};

const SettingsChartDonutContainer = connect(
  mapStateToProps,
  null
)(SettingsChartDonut);

export default SettingsChartDonutContainer;
