import React, {Component} from "react";
import PropTypes from "prop-types";
import {Row, Col} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";

import Chart from "ui/widgets/charts/helper/Chart";
import SettingsLineChartContainer from "ui/widgets/charts/line-chart/containers/SettingsLineChartContainer";

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
        <Row className="ChartSecondStep__row-preview-text-chart">
          <Col
            xs={12}
            className="ChartSecondStep__preview-text-chart-container"
          >
            <FormattedMessage id="plugin.charts.previewChart.label" />
            <span className="ChartSecondStep__preview-text-type-chart">
              <FormattedMessage
                id="plugin.charts.previewChart.label.chartType"
                values={{chart: labelChartPreview}}
              />
            </span>
          </Col>
        </Row>
        <Row className="ChartSecondStep__row-preview-chart">
          <Col xs={12} className="ChartSecondStep__preview-chart">
            <Chart
              type={type}
              columns={data}
              config={{axis: {rotated: rotated}, size: {height: 250}}}
            />
          </Col>
        </Row>
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
