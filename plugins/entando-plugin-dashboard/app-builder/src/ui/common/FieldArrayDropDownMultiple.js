import React, {Component} from "react";
import PropTypes from "prop-types";
import {isEqual} from "lodash";
import {formattedText} from "@entando/utils";
import DropdownMultiple from "ui/common/DropdownMultiple";

class FieldArrayDropDownMultiple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      columns: props.optionColumns || [],
      searchValue: "",
      idKey: props.idKey
    };
    this.toogleSelected = this.toogleSelected.bind(this);
    this.searchItem = this.searchItem.bind(this);
    this.clearSearch = this.clearSearch.bind(this);
  }

  static getDerivedStateFromProps(nextProps, state) {
    const {idKey, optionColumns, fields} = nextProps;
    // console.log("getDerivedStateFromProps props idKey ", nextProps.idKey);
    // console.log("getDerivedStateFromProps state idKey ", state.idKey);
    if (optionColumns.length > 0 && state.columns.length === 0) {
      console.log("Init");
      return {
        idKey,
        columns: optionColumns.map((m, index) => ({
          id: index,
          key: m.key,
          value: m.value,
          selected: false
        }))
      };
    } else if (idKey !== state.idKey) {
      if (!isEqual(optionColumns, state.columns)) {
        // console.log("cambio datasource");
        // console.log("state optionColumns", state.columns);
        fields.removeAll();
        return {
          idKey,
          columns: optionColumns.map((m, index) => ({
            id: index,
            key: m.key,
            value: m.value,
            selected: false
          }))
        };
      }
    }
    return null;
  }

  toogleSelected(id, key) {
    const {nameFieldArray, fields} = this.props;
    let temp = this.state.columns.find(f => f.key === key);
    if (temp) {
      temp.selected = !temp.selected;
      this.setState(prevState => ({
        columns: [...prevState.columns, ...temp]
      }));
      if (temp.selected) {
        const {addColumnOptionSelected} = this.props;
        fields.push(temp);
        addColumnOptionSelected &&
          addColumnOptionSelected(nameFieldArray, temp);
      } else {
        const {optionColumnSelected, removeColumnOptionSelected} = this.props;

        optionColumnSelected
          .map((m, index) => (m.key === key ? index : -1))
          .forEach(index => {
            if (index > -1) {
              fields.remove(index);
              removeColumnOptionSelected &&
                removeColumnOptionSelected(nameFieldArray, index);
            }
          });
      }
    }
  }

  searchItem(ev) {
    const searchValue = ev.target.value;
    const {optionColumns} = this.props;
    let listValue = [];
    if (searchValue.length <= 1) {
      listValue = optionColumns.filter(f =>
        f.value.toLowerCase().includes(searchValue.toLowerCase())
      );
      if (listValue.length === 0) {
        listValue.push({
          id: 0,
          key: "notFound",
          value: formattedText("common.DropdownMultiple.notFound")
        });
      }
    } else {
      const search = optionColumns.find(f =>
        f.value.toLowerCase().startsWith(searchValue.toLowerCase())
      );
      search
        ? listValue.push(search)
        : listValue.push({
            id: 0,
            key: "notFound",
            value: formattedText("common.DropdownMultiple.notFound")
          });
    }

    this.setState({
      columns: listValue,
      searchValue
    });
  }

  clearSearch() {
    this.setState({
      searchValue: ""
    });
  }

  render() {
    const {
      optionColumnSelected,
      className,
      meta: {error},
      disabled
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
            searchItem={this.searchItem}
            searchValue={this.state.searchValue}
            clearSearch={this.clearSearch}
            disabled={disabled}
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
                  {item.value}
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
  value: PropTypes.string,
  selected: PropTypes.bool
};

FieldArrayDropDownMultiple.propTypes = {
  className: PropTypes.string,
  optionColumns: PropTypes.arrayOf(PropTypes.shape(COLUMN_TYPE)),
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})),
  fields: PropTypes.shape({
    push: PropTypes.func.isRequired,
    remove: PropTypes.func.isRequired
  }).isRequired,
  disabled: PropTypes.bool
};
FieldArrayDropDownMultiple.defaultProps = {
  className: "",
  optionColumns: [],
  optionColumnSelected: [],
  disabled: false
};

export default FieldArrayDropDownMultiple;
