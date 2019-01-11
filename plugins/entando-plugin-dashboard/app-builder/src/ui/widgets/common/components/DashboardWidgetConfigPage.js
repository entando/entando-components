import React, {Component} from "react";
import PropTypes from "prop-types";
import FormattedMessage from "ui/i18n/FormattedMessage";
import {
  TabContainer,
  Nav,
  NavItem,
  TabContent,
  TabPane
} from "patternfly-react";

import InfoContainer from "ui/widgets/common/containers/InfoContainer";

class DashboardWidgetConfigPage extends Component {
  render() {
    return (
      <div className="DashboardWidgetConfigPage">
        <TabContainer
          id={1}
          className="DashboardWidgetConfigPage__tabs-container"
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
            <TabContent className="DashboardWidgetConfigPage__tab-content">
              <TabPane eventKey={1}>{this.props.children}</TabPane>
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

DashboardWidgetConfigPage.propTypes = {
  children: PropTypes.node.isRequired
};

export default DashboardWidgetConfigPage;
