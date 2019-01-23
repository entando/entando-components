import React, {Component} from "react";
import PropTypes from "prop-types";
import {Row, Col} from "patternfly-react";
import {uniqueId} from "lodash";
import PreviewChartSelected from "ui/widgets/charts/common/components/PreviewChartSelected";
import GeneralSettingsContainer from "ui/widgets/charts/common/containers/GeneralSettingsContainer";
class ChartThirdStepContent extends Component {
  render() {
    const {
      type,
      data,
      labelChartPreview,
      axis: {rotated}
    } = this.props;
    return (
      <div className="ChartThirdStep">
        <PreviewChartSelected
          idChart={`chart-linear-second-step-${uniqueId()}`}
          type={type}
          data={data}
          labelChartPreview={labelChartPreview}
          axisRotated={rotated}
          heightChart={250}
        />
        <Row>
          <Col xs={12} className="ChartThirdStep__settings-container">
            <GeneralSettingsContainer />
          </Col>
        </Row>
      </div>
    );
  }
}

ChartThirdStepContent.propTypes = {
  type: PropTypes.string.isRequired,
  labelChartPreview: PropTypes.string.isRequired,
  data: PropTypes.arrayOf(
    PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.string, PropTypes.number]))
  ).isRequired,
  axis: PropTypes.shape({
    rotated: PropTypes.bool
  }).isRequired
};

export default ChartThirdStepContent;
