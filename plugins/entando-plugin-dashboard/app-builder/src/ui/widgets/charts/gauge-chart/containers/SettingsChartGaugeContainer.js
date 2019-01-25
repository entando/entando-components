import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import SettingsChartGauge from "../components/SettingsChartGauge";

import {getDatasourceColumns} from "state/main/selectors";

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    optionColumns: getDatasourceColumns(state),
    optionColumnSelected: selector(state, "columns") || []
  };
};

const SettingsChartGaugeContainer = connect(
  mapStateToProps,
  null
)(SettingsChartGauge);

export default SettingsChartGaugeContainer;
