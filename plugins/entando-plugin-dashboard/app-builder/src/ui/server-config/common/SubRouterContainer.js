
import { connect } from 'react-redux';

import { getPluginPage } from 'state/main/selectors';

import SubRouter from './SubRouter';

export const mapStateToProps = state => ({
  pluginPage: getPluginPage(state),
});


const SubRouterContainer = connect(mapStateToProps, null)(SubRouter);

export default SubRouterContainer;
