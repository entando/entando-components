import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Field, reduxForm} from 'redux-form';
import {required, maxLength} from '@entando/utils';
import {InputGroup, FormGroup, Row, Col, ControlLabel} from 'patternfly-react';
import FormattedMessage from 'ui/i18n/FormattedMessage';
import RenderTextInput from 'ui/common/form/RenderTextInput';
import RenderSelectInput from 'ui/common/form/RenderSelectInput';
import SwitchRenderer from 'ui/common/form/SwitchRenderer';
import FormLabel from 'ui/common/form/FormLabel';

const uppercaseThreeLetters = value => (
  value && !/[A-Z]$/g.test(value)
  ? <FormattedMessage id="validateForm.element.code"/>
  : undefined);
const maxLength30 = maxLength(30);
const minLength3 = maxLength(3);
const maxLength50 = maxLength(50);

class TableListDevicesFormBody extends Component {
  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount();
    }
  }

  renderWidgetTitle() {
    return (<InputGroup className="TableListDevicesForm__input-group">
      <Field component={RenderTextInput} className="TableListDevicesForm__input-title" name="title" label={<FormLabel labelId = "plugin.table.widgetTitle" helpId = "plugin.table.widgetTitle.help" required />} alignClass="text-left" validate={[required, uppercaseThreeLetters, minLength3, maxLength30]}/>
    </InputGroup>);
  }
  renderDataSource() {
    const selectOptionsDataSource = [
      {
        value: 'entando-iot-server',
        text: 'Entando IoT Server'
      }
    ];
    return (<InputGroup className="TableListDevicesForm__input-group">
      <Field component={RenderSelectInput} options={selectOptionsDataSource} defaultOptionId="plugin.chooseAnOptionDataSource" label={<FormLabel labelId = "plugin.datasource" helpId = "plugin.datasource.help" required />} labelSize={4} alignClass="text-left" name="datasource"/>
    </InputGroup>);
  }
  renderDownlodable() {
    return (<FormGroup className="TableListDevicesForm__input-group">
      <Col smOffset={2} sm={10}>
        <div className="checkbox">
          <label >
            <Field component="input" type="checkbox" name="downlodable"/>
            <strong>
              <FormattedMessage id="plugin.table.downlodable"/>
            </strong>
            <br/>
            <FormattedMessage id="plugin.table.downlodable.format"/>
          </label>
        </div>
      </Col>
    </FormGroup>);
  }
  renderContext() {
    const selectOptionsContext = [
      {
        value: 'parking',
        text: 'Parking'
      }, {
        value: 'bikeSharing',
        text: 'Bike Sharking'
      }
    ];
    return (<InputGroup className="TableListDevicesForm__input-group">
      <Field component={RenderSelectInput} options={selectOptionsContext} defaultOptionId="plugin.chooseAnOptionContext" label={<FormLabel labelId = "plugin.kindOfContext" helpId = "plugin.kindOfContext.help" required />} labelSize={4} alignClass="text-left" name="datasource"/>
    </InputGroup>);
  }
  renderFiltrable() {
    return (<FormGroup className="TableListDevicesForm__input-group">
      <Col smOffset={2} sm={10}>
        <div className="checkbox">
          <label >
            <Field component="input" type="checkbox" name="downlodable"/>
            <strong>
              <FormattedMessage id="plugin.table.filterable"/>
            </strong>
          </label>
        </div>
      </Col>
    </FormGroup>);
  }
  renderColumnInformation() {
    return (<InputGroup className="TableListDevicesForm__input-group">
      <div className="form-group">
        <Col xs={4} className="text-left">
          <ControlLabel htmlFor="allColumns">
            <strong><FormattedMessage id="plugin.table.allColumns"/></strong>
          </ControlLabel>
        </Col>
        <Col xs={8}>
          <Field component={SwitchRenderer} name="allColumns" trueValue="trueValue" falseValue={false}/>
        </Col>
      </div>
    </InputGroup>);
  }
  renderColumnDetail() {
    return (<Col xs={10} className="TableListDevicesForm__container-data">
      <Row>
        <Col xs={4}>
          <FormGroup className="TableListDevicesForm__input-group">
            <Col xs={10}>
              <div className="checkbox">
                <label >
                  <Field component="input" type="checkbox" name="deviceStatus"/>
                  <strong>
                    <FormattedMessage id="plugin.table.deviceStatus"/>
                  </strong><br/>
                  <FormattedMessage id="plugin.table.deviceStatus.help"/>
                </label>
              </div>
            </Col>
          </FormGroup>
        </Col>
        <Col xs={4}>
          <FormGroup className="TableListDevicesForm__input-group">
            <Col xs={10}>
              <div className="checkbox">
                <label >
                  <Field component="input" type="checkbox" name="deviceUse"/>
                  <strong>
                    <FormattedMessage id="plugin.table.deviceUse"/>
                  </strong><br/>
                  <FormattedMessage id="plugin.table.deviceUse.help"/>
                </label>
              </div>
            </Col>
          </FormGroup>
        </Col>
        <Col xs={4}>
          <FormGroup className="TableListDevicesForm__input-group">
            <Col xs={10}>
              <div className="checkbox">
                <label >
                  <Field component="input" type="checkbox" name="batteryLevel"/>
                  <strong>
                    <FormattedMessage id="plugin.table.batteryLevel"/>
                  </strong><br/>
                  <FormattedMessage id="plugin.table.batteryLevel.help"/>
                </label>
              </div>
            </Col>
          </FormGroup>
        </Col>
      </Row>
      <Row>
        <Col xs={4}>
          <FormGroup className="TableListDevicesForm__input-group">
            <Col xs={10}>
              <div className="checkbox">
                <label >
                  <Field component="input" type="checkbox" name="deviceCode"/>
                  <strong>
                    <FormattedMessage id="plugin.table.deviceCode"/>
                  </strong>
                </label>
              </div>
            </Col>
          </FormGroup>
        </Col>
        <Col xs={4}>
          <FormGroup className="TableListDevicesForm__input-group">
            <Col xs={10}>
              <div className="checkbox">
                <label >
                  <Field component="input" type="checkbox" name="deviceBrand"/>
                  <strong>
                    <FormattedMessage id="plugin.table.deviceBrand"/>
                  </strong>
                </label>
              </div>
            </Col>
          </FormGroup>
        </Col>
        <Col xs={4}>
          <FormGroup className="TableListDevicesForm__input-group">
            <Col xs={10}>
              <div className="checkbox">
                <label >
                  <Field component="input" type="checkbox" name="expirationGuarantee"/>
                  <strong>
                    <FormattedMessage id="plugin.table.expirationGuarantee"/>
                  </strong>
                </label>
              </div>
            </Col>
          </FormGroup>
        </Col>
      </Row>
    </Col>);
  }
  render() {
    // const selectOptions = attributesType.map(item => ({
    //   value: item,
    //   text: item,
    // }));
    const {handleSubmit, onSubmit, invalid, submitting} = this.props;
    return (<form onSubmit={handleSubmit} className="TableListDevicesForm form-horizontal">
      <Row>
        <Col xs={12}>
          <fieldset className="no-padding">
            <div className="TableListDevicesForm__required-fields text-right">
              *<FormattedMessage id="plugin.fieldsRequired"/>
            </div>
            <Row>
              <Col xs={1}/>
              <Col xs={10} className="TableListDevicesForm__container-data">
                <div className="TableListDevicesForm__description">
                  <FormattedMessage id="plugin.table.description"/>
                </div>
              </Col>
              <Col xs={1}/>
            </Row>
            <Row>
              <Col xs={1}/>
              <Col xs={10} className="TableListDevicesForm__container-data TableListDevicesForm__widget-title">
                {this.renderWidgetTitle()}
              </Col>
              <Col xs={1}/>
            </Row>
            <Row >
              <Col xs={1}/>
              <Col xs={10} className="TableListDevicesForm__container-data TableListDevicesForm__line">
                <Row>
                  <Col xs={6}>
                    {this.renderDataSource()}
                  </Col>
                  <Col xs={6}>
                    {this.renderDownlodable()}
                  </Col>
                </Row>
                <Row>
                  <Col xs={6}>
                    {this.renderContext()}
                  </Col>
                  <Col xs={6}>
                    {this.renderFiltrable()}
                  </Col>
                </Row>
              </Col>
              <Col xs={1}/>
            </Row>
            <Row >
              <Col xs={1}/>
              <Col xs={10} className="TableListDevicesForm__container-data">
                <Row>
                  <Col xs={6}>
                    {this.renderColumnInformation()}
                  </Col>
                </Row>
              </Col>
              <Col xs={1}/>
            </Row>
            <Row >
              <Col xs={1}/>
              <Col xs={10} className="TableListDevicesForm__container-data">
                <Row>
                  <Col xs={6}>
                    <div className="TableListDevicesForm__more-information">
                      <strong><FormattedMessage id="plugin.table.moreInformation"/></strong>
                      <br/>
                      <FormattedMessage id="plugin.table.moreInformation.choose"/>
                    </div>
                  </Col>
                </Row>
              </Col>
              <Col xs={1}/>
            </Row>
            <Row >
              <Col xs={1}/> {this.renderColumnDetail()}
            </Row>
          </fieldset>
        </Col>
      </Row>
    </form>);
  }
}
TableListDevicesFormBody.propTypes = {
  handleSubmit: PropTypes.func,
  onSubmit: PropTypes.func,
  onWillMount: PropTypes.func,
  invalid: PropTypes.bool,
  submitting: PropTypes.bool
};
TableListDevicesFormBody.defaultProps = {
  invalid: false,
  submitting: false,
  onSubmit: null,
  handleSubmit: null,
  onWillMount: () => ({})
};
const TableListDevicesForm = reduxForm({form: 'form-list-devices'})(TableListDevicesFormBody);
export default TableListDevicesForm;
