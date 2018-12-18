import {connect} from "react-redux";

import {updateDatasourceColumns} from "state/main/actions";

import {getDatasourceColumns} from "state/main/selectors";

import DashboardTableColumns from "ui/widgets/dashboard-table/Components/DashboardTableColumns";

const mapStateToProps = state => ({
  columns: getDatasourceColumns(state)
});

const mapDispatchToProps = dispatch => ({
  onMoveColumn: columns =>
    dispatch(updateDatasourceColumns("form-dashboard-table", columns))
});
const DashboardTableColumnsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardTableColumns);

export default DashboardTableColumnsContainer;
