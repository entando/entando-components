import React, {Component} from "react";
import PropTypes from "prop-types";
import {uniqueId} from "lodash";

class DropdownMultiple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      listOpen: false,
      headerTitle: props.title,
      timeOut: null
    };
    this.close = this.close.bind(this);
  }

  componentDidUpdate() {
    const {listOpen} = this.state;
    setTimeout(() => {
      if (listOpen) {
        window.addEventListener("click", this.close);
      } else {
        window.removeEventListener("click", this.close);
      }
    }, 0);
  }

  componentWillUnmount() {
    window.removeEventListener("click", this.close);
  }

  close(timeOut) {
    this.setState({
      listOpen: false
    });
  }

  static getDerivedStateFromProps(nextProps, state) {
    const {list, title, titleHelper} = nextProps;
    //console.log("DropdownMultiple - list ", list);
    const count = list.filter(f => f.selected).length;
    if (count === 0) {
      return {headerTitle: title};
    } else if (count === 1) {
      return {headerTitle: `${count} ${titleHelper}`};
    } else if (count > 1) {
      return {headerTitle: `${count} ${titleHelper}s`};
    }
  }

  toggleList() {
    this.setState(prevState => ({
      listOpen: !prevState.listOpen
    }));
  }

  render() {
    const {
      toggleItem,
      disabled,
      searchItem,
      searchValue,
      list,
      clearSearch
    } = this.props;
    const {listOpen, headerTitle} = this.state;
    return (
      <div className={`DropdownMultiple ${disabled ? "disabled" : ""}`}>
        <div
          className="dd-header "
          onClick={() => (!disabled ? this.toggleList() : null)}
          disabled={disabled}
        >
          <div className="dd-header-title">{headerTitle}</div>
          {listOpen ? (
            <i className="fa fa-angle-up angle-up" />
          ) : (
            <i className="fa fa-angle-down angle-down" />
          )}
        </div>
        {listOpen && (
          <ul className="dd-list" onClick={e => e.stopPropagation()}>
            <li className="dd-list-search">
              <div className="input-group">
                <input
                  type="text"
                  className="form-control"
                  aria-describedby="basic-search"
                  value={searchValue}
                  onChange={searchItem}
                />
                <span className="dd-list-search-clear">
                  <i
                    className="fa fa-times"
                    aria-hidden="true"
                    onClick={clearSearch}
                  />
                </span>
                <span className="input-group-addon">
                  <i className="fa fa-search" aria-hidden="true" />
                </span>
              </div>
            </li>
            {list.map(item => {
              return (
                <li
                  className={`dd-list-item ${item.selected ? "selected" : ""}`}
                  key={`${item.value}_${uniqueId()}`}
                  onClick={() => toggleItem(item.id, item.key)}
                >
                  {item.value}{" "}
                  {item.selected && <i className="fa fa-check check" />}
                </li>
              );
            })}
          </ul>
        )}
      </div>
    );
  }
}

const COLUMN_TYPE = {
  id: PropTypes.number,
  key: PropTypes.PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  value: PropTypes.string,
  selected: PropTypes.bool
};

DropdownMultiple.propTypes = {
  titleHelper: PropTypes.string,
  title: PropTypes.string,
  list: PropTypes.arrayOf(PropTypes.shape(COLUMN_TYPE)),
  toggleItem: PropTypes.func,
  searchValue: PropTypes.string,
  searchNotFound: PropTypes.string,
  caseSensitiveSearch: PropTypes.bool
};

DropdownMultiple.defaultProps = {
  titleHelper: "",
  title: "",
  list: [],
  toggleItem: () => {},
  searchValue: "",
  searchNotFound: "Not Found",
  caseSensitiveSearch: true
};

export default DropdownMultiple;
