import React, {Component} from "react";
import {Row, Col} from "patternfly-react";

import DashboardWidgetTitleContainer from "ui/widgets/common/containers/DashboardWidgetTitleContainer";
import ServerNameFormContainer from "ui/widgets/common/form/containers/ServerNameFormContainer";
import DatasourceFormContainer from "ui/widgets/common/form/containers/DatasourceFormContainer";

class ChartFirstStepContent extends Component {
  render() {
    return (
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
                <DatasourceFormContainer labelSize={2} />
              </Col>
            </Row>
          </Col>
        </Row>
      </div>
    );
  }
}

export default ChartFirstStepContent;
