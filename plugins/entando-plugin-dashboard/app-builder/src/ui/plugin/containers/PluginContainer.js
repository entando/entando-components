
import { connect } from 'react-redux';

// import selectors
import { getReduxStatus } from 'state/main/selectors';

// import the Component to be connected
import Plugin from '../components/Plugin';


// map the props
export const mapStateToProps = state => ({
  message: getReduxStatus(state),
});


// connect the component
const PluginContainer = connect(mapStateToProps, null)(Plugin);

// export connected component (Container)
export default PluginContainer;
