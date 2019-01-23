import {connect} from "react-redux";
import {formValueSelector} from "redux-form";
import ChartSecondStepContent from "ui/widgets/charts/common/components/ChartSecondStepContent";

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);

  return {
    formName: ownProps.formName,
    type: ownProps.type,
    data: ownProps.data,
    labelChartPreview: ownProps.labelChartPreview,
    axis: {
      rotated: selector(state, "axis.rotated")
    }
  };
};

const ChartSecondStepContentContainer = connect(
  mapStateToProps,
  null
)(ChartSecondStepContent);
export default ChartSecondStepContentContainer;
