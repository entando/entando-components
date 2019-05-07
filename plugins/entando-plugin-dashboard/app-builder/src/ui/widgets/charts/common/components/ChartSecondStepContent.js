import React from 'react';
import PropTypes from 'prop-types';
import { Row, Col } from 'patternfly-react';
import { uniqueId } from 'lodash';
import SettingsChartContainer from 'ui/widgets/charts/common/containers/SettingsChartContainer';
import SettingsChartDonutContainer from 'ui/widgets/charts/donut-chart/containers/SettingsChartDonutContainer';
import SettingsChartGaugeContainer from 'ui/widgets/charts/gauge-chart/containers/SettingsChartGaugeContainer';
import SettingsChartPieContainer from 'ui/widgets/charts/pie-chart/containers/SettingsChartPieContainer';
import PreviewChartSelected from 'ui/widgets/charts/common/components/PreviewChartSelected';

const DONUT_CHART = 'DONUT_CHART';
const GAUGE_CHART = 'GAUGE_CHART';
const PIE_CHART = 'PIE_CHART';

const options = {
  render: (type, formName) => {
    switch (type) {
      case DONUT_CHART:
        return <SettingsChartDonutContainer formName={formName} />;
      case GAUGE_CHART:
        return <SettingsChartGaugeContainer formName={formName} />;
      case PIE_CHART:
        return <SettingsChartPieContainer formName={formName} />;
      default:
        return <SettingsChartContainer formName={formName} />;
    }
  },
  columnSize: type =>
    ([DONUT_CHART, GAUGE_CHART, PIE_CHART].includes(type) ? 4 : 12),
  width: type =>
    ([DONUT_CHART, GAUGE_CHART, PIE_CHART].includes(type) ? 250 : undefined),
};
const ChartSecondStepContent = ({
  formName,
  typeChart,
  data,
  labelChartPreview,
  rotated,
}) => {
  const render = options.render(typeChart, formName);
  const columnSize = options.columnSize(typeChart);
  const width = options.width(typeChart);
  return (
    <div className="ChartSecondStep">
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
        <Col xs={12} className="ChartSecondStep__settings-container">
          {render}
        </Col>
      </Row>
    </div>
  );
};

ChartSecondStepContent.propTypes = {
  typeChart: PropTypes.string.isRequired,
  labelChartPreview: PropTypes.string.isRequired,
  data: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.oneOfType([
    PropTypes.string,
    PropTypes.number,
  ]))).isRequired,
  rotated: PropTypes.bool,
  formName: PropTypes.string.isRequired,
};

ChartSecondStepContent.defaultProps = {
  rotated: false,
};

export default ChartSecondStepContent;
