import React, {Component} from "react";

class ColumnTokenArray extends Component {
  componentWillMount() {
    console.log("componentWillMount - props", this.props);
    const {columns, fields} = this.props;
    if (columns.length > 0) {
      fields.push(columns);
    }
  }
  componentWillReceiveProps(nextProps) {
    console.log(
      "componentWillReceiveProps - nextProps",
      nextProps,
      "props",
      this.props
    );
    const {columns, fields} = nextProps;
    if (columns.length !== fields.length) {
      console.log("entro qui");
      fields.removeAll();
      columns.forEach(item => fields.push(item));
    }
  }

  render() {
    const {columns, className, fields} = this.props;
    const classNames = `ColumnToken  ${className}`;

    return (
      <div className={classNames}>
        {columns.map((item, index) => (
          <div className="ColumnToken__column" key={index}>
            <span className="label label-info">
              <i
                className="fa fa-fw fa-times ColumnToken__btn-remove"
                onClick={() => fields.remove(index)}
              />
              {item.label}
            </span>
          </div>
        ))}
      </div>
    );
  }
}

export default ColumnTokenArray;
