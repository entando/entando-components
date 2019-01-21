import React, {Component} from "react";
import PropTypes from "prop-types";

class DropdownMultiple extends Component {
  constructor(props) {
    super(props);
    this.state = {
      listOpen: false,
      headerTitle: props.title,
      timeOut: null,
      listValue: props.list,
      searchValue: props.searchValue
    };
    this.close = this.close.bind(this);
    this.searchListValue = this.searchListValue.bind(this);
    this.clearSearch = this.clearSearch.bind(this);
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
    if (list.length > 0 && state.listValue.length === 0) {
      return {listValue: list};
    }
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

  searchListValue(ev) {
    const searchValue = ev.target.value;
    const {searchNotFound} = this.props;
    this.setState(() => {
      let listValue = [];
      if (searchValue.length <= 1) {
        listValue = this.props.list.filter(f => f.label.includes(searchValue));
      } else {
        const search = this.props.list.find(f =>
          f.label.startsWith(searchValue)
        );
        search
          ? listValue.push(search)
          : listValue.push({id: 0, key: "notFound", label: searchNotFound});
      }
      return {
        searchValue,
        listValue
      };
    });
  }

  clearSearch() {
    this.setState(() => ({
      searchValue: "",
      listValue: this.props.list
    }));
  }

  render() {
    const {toggleItem} = this.props;
    const {listValue, listOpen, headerTitle, searchValue} = this.state;

    return (
      <div className="DropdownMultiple">
        <div className="dd-header" onClick={() => this.toggleList()}>
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
                  onChange={this.searchListValue}
                />
                <span className="dd-list-search-clear">
                  <i
                    className="fa fa-times"
                    aria-hidden="true"
                    onClick={this.clearSearch}
                  />
                </span>
                <span className="input-group-addon" id="basic-search">
                  <i className="fa fa-search" aria-hidden="true" />
                </span>
              </div>
            </li>
            {listValue.map(item => (
              <li
                className={`dd-list-item ${item.selected ? "selected" : ""}`}
                key={item.label}
                onClick={() => toggleItem(item.id, item.key)}
              >
                {item.label}{" "}
                {item.selected && <i className="fa fa-check check" />}
              </li>
            ))}
          </ul>
        )}
      </div>
    );
  }
}

DropdownMultiple.propTypes = {
  titleHelper: PropTypes.string,
  title: PropTypes.string,
  list: PropTypes.arrayOf(PropTypes.shape({})),
  toggleItem: PropTypes.func,
  searchValue: PropTypes.string,
  searchNotFound: PropTypes.string
};

DropdownMultiple.defaultProps = {
  titleHelper: "",
  title: "",
  list: [],
  toggleItem: () => {},
  searchValue: "",
  searchNotFound: "Not Found"
};

export default DropdownMultiple;
