import React from 'react';
import PropTypes from 'prop-types';
import { FieldArray } from 'redux-form';
import { Row, Col, FormGroup, ControlLabel } from 'patternfly-react';
import { required } from '@entando/utils';

import FormattedMessage from 'ui/i18n/FormattedMessage';

import FieldArrayDropDownMultiple from 'ui/common/FieldArrayDropDownMultiple';

const SettingsChartGauge = ({
  datasourceSelected,
  optionColumns,
  optionColumnXSelected,
}) => (
  <div className="SettingsChartGauge">
    <Row>
      <Col xs={1} className="SettingsChartGauge__col">
        <FormGroup className="SettingsChartGauge__form-group">
          <ControlLabel>
            <strong>
              <FormattedMessage id="plugin.chart.values" />
            </strong>
          </ControlLabel>
        </FormGroup>
      </Col>
      <Col xs={11} className="SettingsChartGauge__col">
        <FieldArray
          className="SettingsChartGauge__column-selected"
          name="columns.x"
          idKey={datasourceSelected}
          component={FieldArrayDropDownMultiple}
          optionColumns={optionColumns}
          optionColumnSelected={optionColumnXSelected}
          validate={[required]}
        />
      </Col>
    </Row>
  </div>
);

SettingsChartGauge.propTypes = {
  datasourceSelected: PropTypes.string.isRequired,
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnXSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
};

export default SettingsChartGauge;
