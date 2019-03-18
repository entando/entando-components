if (org === undefined) {
  var org = {};
}
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.BarChart = class {
  constructor(id, config) {
    console.log("Bar Chart - config", config);
    const {axis, size, padding, legend, data, bar} = config;

    this.configuration = {
      bindto: id,
      bar,
      axis,
      legend,
      size,
      padding,
      data
    };

    console.log("configuration : ", this.configuration);
    this.chart = c3.generate(this.configuration);
  }

  update(json) {
    const {keys} = this.configuration.data;
    const obj = {
      json,
      keys,
      length: 0
    };
    console.log("obj", obj);
    this.chart.flow(obj);
  }
};
