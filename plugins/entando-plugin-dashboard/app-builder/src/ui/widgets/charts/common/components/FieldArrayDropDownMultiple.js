import React, {Component} from "react";
import DropdownMultiple from "ui/common/form/DropdownMultiple";
import {formattedText} from "@entando/utils";

class FieldArrayDropDownMultiple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      columnsY: props.optionColumns || []
    };
    this.toogleSelected = this.toogleSelected.bind(this);
  }

  static getDerivedStateFromProps(nextProps, state) {
    const {optionColumns} = nextProps;
    if (optionColumns.length > 0 && state.columnsY.length === 0) {
      return {
        columnsY: optionColumns.map((m, index) => ({
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
    let temp = this.state.columnsY.find(f => f.key === key);
    temp.selected = !temp.selected;
    this.setState(prevState => ({
      columnsY: [...prevState.columnsY, ...temp]
    }));
    if (temp.selected) {
      this.props.fields.push(temp);
    } else {
      const {optionColumnYSelected} = this.props;
      optionColumnYSelected
        .map((m, index) => (m.key === key ? index : id))
        .forEach(index => {
          this.props.fields.remove(index);
        });
    }
  }

  render() {
    const {optionColumnYSelected} = this.props;
    return (
      <div className="FieldArrayDropDownMultiple">
        <div className="FieldArrayDropDownMultiple__dropdown">
          <DropdownMultiple
            titleHelper={formattedText(
              "plugin.chart.dropDownMulti.titleHelper"
            )}
            title={formattedText("plugin.chart.dropDownMulti.title")}
            list={this.state.columnsY}
            toggleItem={this.toogleSelected}
            searchValue=""
            searchNotFound={formattedText("common.DropdownMultiple.notFound")}
          />
        </div>
        <div className="FieldArrayDropDownMultiple__token-container">
          {optionColumnYSelected.map((item, index) => {
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
        </div>
      </div>
    );
  }
}

export default FieldArrayDropDownMultiple;
