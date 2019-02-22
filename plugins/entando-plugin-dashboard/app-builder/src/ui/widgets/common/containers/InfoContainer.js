import {connect} from "react-redux";
import Info from "ui/widgets/common/components/Info";

import {getInfoPage} from "state/app-builder/selectors";

const mapsStateToProps = state => ({
  pageInformation: getInfoPage(state)
});

const InfoContainer = connect(
  mapsStateToProps,
  null
)(Info);

export default InfoContainer;
