if (org === undefined) {
  var org = {};
}
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.LineChart = class {
  constructor(id, config) {
    console.log("Line Chart - config", config);
    const {
      axis,
      size,
      padding: {left, right, bottom, top},
      legend,
      data
    } = config;

    this.configuration = {
      bindto: id,
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
