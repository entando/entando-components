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
      columns: {x, y, y2}
    } = config;

    this.configuration = {
      bindto: id,
      axis,
      legend,
      size,
      padding: {
        left: parseInt(left, 10),
        top: parseInt(top, 10),
        right: parseInt(right, 10),
        bottom: parseInt(bottom, 10)
      }
    };
    const data = {};
    if (axis.x.type === "timeseries") {
      data.json = [];
      data.keys = {
        x: x[0].key,
        value: y.map(m => m.key)
      };
    }
    this.configuration.data = data;
    console.log("configuration", this.configuration);
    this.chart = c3.generate(this.configuration);
    this.chart.hide();
  }
  show() {
    console.log("chart", this.chart);
    this.chart.show();
  }
  hide() {
    this.chart.hide();
  }
  update(json) {
    const {keys} = this.configuration.data;
    this.chart.flow({
      json,
      keys,
      length: 0
    });
  }
};
