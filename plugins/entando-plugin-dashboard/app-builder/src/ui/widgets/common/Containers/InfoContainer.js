import {connect} from "react-redux";
import Info from "ui/widgets/common/Components/Info";

import {fetchPageInformation} from "state/main/actions";

import {getInfoPage} from "state/main/selectors";

const mapsStateToProps = state => ({
  pageInformation: getInfoPage(state)
});

const mapDispatchToProps = dispatch => ({
  onWillMount: pageCode => dispatch(fetchPageInformation(pageCode))
});

const InfoContainer = connect(
  mapsStateToProps,
  mapDispatchToProps
)(Info);

export default InfoContainer;
