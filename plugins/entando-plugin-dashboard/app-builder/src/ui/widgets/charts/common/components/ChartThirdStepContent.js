import React from 'react';
import PropTypes from 'prop-types';
import { Row, Col } from 'patternfly-react';
import { uniqueId } from 'lodash';
import PreviewChartSelected from 'ui/widgets/charts/common/components/PreviewChartSelected';
import GeneralSettings from 'ui/widgets/charts/common/components/GeneralSettings';

const DONUT_CHART = 'DONUT_CHART';
const GAUGE_CHART = 'GAUGE_CHART';
const PIE_CHART = 'PIE_CHART';
const options = {
  columnSize: type =>
    ([DONUT_CHART, GAUGE_CHART, PIE_CHART].includes(type) ? 4 : 12),
  width: type =>
    ([DONUT_CHART, GAUGE_CHART, PIE_CHART].includes(type) ? 250 : undefined),
};

const ChartThirdStepContent = ({
  typeChart,
  data,
  labelChartPreview,
  rotated,
  chart,
}) => {
  const columnSize = options.columnSize(typeChart);
  const width = options.width(typeChart);
  return (
    <div className="ChartThirdStep">
      <PreviewChartSelected
        idChart={`chart-second-step-${uniqueId()}`}
        type={typeChart}
        data={data}
        labelChartPreview={labelChartPreview}
        axisRotated={rotated}
        heightChart={250}
        widthChart={width}
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
  data: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.oneOfType([
    PropTypes.string, PropTypes.number,
  ]))).isRequired,
  rotated: PropTypes.bool,
  chart: PropTypes.string,
};

ChartThirdStepContent.defaultProps = {
  rotated: false,
  chart: '',
};

export default ChartThirdStepContent;
