if (org === undefined) {
  var org = {};
}
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.PieChart = class {
  constructor(id, config) {
    console.log("Pie Chart - config", config);
    const {axis, size, padding, legend, data, pie} = config;

    this.configuration = {
      bindto: id,
      axis,
      legend,
      size,
      padding,
      data,
      pie
    };
    console.log("configuration : ", this.configuration);
    this.chart = c3.generate(this.configuration);
    this.chart.resize();
  }

  update(json) {
    const {keys} = this.configuration.data;
    const obj = {
      json,
      keys,
      length: 0
    };
    console.log("obj", obj);
    if (obj.json.length > 0 ) {
      this.chart.flow(obj);
    }
  }
};
