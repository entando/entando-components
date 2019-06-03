import React from 'react';
import PropTypes from 'prop-types';
import { Field, FieldArray } from 'redux-form';
import { Grid, Row, Col, InputGroup } from 'patternfly-react';
import DatasourceFormContainer from 'ui/widgets/common/form/containers/DatasourceFormContainer';
import { formattedText, minLength, maxLength } from '@entando/utils';

import RenderTextInput from 'ui/common/form/RenderTextInput';
import FormLabel from 'ui/common/form/FormLabel';
import FieldArrayDropDownMultiple from 'ui/common/FieldArrayDropDownMultiple';
import FormattedMessage from 'ui/i18n/FormattedMessage';
import IconMarker from './IconMarker';

const maxLength20 = maxLength(20);
const minLength3 = minLength(3);

const ICONS_MARKER_1 = [
  'fa-map-marker',
  'fa-map-pin',
  'fa-circle',
  'fa-exclamation-triangle',
  'fa-flag',
  'fa-star',
  'fa-street-view',
  'fa-bed',
];
const ICONS_MARKER_2 = [
  'fa-male',
  'fa-female',
  'fa-car',
  'fa-bus',
  'fa-bicycle',
  'fa-train',
  'fa-plane',
  'fa-ship',
];

const DatasourceLayer = ({
  formName,
  optionColumns,
  optionColumnSelected,
  optionColumnDeviceLocationsSelected,
  nameFieldArray,
  addColumnOptionSelected,
  removeColumnOptionSelected,
  datasourceSelected,
  disabled,
}) => (
  <Grid className="DatasourceLayer">
    <Row>
      <Col xs={6} className="DatasourceLayer__datasource">
        <DatasourceFormContainer
          formName={formName}
          nameFieldArray={nameFieldArray}
          disabled={disabled}
          labelSize={5}
          isRequired={false}
        />
      </Col>
      <Col xs={6} className="DatasourceLayer__label">
        <InputGroup className="DatasourceLayer__input-group">
          <Field
            component={RenderTextInput}
            name={nameFieldArray ? `${nameFieldArray}[label]` : 'label'}
            label={
              <FormLabel labelId="plugin.geolocalization.label" required />
            }
            alignClass="DatasourceLayer__align-class"
            labelSize={2}
            validate={[minLength3, maxLength20]}
            append={formattedText('plugin.geolocalization.label.requirement')}
          />
        </InputGroup>
      </Col>
      {optionColumns.length === 0 ? null : (
        <Col xs={12} className="DatasourceLayer__icons-marker-container">
          <Col xs={2}>
            <div className="form-group">
              <strong>
                <FormLabel
                  labelId="plugin.geolocalization.icons"
                  helpId="plugin.geolocalization.icons.help"
                />
              </strong>
            </div>
          </Col>
          <Col xs={10} className="DatasourceLayer__icons-marker">
            <div>
              {ICONS_MARKER_1.map(name => (
                <IconMarker key={name} name={name} />
              ))}
            </div>
            <div>
              {ICONS_MARKER_2.map(name => (
                <IconMarker key={name} name={name} />
              ))}
            </div>
          </Col>
        </Col>
      )}
      <Col xs={6}>
        { console.log('optionColumns: ', optionColumns)}
        { console.log('optionColumnSelected: ', optionColumnSelected)}
        { console.log('optionColumnDeviceLocationsSelected: ', optionColumnDeviceLocationsSelected)}
        {optionColumns.length === 0 ? null : (
          <div className="form-group">
            <label htmlFor="label-coordinates" className="col-xs-2 control-label">
              <FormattedMessage id="plugin.geolocalization.coordinates" />
            </label>
            <Col xs={9} className="DatasourceLayer__item">
              <FieldArray
                id="label-coordinates"
                className="DatasourceLayer__column-selected"
                name={
                  nameFieldArray ?
                    `${nameFieldArray}[datasourceColumnsDeviceLocations]` :
                    'datasourceColumnsDeviceLocations'
                }
                component={FieldArrayDropDownMultiple}
                idKey={datasourceSelected}
                optionColumns={optionColumns}
                optionColumnSelected={optionColumnDeviceLocationsSelected}
                disabled={optionColumns.length === 0}
                addColumnOptionSelected={addColumnOptionSelected}
                removeColumnOptionSelected={removeColumnOptionSelected}
                nameFieldArray={
                  nameFieldArray ?
                    `${nameFieldArray}[optionColumnDeviceLocationsSelected]` :
                    null
                }
              />
            </Col>
          </div>
        )}
      </Col>
      <Col xs={6}>
        {optionColumns.length === 0 ? null : (
          <div className="form-group">
            <label htmlFor="label-item" className="col-xs-2 control-label">
              <FormattedMessage id="plugin.geolocalization.item" />
            </label>
            <Col xs={10} className="DatasourceLayer__item">
              <FieldArray
                id="label-item"
                className="DatasourceLayer__column-selected"
                name={
                  nameFieldArray ?
                    `${nameFieldArray}[datasourceColumns]` :
                    'datasourceColumns'
                }
                component={FieldArrayDropDownMultiple}
                idKey={datasourceSelected}
                optionColumns={optionColumns}
                optionColumnSelected={optionColumnSelected}
                disabled={optionColumns.length === 0}
                addColumnOptionSelected={addColumnOptionSelected}
                removeColumnOptionSelected={removeColumnOptionSelected}
                nameFieldArray={
                  nameFieldArray ?
                    `${nameFieldArray}[optionColumnSelected]` :
                    null
                }
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

DatasourceLayer.propTypes = {
  datasourceSelected: PropTypes.string,
  formName: PropTypes.string,
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})),
  optionColumnDeviceLocationsSelected: PropTypes.arrayOf(PropTypes.shape({})),
  nameFieldArray: PropTypes.string,
  addColumnOptionSelected: PropTypes.func,
  removeColumnOptionSelected: PropTypes.func,
  disabled: PropTypes.bool,
};

DatasourceLayer.defaultProps = {
  datasourceSelected: '',
  formName: '',
  optionColumnSelected: [],
  optionColumnDeviceLocationsSelected: [],
  nameFieldArray: undefined,
  addColumnOptionSelected: () => null,
  removeColumnOptionSelected: () => null,
  disabled: false,
};

export default DatasourceLayer;
