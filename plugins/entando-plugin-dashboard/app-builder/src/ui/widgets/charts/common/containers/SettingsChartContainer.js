import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';

import { getDatasourceColumns } from 'state/main/selectors';

import SettingsChart from '../components/SettingsChart';


const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    datasourceSelected: selector(state, 'datasource'),
    optionColumns: getDatasourceColumns(state),
    axisY2Show: selector(state, 'axis.y2.show'),
    axisXType: selector(state, 'axis.x.type'),
    selectedColumnsY: selector(state, 'selectedColumnsY') || [],
    selectedColumnsY2: selector(state, 'selectedColumnsY2') || [],
    optionColumnXSelected: selector(state, 'columns.x') || [],
    optionColumnYSelected: selector(state, 'columns.y') || [],
    optionColumnY2Selected: selector(state, 'columns.y2') || [],
  };
};

const SettingsChartContainer = connect(
  mapStateToProps,
  null,
)(SettingsChart);

export default SettingsChartContainer;
