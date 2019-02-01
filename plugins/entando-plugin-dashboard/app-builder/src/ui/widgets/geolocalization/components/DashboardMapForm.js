import React, {Component} from "react";
import PropTypes from "prop-types";
import {reduxForm} from "redux-form";
import Stepper from "ui/widgets/common/components/Stepper";
import MapFirstStepContent from "ui/widgets/geolocalization/components/MapFirstStepContent";
import MapSecondStepContentContainer from "ui/widgets/geolocalization/containers/MapSecondStepContentContainer";

const FORM_NAME = "form-dashboard-map";

class DashboardMapFormBody extends Component {
  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount();
    }
  }

  render() {
    const {formSyncErrors, datasources} = this.props;
    const validateSteps = [false, false, false];
    if (!formSyncErrors.title && !formSyncErrors.serverName) {
      validateSteps[0] = true;
    }
    if (datasources.length > 0) {
      validateSteps[1] = true;
    }

    return (
      <Stepper
        handleSubmit={this.props.handleSubmit}
        validateSteps={validateSteps}
        step1={<MapFirstStepContent />}
        step2={<MapSecondStepContentContainer formName={FORM_NAME} />}
      />
    );
  }
}
DashboardMapFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  formSyncErrors: PropTypes.shape({}),
  datasources: PropTypes.arrayOf(PropTypes.shape({}))
};

DashboardMapFormBody.defaultProps = {
  formSyncErrors: {},
  datasources: []
};

const DashboardMapForm = reduxForm({
  form: FORM_NAME
})(DashboardMapFormBody);

export default DashboardMapForm;
