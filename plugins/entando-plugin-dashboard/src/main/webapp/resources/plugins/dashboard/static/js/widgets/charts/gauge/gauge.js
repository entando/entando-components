if (org === undefined) {
  var org = {};
}
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.GaugeChart = class {
  constructor(id, config) {
    console.log("Gauge Chart - config", config);
    const {axis, size, padding, legend, data, gauge} = config;

    this.configuration = {
      bindto: id,
      axis,
      legend,
      size,
      padding,
      data,
      gauge
    };
    console.log("configuration : ", this.configuration);
    this.chart = c3.generate(this.configuration);
  }

  update(json) {
    const {keys} = this.configuration.data;
    const values = keys.value.reduce((acc, key) => {
      acc[key] = json.reduce((sum, el) => (sum += el[key]), 0);
      return acc;
    }, {});

    const obj = {
      json: [values],
      keys,
      length: 0
    };
    console.log("obj", obj);
    this.chart.flow(obj);
  }
};
