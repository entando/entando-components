import React from "react";

import PluginStatusContainer from "ui/plugin-status/containers/PluginStatusContainer";

const Plugin = () => (
  <div className="EntandoPlugin EntandoPluginTemplate">
    <PluginStatusContainer publicAssetFileName="green-check.png" />
  </div>
);

export default Plugin;
