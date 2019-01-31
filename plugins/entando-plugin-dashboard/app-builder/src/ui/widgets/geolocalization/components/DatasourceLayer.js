import React from "react";
import PropTypes from "prop-types";
import {Field, FieldArray} from "redux-form";
import {Grid, Row, Col, InputGroup, Button} from "patternfly-react";
import DatasourceFormContainer from "ui/widgets/common/form/containers/DatasourceFormContainer";
import {formattedText, required, minLength, maxLength} from "@entando/utils";

import RenderTextInput from "ui/common/form/RenderTextInput";
import FormLabel from "ui/common/form/FormLabel";
import FieldArrayDropDownMultiple from "ui/common/FieldArrayDropDownMultiple";
import FormattedMessage from "ui/i18n/FormattedMessage";

const maxLength20 = maxLength(20);
const minLength3 = minLength(3);

const DatasourceLayer = ({
  formName,
  optionColumns,
  optionColumnSelected,
  nameFieldArray
}) => {
  console.log("optionColumns", optionColumns);
  console.log("optionColumnSelected", optionColumnSelected);
  const nameFileld = nameFieldArray
    ? `${nameFieldArray}[datasourceColumns]`
    : "datasourceColumns";
  console.log("nameField", nameFileld);

  return (
    <Grid className="DatasourceLayer">
      <Row>
        <Col xs={6} className="DatasourceLayer__datasource">
          <DatasourceFormContainer
            formName={formName}
            nameFieldArray={nameFieldArray}
          />
        </Col>
        <Col xs={6} className="DatasourceLayer__col-label">
          <InputGroup className="">
            <Field
              component={RenderTextInput}
              name={nameFieldArray ? `${nameFieldArray}[label]` : "label"}
              label={
                <FormLabel labelId="plugin.geolocalization.label" required />
              }
              alignClass="text-left"
              validate={[required, minLength3, maxLength20]}
              append={formattedText("plugin.geolocalization.label.requirement")}
            />
          </InputGroup>
        </Col>

        <Col xs={12}>
          {optionColumns.length === 0 ? null : (
            <div className="form-group">
              <label htmlFor="label-item" className="col-xs-2 control-label">
                <FormattedMessage id="plugin.geolocalization.item" />
              </label>
              <Col xs={10} className="DatasourceLayer__item">
                <FieldArray
                  id="label-item"
                  className="SettingsChartPie__column-selected"
                  name={nameFileld}
                  component={FieldArrayDropDownMultiple}
                  optionColumns={optionColumns}
                  optionColumnSelected={optionColumnSelected}
                  disabled={optionColumns.length === 0 ? true : false}
                />
              </Col>
            </div>
          )}
        </Col>
        <Col xs={12}>
          <hr />
        </Col>
      </Row>
    </Grid>
  );
};
export default DatasourceLayer;
