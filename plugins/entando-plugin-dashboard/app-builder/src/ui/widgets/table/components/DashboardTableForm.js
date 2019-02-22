import React, {Component} from "react";
import PropTypes from "prop-types";
import {Field, reduxForm} from "redux-form";
import {
  InputGroup,
  FormGroup,
  Button,
  Col,
  ControlLabel
} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";
import SwitchRenderer from "ui/common/form/SwitchRenderer";
import ServerNameFormContainer from "ui/widgets/common/form/containers/ServerNameFormContainer";
import DatasourceFormContainer from "ui/widgets/common/form/containers/DatasourceFormContainer";
import DashboardTableColumnsContainer from "ui/widgets/table/containers/DashboardTableColumnsContainer";
import DashboardWidgetTitleContainer from "ui/widgets/common/form/containers/DashboardWidgetTitleContainer";

class DashboardTableFormBody extends Component {
  constructor(props) {
    super(props);
    this.state = {
      toggleAllColums: true
    };
    this.handleChange = this.handleChange.bind(this);
  }

  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount();
    }
  }
  renderDownlodable() {
    return (
      <FormGroup className="DashboardTableForm__input-group">
        <Col smOffset={2} xs={10}>
          <div className="checkbox">
            <label>
              <Field
                component="input"
                type="checkbox"
                name="options.downlodable"
              />
              <strong>
                <FormattedMessage id="plugin.table.downlodable" />
              </strong>
              <br />
              <FormattedMessage id="plugin.table.downlodable.format" />
            </label>
          </div>
        </Col>
      </FormGroup>
    );
  }
  renderFiltrable() {
    return (
      <FormGroup className="DashboardTableForm__input-group">
        <Col smOffset={2} xs={10}>
          <div className="checkbox">
            <label>
              <Field
                component="input"
                type="checkbox"
                name="options.filtrable"
              />
              <strong>
                <FormattedMessage id="plugin.table.filterable" />
              </strong>
            </label>
          </div>
        </Col>
      </FormGroup>
    );
  }
  renderColumnInformation() {
    return (
      <InputGroup className="DashboardTableForm__input-group">
        <ControlLabel htmlFor="allColumns">
          <strong>
            <FormattedMessage id="plugin.table.allColumns" />
          </strong>
        </ControlLabel>
        <Field
          className="DashboardTableForm__switch-all-columns"
          component={SwitchRenderer}
          name="allColumns"
          onChange={this.handleChange}
          disabled={this.props.datasource ? false : true}
        />
      </InputGroup>
    );
  }

  handleChange(ev, newValue) {
    this.setState({toggleAllColums: !this.state.toggleAllColums});
  }

  render() {
    const {handleSubmit, invalid, submitting} = this.props;
    return (
      <form
        onSubmit={handleSubmit}
        className="DashboardTableForm form-horizontal"
      >
        <fieldset className="no-padding">
          <Col xs={12}>
            <div className="DashboardTableForm__required-fields text-right">
              *<FormattedMessage id="plugin.fieldsRequired" />
            </div>
          </Col>

          <Col xs={12} className="DashboardTableForm__container-data">
            <div className="DashboardTableForm__description">
              <FormattedMessage id="plugin.table.description" />
            </div>
          </Col>

          <Col
            xs={12}
            className="DashboardTableForm__container-data DashboardTableForm__widget-title"
          >
            <DashboardWidgetTitleContainer />
          </Col>

          <Col
            xs={12}
            className="DashboardTableForm__container-data DashboardTableForm__sourcers"
          >
            <Col xs={6}>
              <ServerNameFormContainer />
            </Col>
            <Col xs={6}>{this.renderDownlodable()}</Col>
            <Col xs={6}>
              <DatasourceFormContainer formName="form-dashboard-table" />
            </Col>
            <Col xs={6}>{this.renderFiltrable()}</Col>
          </Col>

          <Col xs={12} className="DashboardTableForm__container-data">
            <Col xs={6}>{this.renderColumnInformation()}</Col>
          </Col>

          <Col
            xs={12}
            className={
              this.state.toggleAllColums
                ? "DashboardTableForm__container-data--disabled"
                : "DashboardTableForm__container-data"
            }
          >
            <Col xs={6}>
              <div className="DashboardTableForm__more-information">
                <strong>
                  <FormattedMessage id="plugin.table.moreInformation" />
                </strong>
                <br />
                <FormattedMessage id="plugin.table.moreInformation.choose" />
              </div>
            </Col>
          </Col>

          <Col xs={12}>
            <DashboardTableColumnsContainer />
          </Col>

          <Col xs={12} className="DashboardTableForm__line-bottom" />

          <Col xs={12}>&nbsp;</Col>

          <Col xs={12}>
            <Col xs={6}>
              <Button className="pull-left" bsStyle="default">
                <FormattedMessage id="common.cancel" />
              </Button>
            </Col>
            <Col xs={6}>
              <Button
                className="pull-right"
                type="submit"
                bsStyle="primary"
                disabled={invalid || submitting}
              >
                <FormattedMessage id="common.save" />
              </Button>
            </Col>
          </Col>
        </fieldset>
      </form>
    );
  }
}

DashboardTableFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  invalid: PropTypes.bool,
  submitting: PropTypes.bool
};

DashboardTableFormBody.defaultProps = {
  invalid: false,
  submitting: false
};
const DashboardTableForm = reduxForm({
  form: "form-dashboard-table"
})(DashboardTableFormBody);

export default DashboardTableForm;
