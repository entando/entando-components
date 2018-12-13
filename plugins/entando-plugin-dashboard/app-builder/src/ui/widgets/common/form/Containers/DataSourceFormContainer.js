import {connect} from "react-redux";

import {fecthContextList} from "state/main/actions";

import {getServerConfigList} from "state/main/selectors";

import DataSourceForm from "ui/widgets/common/form/Components/DataSourceForm";

const mapStateToProps = state => ({
  datasources: getServerConfigList(state)
});

const mapDispatchToProps = dispatch => ({
  onChange: (event, newValue) => {
    dispatch(fecthContextList(newValue));
  }
});

const DataSourceFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DataSourceForm);

export default DataSourceFormContainer;
