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
      fields: {name, remove},
      label,
      formName,
      datasourceSelected,
      optionColumns,
      optionColumnSelected,
      datasourcesValue,
      addColumnOptionSelected,
      removeColumnOptionSelected
    } = this.props;

    const disabledAddButton =
      label === "" ||
      optionColumns.length === 0 ||
      optionColumnSelected.length === 0
        ? true
        : false;

    return (
      <Grid className="FieldArrayDatasource">
        <Row>
          {datasourcesValue.map((m, index) => (
            <div
              key={index}
              className="FieldArrayDatasource__data-array-container"
            >
              <div className="FieldArrayDatasource__data-array-item">
                <DatasourceLayer
                  formName={formName}
                  optionColumns={m.optionColumns}
                  optionColumnSelected={m.optionColumnSelected}
                  nameFieldArray={`${name}[${index}]`}
                  addColumnOptionSelected={addColumnOptionSelected}
                  removeColumnOptionSelected={removeColumnOptionSelected}
                  datasourceSelected={m.datasource}
                  disabled
                />
              </div>
              <div className="FieldArrayDatasource__icon-remove">
                <i
                  className="fa fa-trash fa-2x"
                  onClick={() => remove(index)}
                />
              </div>
            </div>
          ))}

          <DatasourceLayer
            formName={formName}
            optionColumns={optionColumns}
            optionColumnSelected={optionColumnSelected}
            datasourceSelected={datasourceSelected}
          />
          <Col xs={12}>
            <Button
              disabled={disabledAddButton}
              type="button"
              bsStyle="primary"
              className="btn-add"
              onClick={() => this.addDatasource()}
            >
              <FormattedMessage id="common.add" />
            </Button>
          </Col>
        </Row>
      </Grid>
    );
  }
}
FieldArrayDatasourceLayer.propTypes = {
  name: PropTypes.string,
  label: PropTypes.string,
  formName: PropTypes.string.isRequired,
  clearInputDatasourceData: PropTypes.func.isRequired,
  removeColumnOptionSelected: PropTypes.func.isRequired,
  addColumnOptionSelected: PropTypes.func.isRequired,
  datasourceSelected: PropTypes.string,
  datasourcesValue: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({}))
};
FieldArrayDatasourceLayer.defaultProps = {
  name: null,
  label: "",
  datasourceSelected: "",
  datasourcesValue: [],
  optionColumnSelected: []
};

export default FieldArrayDatasourceLayer;
