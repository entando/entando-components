import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { FormattedMessage } from 'react-intl';
import { Row, Col, Grid, Breadcrumb } from 'patternfly-react';

import FormattedMessageLocal from 'ui/i18n/FormattedMessage';
import ServerConfigFormContainer from 'ui/server-config/common/ServerConfigFormContainer';
import PageTitle from 'ui/PageTitle';

class ServerConfigEditPage extends Component {
  componentWillMount() {
    const { onWillMount } = this.props;
    if (onWillMount) {
      onWillMount(this.props);
    }
  }

  render() {
    return (
      <Grid fluid>
        <Row>
          <Col xs={12}>
            <Breadcrumb>
              <Breadcrumb.Item>
                <FormattedMessage id="menu.integration" />
              </Breadcrumb.Item>
              <Breadcrumb.Item>
                <FormattedMessage id="menu.components" />
              </Breadcrumb.Item>
              <Breadcrumb.Item>
                <FormattedMessageLocal id="plugin.title" />
              </Breadcrumb.Item>
              <Breadcrumb.Item active>
                <FormattedMessageLocal id="plugin.bpmSetting" />
              </Breadcrumb.Item>
            </Breadcrumb>
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <PageTitle titleId="plugin.title" helpId="ConfigPage.help" />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <ServerConfigFormContainer mode="edit" />
          </Col>
        </Row>
      </Grid>
    );
  }
}

ServerConfigEditPage.propTypes = {
  onWillMount: PropTypes.func,
};

ServerConfigEditPage.defaultProps = {
  onWillMount: null,
};


export default ServerConfigEditPage;
