import React, { Component } from 'react';
import PropTypes from 'prop-types';
import FormattedMessage from '../i18n/FormattedMessage';


class MilestonesTableRenderer extends Component {
  componentWillReceiveProps(nextProps) {
    // pushes milestones the first time it loads
    if (!this.props.initialValues && nextProps.initialValues) {
      nextProps.initialValues.forEach(this.props.fields.push);
    }
  }

  render() {
    const { milestones, fields } = this.props;

    return (
      <table className="table">
        <thead>
          <tr>
            <th><FormattedMessage id="BpmCaseProgressStatusForm.visible" /></th>
            <th><FormattedMessage id="BpmCaseProgressStatusForm.milestoneName" /></th>
            <th><FormattedMessage id="BpmCaseProgressStatusForm.completed" /></th>
          </tr>
        </thead>
        <tbody>
          {milestones && milestones.map((item, i) => (
            <tr key={item['milestone-id']}>
              <td>
                {!console.log(item)}
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
              <td>
                {item['milestone-name']}
              </td>
              <td>
                <input
                  type="number"
                  value={item.percentage}
                  onChange={(ev) => {
                    const prev = fields.get(i);
                    fields.remove(i);
                    fields.insert(i, { ...prev, percentage: parseInt(ev.currentTarget.value, 10) });
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


MilestonesTableRenderer.propTypes = {
  fields: PropTypes.shape({
    push: PropTypes.func.isRequired,
  }).isRequired,
  milestones: PropTypes.arrayOf(PropTypes.shape({})),
  initialValues: PropTypes.arrayOf(PropTypes.shape({})),
};

MilestonesTableRenderer.defaultProps = {
  milestones: null,
  initialValues: null,
};

export default MilestonesTableRenderer;
