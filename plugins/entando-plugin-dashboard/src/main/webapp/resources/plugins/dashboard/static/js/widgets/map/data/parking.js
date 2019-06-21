const parking = {
  id: "parking",
  title: "Parking",
  icon: {
    marker: "fa fa-map-marker",
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
const loadData = url => {
  console.log("loadData");
  return new Promise(resolve => {
     // $.get(url, data => {
     //   parking.data = data;
     //   resolve(parking);
     // });
     parking.data = [
       {
         DeviceLocations: {
           latitude: 39.2157421,
           longitude: 9.1075453,
         },
         DeviceInformation: {
           liberi: 25,
           occupati: 50,
           totale: 75,
         },
       },
     ];
     resolve(parking);
  });
};
