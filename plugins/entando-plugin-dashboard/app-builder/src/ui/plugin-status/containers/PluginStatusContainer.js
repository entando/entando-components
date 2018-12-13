
import { connect } from 'react-redux';

import PluginStatus from '../components/PluginStatus';


// map the props
export const mapStateToProps = () => ({
  reduxOk: true,
});


// connect the component
const PluginStatusContainer = connect(mapStateToProps, null)(PluginStatus);

// export connected component (Container)
export default PluginStatusContainer;
