import React from 'react';
import PropTypes from 'prop-types';
import { Row, Col } from 'patternfly-react';

import DashboardWidgetTitleContainer from 'ui/widgets/common/form/containers/DashboardWidgetTitleContainer';
import ServerNameFormContainer from 'ui/widgets/common/form/containers/ServerNameFormContainer';
import DatasourceFormContainer from 'ui/widgets/common/form/containers/DatasourceFormContainer';

const ChartFirstStepContent = ({ formName }) => (
  <div className="ChartFirstStep">
    <Row>
      <Col xs={12} className="ChartFirstStep__widget-title">
        <DashboardWidgetTitleContainer />
      </Col>
    </Row>
    <Row>
      <Col xs={12} className="ChartFirstStep__container-datasource">
        <Row>
          <Col xs={12}>
            <ServerNameFormContainer labelSize={2} />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <DatasourceFormContainer labelSize={2} formName={formName} />
          </Col>
        </Row>
      </Col>
    </Row>
  </div>
);

ChartFirstStepContent.propTypes = {
  formName: PropTypes.string.isRequired,
};

export default ChartFirstStepContent;
