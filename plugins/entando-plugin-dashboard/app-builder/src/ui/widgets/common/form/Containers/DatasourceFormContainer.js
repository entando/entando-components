import {connect} from "react-redux";

import {fetchDatasourceColumns} from "state/main/actions";

import {getDatasourceList} from "state/main/selectors";

import DatasourceForm from "ui/widgets/common/form/Components/DatasourceForm";

const mapStateToProps = state => ({
  datasources: getDatasourceList(state)
});

const mapDispatchToProps = dispatch => ({
  onChange: value => {
    dispatch(
      fetchDatasourceColumns("form-dashboard-table", "serverName", value)
    );
  }
});

const DatasourceFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DatasourceForm);

export default DatasourceFormContainer;
