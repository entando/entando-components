import React, {Component} from "react";

class ColumnTokenArray extends Component {
  componentWillMount() {
    const {columns, fields} = this.props;
    if (columns.length > 0) {
      fields.push(columns);
    }
  }
  componentWillReceiveProps(nextProps) {
    const {columns, fields} = nextProps;
    if (columns.length !== fields.length) {
      fields.removeAll();
      columns.forEach(item => fields.push(item));
    }
  }

  render() {
    const {columns, className} = this.props;
    const classNames = `ColumnToken  ${className}`;
    return (
      <div className={classNames}>
        {columns.map((item, index) => (
          <div className="ColumnToken__column" key={index}>
            <span className="label label-info">{item.label}</span>
          </div>
        ))}
      </div>
    );
  }
}

export default ColumnTokenArray;
