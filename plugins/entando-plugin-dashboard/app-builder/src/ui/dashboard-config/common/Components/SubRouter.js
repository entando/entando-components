import React from "react";
import PropTypes from "prop-types";

import ConfigPageContainer from "ui/dashboard-config/list/Containers/DashboardConfigPageContainer";
import DashboardConfigAddPage from "ui/dashboard-config/add/Components/DashboardConfigAddPage";
// import ServerConfigEditPage from "ui/dashboard-config/add/ServerConfigEditPage";

const SubRouter = ({pluginPage}) => {
  console.log("SubRouter pluginPage", pluginPage);
  switch (pluginPage) {
    case "add":
      return <DashboardConfigAddPage />;
    // case "edit":
    //   return <ServerConfigEditPage />;
    default:
      return <ConfigPageContainer />;
  }
};

SubRouter.propTypes = {
  pluginPage: PropTypes.string.isRequired
};

export default SubRouter;
