import {getChart} from "ui/widgets/charts/helper/getChart";
const PreviewChart = ({type, data, props}) => {
  console.log("PreviewChart - props", props);
  return getChart("LINE_CHART", data, props);
};

export default PreviewChart;
