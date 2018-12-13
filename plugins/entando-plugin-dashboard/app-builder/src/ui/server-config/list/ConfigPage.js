import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Row, Col, Grid, CardGrid, Button } from 'patternfly-react';

import ServerConfigCard from 'ui/server-config/list/ServerConfigCard';
import FormattedMessage from 'ui/i18n/FormattedMessage';
import PageTitle from 'ui/PageTitle';

class ConfigPage extends Component {
  componentWillMount() {
    const { onWillMount } = this.props;
    if (onWillMount) {
      onWillMount(this.props);
    }
  }

  render() {
    const {
      configList, removeConfigItem, testConfigItem, testAllConfigItems,
      connectionOutcomes, gotoPluginPage, editConfigItem,
    } = this.props;
    if (!configList) {
      return <span />;
    }
    return (
      <Grid fluid>
        <Row>
          <Col xs={12}>
            <PageTitle titleId="plugin.title" helpId="ConfigPage.help" />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <Button bsStyle="warning" onClick={testAllConfigItems}>
              <FormattedMessage id="ConfigPage.testAll" />
            </Button>
            <Button bsStyle="primary" className="pull-right" onClick={() => gotoPluginPage('add')}>
              <FormattedMessage id="ConfigPage.addConfig" />
            </Button>
          </Col>
        </Row>
        <div className="cards-pf">
          <CardGrid matchHeight>
            <Row style={{ marginBottom: '20px', marginTop: '20px' }}>
              { configList.map(configItem => (
                <ServerConfigCard
                  key={configItem.id}
                  configItem={configItem}
                  testConnectionOutcome={connectionOutcomes[configItem.id]}
                  onClickRemove={() => removeConfigItem(configItem.id)}
                  onClickTest={() => testConfigItem(configItem)}
                  onClickEdit={() => editConfigItem(configItem)}
                />
              )) }
            </Row>
          </CardGrid>
        </div>
      </Grid>
    );
  }
}

ConfigPage.propTypes = {
  onWillMount: PropTypes.func.isRequired,
  removeConfigItem: PropTypes.func.isRequired,
  testConfigItem: PropTypes.func.isRequired,
  testAllConfigItems: PropTypes.func.isRequired,
  editConfigItem: PropTypes.func.isRequired,
  gotoPluginPage: PropTypes.func.isRequired,
  configList: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  connectionOutcomes: PropTypes.objectOf(PropTypes.string).isRequired,
};

export default ConfigPage;
