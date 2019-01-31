import React, {Component} from "react";
import PropTypes from "prop-types";
import {Grid, Row, Col, Button} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";

import DatasourceLayer from "./DatasourceLayer";

class FieldArrayDatasourceLayer extends Component {
  constructor(props) {
    super(props);
    this.addDatasource = this.addDatasource.bind(this);
  }

  addDatasource() {
    console.log("addDatasource", this.props);
    const {
      fields,
      label,
      datasourceSelected,
      optionColumns,
      optionColumnSelected
    } = this.props;
    const datasource = {
      label,
      datasource: datasourceSelected,
      optionColumns,
      optionColumnSelected
    };
    fields.push(datasource);
    this.props.clearInputDatasourceData();
  }

  render() {
    const {
      formName,
      optionColumns,
      optionColumnSelected,
      datasourcesValue
    } = this.props;
    return (
      <Grid className="MapSecondStepContent">
        <Row>
          {datasourcesValue.map((m, index) => (
            <DatasourceLayer
              key={m.datasource}
              formName={m.formName}
              optionColumns={m.optionColumns}
              optionColumnSelected={m.optionColumnSelected}
              nameFieldArray={`datasources[${index}]`}
            />
          ))}

          <DatasourceLayer
            formName={formName}
            optionColumns={optionColumns}
            optionColumnSelected={optionColumnSelected}
          />
          <Col xs={12}>
            <div className="col-xs-2">
              <Button
                type="button"
                bsStyle="default"
                className="btn-add"
                onClick={() => this.addDatasource()}
              >
                <FormattedMessage id="common.add" />
              </Button>
            </div>
          </Col>
        </Row>
      </Grid>
    );
  }
}
FieldArrayDatasourceLayer.propTypes = {
  clearInputDatasourceData: PropTypes.func.isRequired,
  datasourcesValue: PropTypes.arrayOf(PropTypes.shape({}))
};
FieldArrayDatasourceLayer.defaultProps = {
  datasourcesValue: []
};

export default FieldArrayDatasourceLayer;
