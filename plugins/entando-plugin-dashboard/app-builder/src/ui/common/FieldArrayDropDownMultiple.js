import React, {Component} from "react";
import PropTypes from "prop-types";
import DropdownMultiple from "ui/common/DropdownMultiple";
import {formattedText} from "@entando/utils";

class FieldArrayDropDownMultiple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      columns: props.optionColumns || []
    };
    this.toogleSelected = this.toogleSelected.bind(this);
  }

  static getDerivedStateFromProps(nextProps, state) {
    const {optionColumns} = nextProps;
    if (optionColumns.length > 0 && state.columns.length === 0) {
      return {
        columns: optionColumns.map((m, index) => ({
          id: index,
          key: m.key,
          label: m.value,
          selected: false
        }))
      };
    }
    return null;
  }

  toogleSelected(id, key) {
    let temp = this.state.columns.find(f => f.key === key);
    temp.selected = !temp.selected;
    this.setState(prevState => ({
      columns: [...prevState.columns, ...temp]
    }));
    if (temp.selected) {
      this.props.fields.push(temp);
    } else {
      const {optionColumnSelected} = this.props;
      optionColumnSelected
        .map((m, index) => (m.key === key ? index : -1))
        .forEach(index => {
          if (index > -1) {
            this.props.fields.remove(index);
          }
        });
    }
  }

  render() {
    const {
      optionColumnSelected,
      className,
      meta: {error}
    } = this.props;
    let classNames = `FieldArrayDropDownMultiple ${className}`;
    return (
      <div className={classNames}>
        <div className="FieldArrayDropDownMultiple__dropdown">
          <DropdownMultiple
            titleHelper={formattedText(
              "plugin.chart.dropDownMulti.titleHelper"
            )}
            title={formattedText("plugin.chart.dropDownMulti.title")}
            list={this.state.columns}
            toggleItem={this.toogleSelected}
            searchValue=""
            searchNotFound={formattedText("common.DropdownMultiple.notFound")}
          />
        </div>
        <div className="FieldArrayDropDownMultiple__token-container">
          {optionColumnSelected.map((item, index) => {
            return (
              <div className="FieldArrayDropDownMultiple__token" key={index}>
                <span className="label label-info FieldArrayDropDownMultiple__token-clear">
                  <i
                    className="fa fa-times"
                    onClick={() => this.toogleSelected(index, item.key)}
                  />
                </span>
                <span className="label label-info FieldArrayDropDownMultiple__token-label">
                  {item.label}
                </span>
              </div>
            );
          })}
          {error && <span className="help-block">{error}</span>}
        </div>
      </div>
    );
  }
}

const COLUMN_TYPE = {
  key: PropTypes.PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  label: PropTypes.string
};

FieldArrayDropDownMultiple.propTypes = {
  className: PropTypes.string,
  optionColumn: PropTypes.arrayOf(PropTypes.shape(COLUMN_TYPE)),
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})),
  fields: PropTypes.shape({
    push: PropTypes.func.isRequired,
    remove: PropTypes.func.isRequired
  }).isRequired
};
FieldArrayDropDownMultiple.defaultProps = {
  className: "",
  optionColumns: [],
  optionColumnSelected: []
};

export default FieldArrayDropDownMultiple;
