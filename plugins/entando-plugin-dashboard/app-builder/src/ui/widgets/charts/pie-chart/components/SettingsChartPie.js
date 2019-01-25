import React from "react";
import PropTypes from "prop-types";
import {FieldArray} from "redux-form";
import {Row, Col, FormGroup, ControlLabel} from "patternfly-react";

import FormattedMessage from "ui/i18n/FormattedMessage";

import FieldArrayDropDownMultiple from "ui/widgets/charts/common/components/FieldArrayDropDownMultiple";

const SettingsChartPie = ({optionColumns, optionColumnSelected}) => {
  return (
    <div className="SettingsChartPie">
      <Row>
        <Col xs={1} className="SettingsChartPie">
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

SettingsChartPie.propTypes = {
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};

export default SettingsChartPie;
