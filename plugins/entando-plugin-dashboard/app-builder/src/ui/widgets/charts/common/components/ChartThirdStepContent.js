import React from "react";
import PropTypes from "prop-types";
import {Row, Col} from "patternfly-react";
import {uniqueId} from "lodash";
import PreviewChartSelected from "ui/widgets/charts/common/components/PreviewChartSelected";
import GeneralSettings from "ui/widgets/charts/common/components/GeneralSettings";

const DONUT_CHART = "DONUT_CHART";
const GAUGE_CHART = "GAUGE_CHART";
const options = {
  widthChart: type => {
    switch (type) {
      case DONUT_CHART:
        return 400;
      case GAUGE_CHART:
        return 400;
      default:
        return null;
    }
  },
  columnSize: type => {
    switch (type) {
      case DONUT_CHART:
        return 4;
      case GAUGE_CHART:
        return 4;
      default:
        return 12;
    }
  }
};

const ChartThirdStepContent = ({
  formName,
  typeChart,
  data,
  labelChartPreview,
  rotated,
  chart
}) => {
  const widthChart = options.widthChart(typeChart);
  const columnSize = options.columnSize(typeChart);
  return (
    <div className="ChartThirdStep">
      <PreviewChartSelected
        idChart={`chart-second-step-${uniqueId()}`}
        type={typeChart}
        data={data}
        labelChartPreview={labelChartPreview}
        axisRotated={rotated}
        heightChart={250}
        widthChart={widthChart}
        columnSize={columnSize}
      />
      <Row>
        <Col xs={12} className="ChartThirdStep__settings-container">
          <GeneralSettings typeChart={typeChart} chart={chart} />
        </Col>
      </Row>
    </div>
  );
};

ChartThirdStepContent.propTypes = {
  typeChart: PropTypes.string.isRequired,
  labelChartPreview: PropTypes.string.isRequired,
  data: PropTypes.arrayOf(
    PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.string, PropTypes.number]))
  ).isRequired,
  axis: PropTypes.shape({
    rotated: PropTypes.bool
  })
};

ChartThirdStepContent.defaultProps = {
  rotated: false
};

export default ChartThirdStepContent;
