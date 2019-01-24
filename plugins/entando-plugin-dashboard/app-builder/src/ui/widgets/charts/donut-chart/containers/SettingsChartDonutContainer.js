import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsChartDonut from "../components/SettingsChartDonut";

import {getDatasourceColumns} from "state/main/selectors";

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    optionColumns: getDatasourceColumns(state),
    optionColumnSelected: selector(state, "columns") || []
  };
};

const SettingsChartDonutContainer = connect(
  mapStateToProps,
  null
)(SettingsChartDonut);

export default SettingsChartDonutContainer;
