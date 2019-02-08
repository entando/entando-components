import React from "react";
import PropTypes from "prop-types";
import {Field} from "redux-form";

const IconMarker = ({name}) => {
  return (
    <label className="IconMarker radio-inline">
      <Field component="input" type="radio" name="icon.marker" value={name} />
      <i className={`fa fa-fw ${name}`} />
    </label>
  );
};

IconMarker.propTypes = {
  name: PropTypes.string
};

IconMarker.defaultProps = {
  name: "fa-map-marker"
};
export default IconMarker;
