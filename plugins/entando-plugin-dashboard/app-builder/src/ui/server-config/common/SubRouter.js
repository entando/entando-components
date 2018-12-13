import React from 'react';
import PropTypes from 'prop-types';

import ConfigPageContainer from 'ui/server-config/list/ConfigPageContainer';
import ServerConfigAddPage from 'ui/server-config/add/ServerConfigAddPage';
import ServerConfigEditPage from 'ui/server-config/add/ServerConfigEditPage';

const SubRouter = ({ pluginPage }) => {
  switch (pluginPage) {
    case 'add': return <ServerConfigAddPage />;
    case 'edit': return <ServerConfigEditPage />;
    default: return <ConfigPageContainer />;
  }
};

SubRouter.propTypes = {
  pluginPage: PropTypes.string.isRequired,
};

export default SubRouter;
