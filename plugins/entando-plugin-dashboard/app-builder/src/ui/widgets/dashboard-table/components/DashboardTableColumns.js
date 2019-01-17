import React, {Component} from "react";
import PropTypes from "prop-types";
import {Field, FormSection} from "redux-form";
import {maxLength} from "@entando/utils";
import FormattedMessage from "ui/i18n/FormattedMessage";
import {OverlayTrigger, Tooltip} from "patternfly-react";

import {
  SortableContainer,
  SortableElement,
  arrayMove
} from "react-sortable-hoc";

const maxLength15 = maxLength(15);

const renderField = ({input, meta: {touched, error}}) => {
  const classContainer =
    touched && error
      ? "DashboardTableColumns__container-input--error"
      : "DashboardTableColumns__container-input";
  return (
    <div className={classContainer}>
      <input
        className="DashboardTableColumns__input"
        id={input.name}
        {...input}
        type="text"
      />
      {touched && (error && <span className="help-block">{error}</span>)}
    </div>
  );
};

const SortableItem = SortableElement(({item, showColumnHandler}) => (
  <th className="DashboardTableColumns__th-editable-label">
    <div className="DashboardTableColumns__th-editable-label-container">
      <Field component={renderField} name={item.key} validate={[maxLength15]} />
      {showColumn(item, showColumnHandler)}
    </div>
  </th>
));

const showColumn = (item, onClickHandler) => (
  <OverlayTrigger
    overlay={
      <Tooltip id={item.key}>
        <FormattedMessage
          id={`plugin.table.column.tooltip.${item.hidden ? "show" : "hidden"}`}
        />
      </Tooltip>
    }
    placement="top"
    trigger={["hover"]}
    rootClose={false}
  >
    <i
      className={`DashboardTableColumns__th-column-${
        item.hidden ? "hidden" : "show"
      }`}
      onClick={() => onClickHandler(item.key)}
    />
  </OverlayTrigger>
);

const SortableList = SortableContainer(({items, showColumnHandler}) => {
  return (
    <FormSection name="columns">
      <div className="DashboardTableColumns__container_columns">
        <label>
          {items.length > 0 ? (
            <FormattedMessage id={"plugin.table.column.default.label"} />
          ) : null}
        </label>
        <table className="DashboardTableColumns__table table">
          <thead>
            <tr>
              {items.map((item, index) => (
                <th
                  key={`item-${index}`}
                  className="DashboardTableColumns__th-default-label"
                >
                  {item.key}
                </th>
              ))}
            </tr>
          </thead>
        </table>
        <label>
          {items.length > 0 ? (
            <FormattedMessage id={"plugin.table.column.customizable.label"} />
          ) : null}
        </label>
        <table className="DashboardTableColumns__table table table-striped table-bordered">
          <thead>
            <tr>
              {items.map((item, index) => (
                <SortableItem
                  key={`item-${index}`}
                  index={index}
                  item={item}
                  showColumnHandler={showColumnHandler}
                />
              ))}
            </tr>
          </thead>
        </table>
      </div>
    </FormSection>
  );
});

class DashboardTableColumns extends Component {
  constructor(props) {
    super(props);
    this.onMoveHandler = this.onMoveHandler.bind(this);
  }

  onMoveHandler({oldIndex, newIndex}) {
    const {onMoveColumn, columns} = this.props;
    onMoveColumn(arrayMove(columns, oldIndex, newIndex));
  }

  render() {
    return (
      <div className="DashboardTableColumns">
        <SortableList
          items={this.props.columns}
          showColumnHandler={this.props.onShowHideColumn}
          formValues={this.props.formValues}
          lockAxis="x"
          axis="x"
          pressDelay={100}
          onSortEnd={this.onMoveHandler}
        />
      </div>
    );
  }
}

DashboardTableColumns.propTypes = {
  columns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  onMoveColumn: PropTypes.func.isRequired,
  onShowHideColumn: PropTypes.func.isRequired
};

export default DashboardTableColumns;