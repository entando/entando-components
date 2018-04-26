
import { connect } from 'react-redux';
import { initialize } from 'redux-form';

import { getServerConfigList, getConnectionOutcomes } from 'state/main/selectors';
import {
  fetchServerConfigList, removeServerConfig, testServerConfig, testAllServerConfigs,
  gotoPluginPage,
} from 'state/main/actions';

import ConfigPage from './ConfigPage';


export const mapStateToProps = state => ({
  configList: getServerConfigList(state),
  connectionOutcomes: getConnectionOutcomes(state) || {},
});

export const mapDispatchToProps = dispatch => ({
  onWillMount: () => dispatch(fetchServerConfigList()),
  removeConfigItem: id => dispatch(removeServerConfig(id)),
  testConfigItem: configItem => dispatch(testServerConfig(configItem)),
  editConfigItem: (configItem) => {
    dispatch(gotoPluginPage('edit'));
    dispatch(initialize('jpkiebpm_serverConfig', configItem));
  },
  testAllConfigItems: () => dispatch(testAllServerConfigs()),
  gotoPluginPage: page => dispatch(gotoPluginPage(page)),
});


const ConfigPageContainer = connect(mapStateToProps, mapDispatchToProps)(ConfigPage);

export default ConfigPageContainer;
