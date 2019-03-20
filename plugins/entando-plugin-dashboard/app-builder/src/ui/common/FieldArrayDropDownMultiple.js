import React, {Component} from "react";
import PropTypes from "prop-types";
import {formattedText} from "@entando/utils";
import DropdownMultiple from "ui/common/DropdownMultiple";

class FieldArrayDropDownMultiple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      columns: [],
      columnsOnInit: [],
      searchValue: "",
      idKey: undefined
    };
    this.toogleSelected = this.toogleSelected.bind(this);
    this.searchItem = this.searchItem.bind(this);
    this.clearSearch = this.clearSearch.bind(this);
  }

  refreshState(idKey, optionColumns) {
    this.setState({
      idKey,
      columns: optionColumns.map((m, index) => ({
        id: index,
        key: m.key,
        value: m.value,
        selected: m.selected || false
      }))
    });
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.fields && nextProps.fields.length > 0) {
      const {columns} = this.state;
      nextProps.optionColumnSelected.forEach(item => {
        const idx = columns.findIndex(el => el.key === item.key);
        if (idx !== -1) {
          columns[idx].selected = item.selected;
        }
      });
      this.setState({columns, columnsOnInit: columns});
    }
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.props.optionColumns.length > 0 && prevState.columns.length === 0) {
      this.refreshState(this.props.idKey, this.props.optionColumns);
    } else if (prevProps.idKey !== prevState.idKey) {
      this.props.fields.removeAll();
      this.refreshState(this.props.idKey, this.props.optionColumns);
    }
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
    const {columnsOnInit} = this.state;
    const listValue = optionColumns.filter(f =>
      f.value.toLowerCase().includes(searchValue.toLowerCase())
    );
    console.log("searchItem columnsOnInit ", columnsOnInit);
    if (listValue.length === 0) {
      listValue.push({
        id: 0,
        key: "notFound",
        value: formattedText("common.DropdownMultiple.notFound")
      });
    }
    listValue.forEach(item => {
      const idx = columnsOnInit.findIndex(f => f.value === item.value);
      if (idx !== -1) {
        item.selected = columnsOnInit[idx].selected;
      }
    });
    this.setState({
      columns: listValue,
      searchValue
    });
  }

  clearSearch(ev) {
    const searchValue = ev.target.value;
    this.setState({
      searchValue: "",
      columns: searchValue ? this.state.columnsOnInit : this.props.optionColumns
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
