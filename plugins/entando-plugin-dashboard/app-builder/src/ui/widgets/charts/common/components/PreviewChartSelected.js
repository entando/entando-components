import React from "react";
import PropTypes from "prop-types";
import {Grid, Row, Col} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";

import Chart from "ui/widgets/charts/helper/Chart";

const PreviewChartSelected = ({
  type,
  labelChartPreview,
  idChart,
  data,
  axisRotated,
  heightChart,
  widthChart,
  columnSize
}) => (
  <Grid fluid className="PreviewChartSelected">
    <Row className="PreviewChartSelected__row-preview-text-chart">
      <Col
        xs={12}
        className="PreviewChartSelected__preview-text-chart-container"
      >
        <FormattedMessage id="plugin.charts.previewChart.label" />
        <span className="PreviewChartSelected__preview-text-type-chart">
          <FormattedMessage
            id="plugin.charts.previewChart.label.chartType"
            values={{chart: labelChartPreview}}
          />
        </span>
      </Col>
    </Row>
    <Row className="PreviewChartSelected__row-preview-chart">
      <Col xs={columnSize} className="PreviewChartSelected__preview-chart">
        <Chart
          idChart={idChart}
          type={type}
          columns={data}
          config={{
            axis: {rotated: axisRotated},
            size: {height: heightChart, width: widthChart}
          }}
        />
      </Col>
    </Row>
  </Grid>
);

PreviewChartSelected.propTypes = {
  type: PropTypes.string.isRequired,
  labelChartPreview: PropTypes.string.isRequired,
  data: PropTypes.arrayOf(
    PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.string, PropTypes.number]))
  ).isRequired,
  axisRotated: PropTypes.bool,
  heightChart: PropTypes.number,
  columnSize: PropTypes.number
};

PreviewChartSelected.defaultProps = {
  axisRotated: false,
  heightChart: 250,
  widthChart: null,
  columnSize: 12
};

export default PreviewChartSelected;
