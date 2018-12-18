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
import SwitchRenderer from "ui/common/form/SwitchRenderer";
import FormLabel from "ui/common/form/FormLabel";
import ServerNameFormContainer from "ui/widgets/common/form/Containers/ServerNameFormContainer";
import DatasourceFormContainer from "ui/widgets/common/form/Containers/DatasourceFormContainer";
import DashboardTableColumnsContainer from "ui/widgets/dashboard-table/Containers/DashboardTableColumnsContainer";

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
          append={<FormattedMessage id="plugin.table.requirement" />}
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
                      <ServerNameFormContainer />
                    </Col>
                    <Col xs={6}>{this.renderDownlodable()}</Col>
                  </Row>
                  <Row>
                    <Col xs={6}>
                      <DatasourceFormContainer />
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
                    <Col xs={8}>
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
                <Col
                  xs={10}
                  className={
                    this.state.toggleAllColums
                      ? "DashboardTableForm__container-data--disabled"
                      : "DashboardTableForm__container-data"
                  }
                >
                  <DashboardTableColumnsContainer />
                </Col>
                <Col xs={1} />
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
