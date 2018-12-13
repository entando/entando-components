import React, {Component} from "react";
import PropTypes from "prop-types";
import {Field, reduxForm} from "redux-form";
import {required, minLength, maxLength} from "@entando/utils";
import {
  InputGroup,
  FormGroup,
  Button,
  Row,
  Col,
  ControlLabel
} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";
import RenderTextInput from "ui/common/form/RenderTextInput";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import SwitchRenderer from "ui/common/form/SwitchRenderer";
import FormLabel from "ui/common/form/FormLabel";
import DataSourceFormContainer from "ui/widgets/common/form/Containers/DataSourceFormContainer";
import ContextFormContainer from "ui/widgets/common/form/Containers/ContextFormContainer";

const maxLength30 = maxLength(30);
const minLength3 = minLength(3);

const rowRequired = messageId => (
  <Row>
    <Col xs={1} />
    <Col xs={10}>
      <div className="DashboardTableForm__required-fields text-right">
        *<FormattedMessage id={messageId} />
      </div>
    </Col>
    <Col xs={1} />
  </Row>
);

const rowCommon = (messageId, func, classContainer, classDataMessage) => (
  <Row>
    <Col xs={1} />
    <Col xs={10} className={classContainer}>
      {func !== null ? (
        func()
      ) : (
        <div className={classDataMessage}>
          <FormattedMessage id={messageId} />
        </div>
      )}
    </Col>
    <Col xs={1} />
  </Row>
);

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

  renderWidgetTitle() {
    return (
      <InputGroup className="DashboardTableForm__input-group">
        <Field
          component={RenderTextInput}
          className="DashboardTableForm__input-title"
          name="title"
          label={
            <FormLabel
              labelId="plugin.table.widgetTitle"
              helpId="plugin.table.widgetTitle.help"
              required
            />
          }
          alignClass="text-left"
          validate={[required, minLength3, maxLength30]}
        />
      </InputGroup>
    );
  }
  renderDownlodable() {
    return (
      <FormGroup className="DashboardTableForm__input-group">
        <Col smOffset={2} sm={10}>
          <div className="checkbox">
            <label>
              <Field component="input" type="checkbox" name="downlodable" />
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
  renderContext() {
    const selectOptionsContext = [
      {
        value: "parking",
        text: "Parking"
      },
      {
        value: "bikeSharing",
        text: "Bike Sharking"
      }
    ];
    return (
      <InputGroup className="DashboardTableForm__input-group">
        <Field
          component={RenderSelectInput}
          options={selectOptionsContext}
          defaultOptionId="plugin.chooseAnOptionContext"
          label={
            <FormLabel
              labelId="plugin.kindOfContext"
              helpId="plugin.kindOfContext.help"
              required
            />
          }
          labelSize={4}
          alignClass="text-left DashboardTableForm__no-padding-right"
          name="kindContext"
          validate={[required]}
        />
      </InputGroup>
    );
  }
  renderFiltrable() {
    return (
      <FormGroup className="DashboardTableForm__input-group">
        <Col smOffset={2} sm={10}>
          <div className="checkbox">
            <label>
              <Field component="input" type="checkbox" name="filtrable" />
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
        <div className="form-group">
          <Col xs={4} className="text-left">
            <ControlLabel htmlFor="allColumns">
              <strong>
                <FormattedMessage id="plugin.table.allColumns" />
              </strong>
            </ControlLabel>
          </Col>
          <Col xs={8}>
            <Field
              component={SwitchRenderer}
              name="allColumns"
              onChange={this.handleChange}
            />
          </Col>
        </div>
      </InputGroup>
    );
  }
  renderColumnDetail() {
    return (
      <Col
        xs={10}
        className={
          this.state.toggleAllColums
            ? "DashboardTableForm__container-data--disabled"
            : "DashboardTableForm__container-data"
        }
      >
        <Row>
          <Col xs={4}>
            <FormGroup className="DashboardTableForm__input-group">
              <Col xs={10}>
                <div className="checkbox">
                  <label>
                    <Field
                      component="input"
                      type="checkbox"
                      name="deviceStatus"
                      disabled={this.state.toggleAllColums}
                    />
                    <strong>
                      <FormattedMessage id="plugin.table.deviceStatus" />
                    </strong>
                    <br />
                    <FormattedMessage id="plugin.table.deviceStatus.help" />
                  </label>
                </div>
              </Col>
            </FormGroup>
            1/12/201
          </Col>
          <Col xs={4}>
            <FormGroup className="DashboardTableForm__input-group">
              <Col xs={10}>
                <div className="checkbox">
                  <label>
                    <Field
                      component="input"
                      type="checkbox"
                      name="deviceUse"
                      disabled={this.state.toggleAllColums}
                    />
                    <strong>
                      <FormattedMessage id="plugin.table.deviceUse" />
                    </strong>
                    <br />
                    <FormattedMessage id="plugin.table.deviceUse.help" />
                  </label>
                </div>
              </Col>
            </FormGroup>
          </Col>
          <Col xs={4}>
            <FormGroup className="DashboardTableForm__input-group">
              <Col xs={10}>
                <div className="checkbox">
                  <label>
                    <Field
                      component="input"
                      type="checkbox"
                      name="batteryLevel"
                      disabled={this.state.toggleAllColums}
                    />
                    <strong>
                      <FormattedMessage id="plugin.table.batteryLevel" />
                    </strong>
                    <br />
                    <FormattedMessage id="plugin.table.batteryLevel.help" />
                  </label>
                </div>
              </Col>
            </FormGroup>
          </Col>
        </Row>
        <Row>
          <Col xs={4}>
            <FormGroup className="DashboardTableForm__input-group">
              <Col xs={10}>
                <div className="checkbox">
                  <label>
                    <Field
                      component="input"
                      type="checkbox"
                      name="deviceCode"
                      disabled={this.state.toggleAllColums}
                    />
                    <strong>
                      <FormattedMessage id="plugin.table.deviceCode" />
                    </strong>
                  </label>
                </div>
              </Col>
            </FormGroup>
          </Col>
          <Col xs={4}>
            <FormGroup className="DashboardTableForm__input-group">
              <Col xs={10}>
                <div className="checkbox">
                  <label>
                    <Field
                      component="input"
                      type="checkbox"
                      name="deviceBrand"
                      disabled={this.state.toggleAllColums}
                    />
                    <strong>
                      <FormattedMessage id="plugin.table.deviceBrand" />
                    </strong>
                  </label>
                </div>
              </Col>
            </FormGroup>
          </Col>
          <Col xs={4}>
            <FormGroup className="DashboardTableForm__input-group">
              <Col xs={10}>
                <div className="checkbox">
                  <label>
                    <Field
                      component="input"
                      type="checkbox"
                      name="expirationGuarantee"
                      disabled={this.state.toggleAllColums}
                    />
                    <strong>
                      <FormattedMessage id="plugin.table.expirationGuarantee" />
                    </strong>
                  </label>
                </div>
              </Col>
            </FormGroup>
          </Col>
        </Row>
      </Col>
    );
  }
  handleChange(ev, newValue) {
    this.setState({toggleAllColums: !this.state.toggleAllColums});
    this.props.onChangeToggleColumns(newValue);
  }

  render() {
    const {handleSubmit, invalid, submitting} = this.props;
    return (
      <form
        onSubmit={handleSubmit}
        className="DashboardTableForm form-horizontal"
      >
        <Row>
          <Col xs={12}>
            <fieldset className="no-padding">
              {rowRequired("plugin.fieldsRequired")}
              {rowCommon(
                "plugin.table.description",
                null,
                "DashboardTableForm__container-data",
                "DashboardTableForm__description"
              )}
              {rowCommon(
                null,
                this.renderWidgetTitle,
                "DashboardTableForm__container-data DashboardTableForm__widget-title"
              )}
              <Row>
                <Col xs={1} />
                <Col
                  xs={10}
                  className="DashboardTableForm__container-data DashboardTableForm__line"
                >
                  <Row>
                    <Col xs={6}>
                      <DataSourceFormContainer />
                    </Col>
                    <Col xs={6}>{this.renderDownlodable()}</Col>
                  </Row>
                  <Row>
                    <Col xs={6}>
                      <ContextFormContainer />
                    </Col>
                    <Col xs={6}>{this.renderFiltrable()}</Col>
                  </Row>
                </Col>
                <Col xs={1} />
              </Row>
              <Row>
                <Col xs={1} />
                <Col xs={10} className="DashboardTableForm__container-data">
                  <Row>
                    <Col xs={6}>{this.renderColumnInformation()}</Col>
                  </Row>
                </Col>
                <Col xs={1} />
              </Row>
              <Row>
                <Col xs={1} />
                <Col
                  xs={10}
                  className={
                    this.state.toggleAllColums
                      ? "DashboardTableForm__container-data--disabled"
                      : "DashboardTableForm__container-data"
                  }
                >
                  <Row>
                    <Col xs={6}>
                      <div className="DashboardTableForm__more-information">
                        <strong>
                          <FormattedMessage id="plugin.table.moreInformation" />
                        </strong>
                        <br />
                        <FormattedMessage id="plugin.table.moreInformation.choose" />
                      </div>
                    </Col>
                  </Row>
                </Col>
                <Col xs={1} />
              </Row>
              <Row>
                <Col xs={1} />
                {this.renderColumnDetail()}
              </Row>
              <Row>
                <Col xs={1} />
                <Col xs={10} className="DashboardTableForm__line-bottom" />
                <Col xs={1} />
              </Row>
              <Row>
                <Col xs={12}>&nbsp;</Col>
              </Row>
              <Row>
                <Col xs={1} />
                <Col xs={10}>
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
                <Col xs={1} />
              </Row>
              <Row>
                <Col xs={12}>&nbsp;</Col>
              </Row>
            </fieldset>
          </Col>
        </Row>
      </form>
    );
  }
}

DashboardTableFormBody.propTypes = {
  handleSubmit: PropTypes.func,
  onWillMount: PropTypes.func,
  onChangeToggleColumns: PropTypes.func.isRequired,
  invalid: PropTypes.bool,
  submitting: PropTypes.bool
};

DashboardTableFormBody.defaultProps = {
  invalid: false,
  submitting: false,
  handleSubmit: null,
  onWillMount: () => ({})
};
const DashboardTableForm = reduxForm({
  form: "form-list-devices"
})(DashboardTableFormBody);

export default DashboardTableForm;
