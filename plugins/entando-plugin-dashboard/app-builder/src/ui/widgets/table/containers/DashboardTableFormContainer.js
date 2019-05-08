import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';

import {
  fetchServerConfigList,
  getWidgetConfig,
  gotoConfigurationPage,
} from 'state/main/actions';

import { getDatasourceColumns } from 'state/main/selectors';

import DashboardTableForm from 'ui/widgets/table/components/DashboardTableForm';

const selector = formValueSelector('form-dashboard-table');

export const mapStateToProps = state => ({
  initialValues: {
    allColumns: true,
    options: {
      downlodable: true,
      filtrable: true,
    },
  },
  datasource: selector(state, 'datasource'),
  columns: getDatasourceColumns(state),
});

export const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList()).then(() => {
      dispatch(getWidgetConfig('form-dashboard-table'));
    });
  },
  onSubmit: (data) => {
    ownProps.onSubmit({ config: JSON.stringify(data) });
  },
  onCancel: () => dispatch(gotoConfigurationPage()),
});

// salvataggio delle colonne con l'ordinamento
const mergeProps = (stateProps, dispatchProps) => ({
  ...stateProps,
  ...dispatchProps,
  onSubmit: (data) => {
    const { columns } = data;
    stateProps.columns.forEach((col, index) => {
      const { key } = col;
      columns[key].order = index;
    });
    const obj = { ...data, columns };
    dispatchProps.onSubmit(obj);
  },
});

const DashboardTableFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
  mergeProps,
)(DashboardTableForm);

export default DashboardTableFormContainer;
