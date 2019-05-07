import React from 'react';
import PropTypes from 'prop-types';
import { FieldArray } from 'redux-form';
import { Row, Col, FormGroup, ControlLabel } from 'patternfly-react';
import { required } from '@entando/utils';

import FormattedMessage from 'ui/i18n/FormattedMessage';

import FieldArrayDropDownMultiple from 'ui/common/FieldArrayDropDownMultiple';

const SettingsChartPie = ({
  datasourceSelected,
  optionColumns,
  optionColumnXSelected,
}) => (
  <div className="SettingsChartPie">
    <Row>
      <Col xs={1} className="SettingsChartPie__col">
        <FormGroup className="SettingsChartPie__form-group">
          <ControlLabel>
            <strong>
              <FormattedMessage id="plugin.chart.values" />
            </strong>
          </ControlLabel>
        </FormGroup>
      </Col>
      <Col xs={11} className="SettingsChartPie__col">
        <FieldArray
          className="SettingsChartPie__column-selected"
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

SettingsChartPie.propTypes = {
  datasourceSelected: PropTypes.string.isRequired,
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnXSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
};
export default SettingsChartPie;
