import React, {Component} from "react";
import {reduxForm} from "redux-form";
import PropTypes from "prop-types";

class DashboardConfigFormBody extends Component {
  render() {
    const {handleSubmit} = this.props;
    return (
      <div className="DashboardConfig">
        <form onSubmit={handleSubmit}>
          <h1>CICCIO</h1>
        </form>
      </div>
    );
  }
}

DashboardConfigFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired
};

const DashboardConfigForm = reduxForm({
  form: "dashboard-config-form"
})(DashboardConfigFormBody);

export default DashboardConfigForm;
