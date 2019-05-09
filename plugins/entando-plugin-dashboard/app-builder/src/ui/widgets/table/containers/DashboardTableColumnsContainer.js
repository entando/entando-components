import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';

import { updateDatasourceColumns } from 'state/main/actions';

import { getDatasourceColumns } from 'state/main/selectors';

import DashboardTableColumns from 'ui/widgets/table/components/DashboardTableColumns';

const selector = formValueSelector('form-dashboard-table');

export const mapStateToProps = state => ({
  columns: getDatasourceColumns(state),
  fieldColumnData: selector(state, 'columns'),
});

export const mapDispatchToProps = dispatch => ({
  onMoveColumn: columns =>
    dispatch(updateDatasourceColumns(columns)),
});
const DashboardTableColumnsContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(DashboardTableColumns);

export default DashboardTableColumnsContainer;
