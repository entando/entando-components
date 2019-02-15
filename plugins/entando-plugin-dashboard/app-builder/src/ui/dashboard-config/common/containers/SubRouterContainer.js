import {connect} from "react-redux";

import {getPluginPage, getInternalRoute} from "state/main/selectors";

import SubRouter from "../components/SubRouter";

export const mapStateToProps = state => {
  let pluginPage = getPluginPage(state);
  const internalRoute = getInternalRoute(state);

  if (pluginPage === "" && internalRoute !== "") {
    pluginPage = internalRoute;
  }

  return {
    pluginPage
  };
};

const SubRouterContainer = connect(
  mapStateToProps,
  null
)(SubRouter);

export default SubRouterContainer;
