import { connect } from 'react-redux';

import { fecthDatasourceList } from 'state/main/actions';

import { getServerConfigList } from 'state/main/selectors';

import ServerNameForm from 'ui/widgets/common/form/components/ServerNameForm';

const mapStateToProps = state => ({
  serverNames: getServerConfigList(state),
});

const mapDispatchToProps = dispatch => ({
  onChange: (event, serverId) => {
    dispatch(fecthDatasourceList(serverId));
  },
});

const ServerNameFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(ServerNameForm);

export default ServerNameFormContainer;
