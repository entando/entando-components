import React from "react";
import PropTypes from "prop-types";
import {FieldArray} from "redux-form";
import {Row, Col, FormGroup, ControlLabel} from "patternfly-react";

import FormattedMessage from "ui/i18n/FormattedMessage";

import FieldArrayDropDownMultiple from "ui/widgets/charts/common/components/FieldArrayDropDownMultiple";

const SettingsChartGauge = ({optionColumns, optionColumnSelected}) => {
  return (
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
            name="columns"
            component={FieldArrayDropDownMultiple}
            optionColumns={optionColumns}
            optionColumnSelected={optionColumnSelected}
          />
        </Col>
      </Row>
    </div>
  );
};

SettingsChartGauge.propTypes = {
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};

export default SettingsChartGauge;
