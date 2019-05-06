import React from 'react';
import PropTypes from 'prop-types';
import { Grid, Row, Col } from 'patternfly-react';
import { get } from 'lodash';

import FormattedMessage from 'ui/i18n/FormattedMessage';

const row = (idMessage, label) => (
  <div className="Info__table-row">
    <div className="Info__table-cell">
      <FormattedMessage id={idMessage} />
    </div>
    <div className="Info__table-cell">
      <label htmlFor="info">{label}</label>
    </div>
  </div>
);

const Info = ({ pageInformation }) => {
  const renderBool = value =>
    (value ? (
      <i className="fa fa-check-square-o" />
    ) : (
      <i className="fa fa-square-o" />
    ));
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
                {row('common.code', pageInformation.code)}
                {row('common.title', get(pageInformation, 'titles.en', ''))}
                {row('common.ownerGroup', pageInformation.ownerGroup)}
                {row('common.pageModel', pageInformation.pageModel)}
                <div className="Info__table-row">
                  <div className="Info__table-cell">
                    <FormattedMessage id="common.configureOnThefly" />
                  </div>
                  <div className="Info__table-cell">
                    <span>{renderBool(pageInformation.lastModified)}</span>
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
};

const INFORMATION_TYPE = {
  code: PropTypes.string,
  titles: PropTypes.shape({ en: PropTypes.string }),
  ownerGroup: PropTypes.string,
  pageModel: PropTypes.string,
  lastModified: PropTypes.string,
  status: PropTypes.string,
};

Info.propTypes = {
  pageInformation: PropTypes.shape(INFORMATION_TYPE),
};

Info.defaultProps = {
  pageInformation: {
    code: '',
    titles: {
      en: '',
    },
    ownerGroup: '',
    pageModel: '',
    lastModified: '',
    status: '',
  },
};

export default Info;
