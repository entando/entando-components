import React, {Component} from "react";
import FormattedMessage from "ui/i18n/FormattedMessage";
import {
  TabContainer,
  Nav,
  NavItem,
  TabContent,
  TabPane
} from "patternfly-react";

import InfoContainer from "ui/widgets/table-list-devices/Containers/InfoContainer";
import TableListDevicesFormContainer from "ui/widgets/table-list-devices/Containers/TableListDevicesFormContainer";

class TableListDevices extends Component {
  render() {
    return (
      <div className="TableListDevices">
        <TabContainer
          id="basic-tabs"
          className="TableListDevices__tabs-container"
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
            <TabContent className="TableListDevices__tab-content">
              <TabPane eventKey={1}>
                <TableListDevicesFormContainer />
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

export default TableListDevices;
