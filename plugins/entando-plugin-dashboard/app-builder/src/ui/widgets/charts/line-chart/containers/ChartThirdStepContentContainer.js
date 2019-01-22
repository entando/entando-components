import {connect} from "react-redux";
import {formValueSelector} from "redux-form";
import ChartThirdStepContent from "ui/widgets/charts/common/components/ChartThirdStepContent";

const selector = formValueSelector("form-dashboard-line-chart");

const mapStateToProps = (state, ownProps) => ({
  type: ownProps.type,
  data: ownProps.data,
  labelChartPreview: ownProps.labelChartPreview,
  axis: {rotated: selector(state, "axis.rotated")}
});

const ChartThirdStepContentContainer = connect(
  mapStateToProps,
  null
)(ChartThirdStepContent);
export default ChartThirdStepContentContainer;
