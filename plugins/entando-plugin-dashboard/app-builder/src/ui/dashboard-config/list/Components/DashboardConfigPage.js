import React, {Component} from "react";
import PropTypes from "prop-types";
import {Row, Col, Grid, CardGrid, Button} from "patternfly-react";

import FormattedMessageLocal from "ui/i18n/FormattedMessage";
import PageTitle from "ui/PageTitle";
import ServerConfigCard from "ui/dashboard-config/list/Components/ServerConfigCard";

class DashboardConfigPage extends Component {
  componentWillMount() {
    const {onWillMount} = this.props;
    if (onWillMount) {
      onWillMount(this.props);
    }
  }
  render() {
    const {
      serverList,
      removeConfigItem,
      testConfigItem,
      testAllConfigItems,
      connectionOutcomes,
      gotoPluginPage,
      editConfigItem
    } = this.props;
    return (
      <Grid fluid className="DashboardConfigPage">
        <Row>
          <Col xs={12}>
            <PageTitle titleId="plugin.title" helpId="ConfigPage.help" />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <Button
              bsStyle="warning"
              className="DashboardConfigPage__btn-tests pull-right"
              onClick={testAllConfigItems}
            >
              <FormattedMessageLocal id="plugin.config.testServers" />
            </Button>
            <Button
              bsStyle="primary"
              className="DashboardConfigPage__btn-add pull-right"
              onClick={() => gotoPluginPage("add")}
            >
              <FormattedMessageLocal id="plugin.config.addServer" />
            </Button>
          </Col>
        </Row>
        <div className="cards-pf">
          <CardGrid matchHeight>
            <Row style={{marginBottom: "20px", marginTop: "20px"}}>
              {serverList.map(configItem => (
                <ServerConfigCard
                  key={configItem.id}
                  configItem={configItem}
                  testConnectionOutcome={connectionOutcomes[configItem.id]}
                  onClickRemove={() => removeConfigItem(configItem.id)}
                  onClickTest={() => testConfigItem(configItem)}
                  onClickEdit={() => editConfigItem(configItem)}
                />
              ))}
            </Row>
          </CardGrid>
        </div>
      </Grid>
    );
  }
}

DashboardConfigPage.propTypes = {
  onWillMount: PropTypes.func.isRequired,
  removeConfigItem: PropTypes.func.isRequired,
  testConfigItem: PropTypes.func.isRequired,
  testAllConfigItems: PropTypes.func.isRequired,
  editConfigItem: PropTypes.func.isRequired,
  gotoPluginPage: PropTypes.func.isRequired,
  serverList: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  connectionOutcomes: PropTypes.objectOf(PropTypes.string).isRequired
};
DashboardConfigPage.defaultProps = {
  connectionOutcomes: {}
};
export default DashboardConfigPage;
