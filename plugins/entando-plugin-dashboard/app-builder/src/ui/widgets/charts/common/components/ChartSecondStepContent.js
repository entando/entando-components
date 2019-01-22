import React, {Component} from "react";
import PropTypes from "prop-types";
import {Row, Col} from "patternfly-react";
import SettingsLineChartContainer from "ui/widgets/charts/line-chart/containers/SettingsLineChartContainer";
import PreviewChartSelected from "ui/widgets/charts/common/components/PreviewChartSelected";

class ChartSecondStepContent extends Component {
  render() {
    const {
      type,
      data,
      labelChartPreview,
      axis: {rotated}
    } = this.props;
    return (
      <div className="ChartSecondStep">
        <PreviewChartSelected
          idChart="chart-linear-second-step"
          type={type}
          data={data}
          labelChartPreview={labelChartPreview}
          axisRotated={rotated}
          heightChart={250}
        />
        <Row>
          <Col xs={12} className="ChartSecondStep__settings-container">
            <SettingsLineChartContainer />
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
