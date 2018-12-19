import React, {Component} from "react";
import PropTypes from "prop-types";
import {Grid, Row, Col} from "patternfly-react";

import FormattedMessage from "ui/i18n/FormattedMessage";

const row = (idMessage, label) => (
  <div className="Info__table-row">
    <div className="Info__table-cell">
      <FormattedMessage id={idMessage} />
    </div>
    <div className="Info__table-cell">
      <label>{label}</label>
    </div>
  </div>
);

class Info extends Component {
  componentWillMount() {
    this.props.onWillMount();
  }
  render() {
    const renderBool = value =>
      value ? (
        <i className="fa fa-check-square-o" />
      ) : (
        <i className="fa fa-square-o" />
      );

    const {pageInformation} = this.props;
    return (
      <Grid className="Info">
        <Row>
          <Col xs={12}>
            <fieldset className="no-padding">
              <Row>
                <Col xs={1} />
                <Col xs={10} className="Info__container-data">
                  <div className="Info__description">
                    <FormattedMessage id="plugin.table.description" />
                  </div>
                </Col>
                <Col xs={1} />
              </Row>
            </fieldset>
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <fieldset className="no-padding">
              <Row>
                <Col xs={1} />
                <Col
                  xs={4}
                  className="Info__container-data-information Info__table"
                >
                  {row("common.code", pageInformation.code)}
                  {row(
                    "common.title",
                    pageInformation.title && pageInformation.title.en
                  )}
                  {row("common.ownerGroup", pageInformation.group)}
                  {row(
                    "common.viewOnlyGroup",
                    pageInformation.metadata &&
                      pageInformation.metadata.extraGroups
                  )}
                  {row(
                    "common.pageModel",
                    pageInformation.metadata && pageInformation.metadata.model
                  )}
                  <div className="Info__table-row">
                    <div className="Info__table-cell">
                      <FormattedMessage id="common.configureOnThefly" />
                    </div>
                    <div className="Info__table-cell">
                      <span>{renderBool(pageInformation.changed)}</span>
                    </div>
                  </div>
                  <div className="Info__table-row">
                    <div className="Info__table-cell">
                      <FormattedMessage id="common.SEO" />
                    </div>
                    <div className="Info__table-cell">
                      <span>{renderBool(pageInformation.status)}</span>
                    </div>
                  </div>
                </Col>
                <Col xs={1} />
              </Row>
            </fieldset>
          </Col>
        </Row>
      </Grid>
    );
  }
}

Info.propTypes = {
  onWillMount: PropTypes.func.isRequired,
  pageInformation: PropTypes.shape({})
};

export default Info;
