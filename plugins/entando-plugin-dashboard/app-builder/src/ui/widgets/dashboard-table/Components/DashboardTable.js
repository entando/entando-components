import React, {Component} from "react";
import FormattedMessage from "ui/i18n/FormattedMessage";
import {
  TabContainer,
  Nav,
  NavItem,
  TabContent,
  TabPane
} from "patternfly-react";

import InfoContainer from "ui/widgets/dashboard-table/Containers/InfoContainer";
import DashboardTableFormContainer from "ui/widgets/dashboard-table/Containers/DashboardTableFormContainer";

class DashboardTable extends Component {
  render() {
    return (
      <div className="DashboardTable">
        <TabContainer
          id={1}
          className="DashboardTable__tabs-container"
          defaultActiveKey={1}
        >
          <div>
            <Nav bsClass="nav nav-tabs">
              <NavItem eventKey={1}>
                <FormattedMessage id="plugin.settings" />
              </NavItem>
              <NavItem eventKey={2} className="pull-right">
                <FormattedMessage id="plugin.info" />
              </NavItem>
            </Nav>
            <TabContent className="DashboardTable__tab-content">
              <TabPane eventKey={1}>
                <DashboardTableFormContainer />
              </TabPane>
              <TabPane eventKey={2}>
                <InfoContainer />
              </TabPane>
            </TabContent>
          </div>
        </TabContainer>
      </div>
    );
  }
}

export default DashboardTable;
