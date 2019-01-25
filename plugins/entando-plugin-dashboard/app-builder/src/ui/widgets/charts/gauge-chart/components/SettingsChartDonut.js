import React from "react";
import PropTypes from "prop-types";
import {FieldArray} from "redux-form";
import {Row, Col, FormGroup, ControlLabel} from "patternfly-react";

import FormattedMessage from "ui/i18n/FormattedMessage";

import FieldArrayDropDownMultiple from "ui/widgets/charts/common/components/FieldArrayDropDownMultiple";

const SettingsChartDonut = ({optionColumns, optionColumnSelected}) => {
  return (
    <div className="SettingsChartDonut">
      <Row>
        <Col xs={1} className="SettingsChartDonut__col">
          <FormGroup className="SettingsChartDonut__form-group">
            <ControlLabel>
              <strong>
                <FormattedMessage id="plugin.chart.values" />
              </strong>
            </ControlLabel>
          </FormGroup>
        </Col>
        <Col xs={11} className="SettingsChartDonut__col">
          <FieldArray
            className="SettingsChartDonut__column-selected"
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

SettingsChartDonut.propTypes = {
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};

export default SettingsChartDonut;
