import React, { Component } from 'react';
import PropTypes from 'prop-types';

class ColumnTokenArray extends Component {
  componentWillMount() {
    const { columns, fields } = this.props;
    if (columns.length > 0) {
      fields.push(columns);
    }
  }
  componentWillReceiveProps(nextProps) {
    const { columns, fields } = nextProps;
    if (columns.length !== fields.length) {
      fields.removeAll();
      columns.forEach(item => fields.push(item));
    }
  }

  render() {
    const { columns, className } = this.props;
    const classNames = `ColumnToken  ${className}`;
    return (
      <div className={classNames}>
        {columns.map(item => (
          <div className="ColumnToken__column" key={`col-${item.label}`}>
            <span className="label label-info">{item.label}</span>
          </div>
        ))}
      </div>
    );
  }
}

ColumnTokenArray.propTypes = {
  className: PropTypes.string,
  fields: PropTypes.shape({}),
  columns: PropTypes.arrayOf(PropTypes.shape({})),
};

ColumnTokenArray.defaultProps = {
  className: '',
  fields: {},
  columns: [],
};

export default ColumnTokenArray;
