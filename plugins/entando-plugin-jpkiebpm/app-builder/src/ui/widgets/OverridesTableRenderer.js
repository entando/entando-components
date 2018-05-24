
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import FormattedMessage from '../i18n/FormattedMessage';


class OverridesTableRenderer extends Component {
  componentWillReceiveProps(nextProps) {
    // pushes overrides the first time it loads
    if (!(this.props.initialValues && this.props.initialValues.length) && nextProps.initialValues) {
      nextProps.initialValues.forEach(this.props.fields.push);
    }
  }

  render() {
    const { overrides, fields } = this.props;

    return (
      <table className="grid table table-bordered table-hover">
        <thead>
          <tr>
            <th className="text-center table-w-5">
              <FormattedMessage id="BpmDatatableTaskListForm.position" />
            </th>
            <th className="table-w-20">
              <FormattedMessage id="BpmDatatableTaskListForm.columnName" />
            </th>
            <th className="text-center table-w-5">
              <FormattedMessage id="BpmDatatableTaskListForm.visible" />
            </th>
            <th className="text-center table-w-20">
              <FormattedMessage id="BpmDatatableTaskListForm.overrideFields" />
            </th>
          </tr>
        </thead>
        <tbody>
          {overrides && overrides.map((item, i) => (
            <tr key={item.id} className="ui-sortable-handle">
              <td className="index text-center">
                { i + 1 }
              </td>
              <td className="field text-center">
                { item.field }
              </td>
              <td className="text-center">
                <input
                  type="checkbox"
                  checked={item.visible}
                  onChange={(ev) => {
                    const prev = fields.get(i);
                    fields.remove(i);
                    fields.insert(i, { ...prev, visible: ev.currentTarget.checked });
                  }}
                />
              </td>
              <td className="text-center">
                <input
                  type="text"
                  value={item.fieldOverride}
                  onChange={(ev) => {
                    const prev = fields.get(i);
                    fields.remove(i);
                    fields.insert(i, { ...prev, fieldOverride: ev.currentTarget.value });
                  }}
                />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    );
  }
}


OverridesTableRenderer.propTypes = {
  fields: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  overrides: PropTypes.arrayOf(PropTypes.shape({})),
  initialValues: PropTypes.arrayOf(PropTypes.shape({})),
};

OverridesTableRenderer.defaultProps = {
  overrides: null,
  initialValues: null,
};

export default OverridesTableRenderer;
