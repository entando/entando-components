import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, FormSection } from 'redux-form';
import { Tabs, Tab, InputGroup } from 'patternfly-react';
import { formattedText, required, minLength, maxLength } from '@entando/utils';

import RenderTextInput from 'ui/common/form/RenderTextInput';
import FormLabel from 'ui/common/form/FormLabel';

const maxLength30 = maxLength(30);
const minLength3 = minLength(3);

class DashboardWidgetTitle extends Component {
  componentWillMount() {
    this.props.onWillMount();
  }

  render() {
    const tabs = this.props.languages
      .sort(a => (a.isDefault ? -1 : 1))
      .map((lang, i) => {
        const validate = [minLength3, maxLength30];
        if (lang.isDefault) {
          validate.push(required);
        }
        return (
          <Tab
            key={`lang-tab-${lang.code}`}
            eventKey={i}
            title={lang.isDefault ? `${lang.code}*` : lang.code}
            animation={false}
          >
            <FormSection name="title">
              <InputGroup className="DashboardWidgetTitle__input-group">
                <Field
                  component={RenderTextInput}
                  className="DashboardWidgetTitle__input-title"
                  name={lang.code}
                  label={
                    <FormLabel
                      labelId="plugin.table.widgetTitle"
                      helpId="plugin.table.widgetTitle.help"
                      required
                    />
                  }
                  alignClass="text-left"
                  validate={validate}
                  append={formattedText('plugin.table.requirement')}
                />
              </InputGroup>
            </FormSection>
          </Tab>
        );
      });

    return (
      <div className="DashboardWidgetTitle">
        <Tabs defaultActiveKey={0} id="labels-tabs">
          {tabs}
        </Tabs>
      </div>
    );
  }
}

DashboardWidgetTitle.propTypes = {
  languages: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  onWillMount: PropTypes.func.isRequired,
};

export default DashboardWidgetTitle;
