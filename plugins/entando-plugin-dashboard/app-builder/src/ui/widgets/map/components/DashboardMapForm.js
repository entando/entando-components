import React, {Component} from "react";
import PropTypes from "prop-types";
import {reduxForm} from "redux-form";
import MapFirstStepContent from "ui/widgets/map/components/MapFirstStepContent";
import Stepper from "ui/widgets/common/components/Stepper";

const FORM_NAME = "form-dashboard-map";

class DashboardMapFormBody extends Component {
  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount();
    }
  }

  render() {
    const {formSyncErrors} = this.props;
    const validateSteps = [false, false, false];
    if (
      !formSyncErrors.title &&
      !formSyncErrors.serverName &&
      !formSyncErrors.datasource
    ) {
      validateSteps[0] = true;
    }

    return (
      <Stepper
        handleSubmit={this.props.handleSubmit}
        validateSteps={validateSteps}
        step1={<MapFirstStepContent />}
      />
    );
  }
}
DashboardMapFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  formSyncErrors: PropTypes.shape({})
};

DashboardMapFormBody.defaultProps = {
  formSyncErrors: {}
};

const DashboardMapForm = reduxForm({
  form: FORM_NAME
})(DashboardMapFormBody);

export default DashboardMapForm;
