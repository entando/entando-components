import {connect} from "react-redux";

import {fetchDatasourceColumns} from "state/main/actions";

import {getDatasourceList} from "state/main/selectors";

import DatasourceForm from "ui/widgets/common/form/components/DatasourceForm";

const mapStateToProps = state => ({
  datasources: getDatasourceList(state)
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onChange: value => {
    dispatch(fetchDatasourceColumns(ownProps.formName, "serverName", value));
  }
});

const DatasourceFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DatasourceForm);

export default DatasourceFormContainer;
