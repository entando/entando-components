import React from "react";
import PropTypes from "prop-types";

import ConfigPageContainer from "ui/dashboard-config/list/containers/DashboardConfigPageContainer";
import DashboardConfigAddPage from "ui/dashboard-config/add/components/DashboardConfigAddPage";
import DashboardConfigEditPage from "ui/dashboard-config/add/components/DashboardConfigEditPage";

const SubRouter = ({pluginPage}) => {
  console.log("SubRouter pluginPage", pluginPage);
  switch (pluginPage) {
    case "add":
      return <DashboardConfigAddPage />;
    case "edit":
      return <DashboardConfigEditPage />;
    default:
      return <ConfigPageContainer />;
  }
};

SubRouter.propTypes = {
  pluginPage: PropTypes.string.isRequired
};

export default SubRouter;
