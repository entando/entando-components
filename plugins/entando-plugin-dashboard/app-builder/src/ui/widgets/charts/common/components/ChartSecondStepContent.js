import React, {Component} from "react";
import PropTypes from "prop-types";
import {Row, Col} from "patternfly-react";
import {uniqueId} from "lodash";
import SettingsChartContainer from "ui/widgets/charts/common/containers/SettingsChartContainer";
import PreviewChartSelected from "ui/widgets/charts/common/components/PreviewChartSelected";

class ChartSecondStepContent extends Component {
  render() {
    const {
      formName,
      type,
      data,
      labelChartPreview,
      axis: {rotated}
    } = this.props;
    return (
      <div className="ChartSecondStep">
        <PreviewChartSelected
          idChart={`chart-linear-second-step-${uniqueId()}`}
          type={type}
          data={data}
          labelChartPreview={labelChartPreview}
          axisRotated={rotated}
          heightChart={250}
        />
        <Row>
          <Col xs={12} className="ChartSecondStep__settings-container">
            <SettingsChartContainer formName={formName} />
          </Col>
        </Row>
      </div>
    );
  }
}

ChartSecondStepContent.propTypes = {
  type: PropTypes.string.isRequired,
  labelChartPreview: PropTypes.string.isRequired,
  data: PropTypes.arrayOf(
    PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.string, PropTypes.number]))
  ).isRequired,
  axis: PropTypes.shape({
    rotated: PropTypes.bool
  })
};

ChartSecondStepContent.defaultProps = {
  axix: {
    rotated: false
  }
};

export default ChartSecondStepContent;
