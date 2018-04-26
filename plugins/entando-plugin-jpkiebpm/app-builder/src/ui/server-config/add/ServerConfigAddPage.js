import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { FormattedMessage } from 'react-intl';
import { Row, Col, Grid, Breadcrumb } from 'patternfly-react';
import { BreadcrumbItem } from 'frontend-common-components';

import FormattedMessageLocal from 'ui/i18n/FormattedMessage';
import ServerConfigFormContainer from 'ui/server-config/common/ServerConfigFormContainer';
import PageTitle from 'ui/PageTitle';

class ServerConfigAddPage extends Component {
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
              <BreadcrumbItem>
                <FormattedMessage id="menu.integration" />
              </BreadcrumbItem>
              <BreadcrumbItem>
                <FormattedMessage id="menu.components" />
              </BreadcrumbItem>
              <BreadcrumbItem>
                <FormattedMessageLocal id="plugin.title" />
              </BreadcrumbItem>
              <BreadcrumbItem active>
                <FormattedMessageLocal id="plugin.bpmSetting" />
              </BreadcrumbItem>
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
            <ServerConfigFormContainer mode="add" />
          </Col>
        </Row>
      </Grid>
    );
  }
}

ServerConfigAddPage.propTypes = {
  onWillMount: PropTypes.func,
};

ServerConfigAddPage.defaultProps = {
  onWillMount: null,
};

export default ServerConfigAddPage;
