import React from "react";
import PropTypes from "prop-types";
import {Row, Col} from "patternfly-react";
import {uniqueId} from "lodash";
import PreviewChartSelected from "ui/widgets/charts/common/components/PreviewChartSelected";
import GeneralSettings from "ui/widgets/charts/common/components/GeneralSettings";

const DONUT_CHART = "DONUT_CHART";

const options = {
  render: (type, formName) => {
    switch (type) {
      case DONUT_CHART:
        return <GeneralSettings />;
      default:
        return <GeneralSettings />;
    }
  },
  widthChart: type => {
    switch (type) {
      case DONUT_CHART:
        return 400;
      default:
        return null;
    }
  },
  columnSize: type => {
    switch (type) {
      case DONUT_CHART:
        return 4;
      default:
        return 12;
    }
  }
};

const ChartThirdStepContent = ({
  formName,
  type,
  data,
  labelChartPreview,
  axis: {rotated}
}) => {
  const render = options.render(type, formName);
  const widthChart = options.widthChart(type);
  const columnSize = options.columnSize(type);
  return (
    <div className="ChartThirdStep">
      <PreviewChartSelected
        idChart={`chart-second-step-${uniqueId()}`}
        type={type}
        data={data}
        labelChartPreview={labelChartPreview}
        axisRotated={rotated}
        heightChart={250}
        widthChart={widthChart}
        columnSize={columnSize}
      />
      <Row>
        <Col xs={12} className="ChartThirdStep__settings-container">
          {render}
        </Col>
      </Row>
    </div>
  );
};

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
