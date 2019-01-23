import {connect} from "react-redux";
import {formValueSelector} from "redux-form";
import ChartThirdStepContent from "ui/widgets/charts/common/components/ChartThirdStepContent";

const mapStateToProps = (state, ownProps) => {
  const selector = formValueSelector(ownProps.formName);
  return {
    type: ownProps.type,
    data: ownProps.data,
    labelChartPreview: ownProps.labelChartPreview,
    axis: {rotated: selector(state, "axis.rotated")}
  };
};

const ChartThirdStepContentContainer = connect(
  mapStateToProps,
  null
)(ChartThirdStepContent);
export default ChartThirdStepContentContainer;
