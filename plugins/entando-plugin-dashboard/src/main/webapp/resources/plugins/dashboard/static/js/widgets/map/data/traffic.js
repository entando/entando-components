const parking = {
  id: "traffic",
  title: "Traffic",
  icon: {
    id: "fa fa-map-marker",
    rules: {
      key: "level",
      conditions: [
        {
          check: "val > 0.5",
          result: 2
        },
        {
          check: "val > 0.5 && val < 0.7 ",
          result: 4
        },
        {
          check: "val < 0.5  && val > 0.3 ",
          result: 1
        }
      ]
    }
  }
};

const CONFIG_MAP = {
  datasources: [parking]
};
const loadTrafficData = url => {
  console.log("loadTrafficData");
  return new Promise(resolve => {
    $.get(url, data => {
      parking.data = data;
      resolve(parking);
    });
  });
};
