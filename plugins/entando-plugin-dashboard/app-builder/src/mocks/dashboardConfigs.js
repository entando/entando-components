export const DEVICE1 = {
  status: "online",
  inUse: "On",
  batteryLevel: "90%",
  deviceCode: "xxx-33-444",
  deviceBrand: "logitech",
  expirationGuarantee: "31/12/2017",
  coordinates: ["39.2153109", "9.1076246"],
  information: "Parcheggi Piazza Matteoti"
};
export const DEVICE2 = {
  status: "offline",
  inUse: "Off",
  batteryLevel: "70%",
  deviceCode: "xxx-55-444",
  deviceBrand: "logitech",
  expirationGuarantee: "31/12/2021",
  coordinates: ["39.2168495", "9.1075549"],
  information: "Piazza del Carmine"
};
export const DEVICE3 = {
  status: "online",
  inUse: "On",
  batteryLevel: "off",
  deviceCode: "xxx-zz-444",
  deviceBrand: "asus",
  expirationGuarantee: "31/12/2018",
  coordinates: ["39.2136159", "9.115505"],
  information: "Via Roma"
};
export const DEVICE4 = {
  status: "offline",
  inUse: "Off",
  batteryLevel: "",
  deviceCode: "xxx-22-444",
  deviceBrand: "intel",
  expirationGuarantee: "",
  coordinates: ["39.2153109", "9.1076246"],
  information: "Parcheggi Piazza Matteoti"
};

export const DEVICE_TEMPERATURE = {
  temperature: 25,
  timestamp: "2019-02-12T11:03:22+00:00"
};

export const DASHBOARD_CONFIG_LIST = [
  {
    active: true,
    debug: false,
    id: "1",
    serverDescription: "KAA IoT Server Devices CRS4",
    username: "admin",
    password: "adminadmin",
    serverURI: "http://kaa.entando.iot.com:3303",
    token: "token-code",
    timeout: 300
  },
  {
    active: true,
    debug: false,
    id: "2",
    serverDescription: "KAA IoT Server PIA",
    username: "admin",
    password: "adminadmin",
    serverURI: "http://kaa.entando.iot.com:3303",
    token: "token-code",
    timeout: 300
  }
];

export const DATASOURCE_TEMPERATURE = {
  id: "temperature",
  datasource: "Temperature Sensor",
  datasourceURI: "/api/temperature",
  status: "ok"
};
export const DATASOURCE_PARKING = {
  id: "parking",
  datasource: "Parking Area",
  datasourceURI: "parking/devices/",
  status: "ok"
};
export const DATASOURCE_BIKE_SHARING = {
  id: "bike",
  datasource: "Bike Sharing",
  datasourceURI: "bike-sharing/devices/",
  status: "ok"
};
export const DATASOURCE_BUS_STATION = {
  id: "bus",
  datasource: "Bus station",
  datasourceURI: "bus-station/devices/",
  status: "ok"
};

export const DATASOURCE_PARKING_DATA = {
  id: "parking",
  columns: [
    {key: "status", value: "status", hidden: false},
    {key: "inUse", value: "inUse", hidden: false},
    {key: "batteryLevel", value: "batteryLevel", hidden: false},
    {key: "deviceCode", value: "deviceCode", hidden: false},
    {key: "deviceBrand", value: "deviceBrand", hidden: false},
    {key: "expirationGuarantee", value: "expirationG.", hidden: true},
    {key: "coordinates", value: "coordinates", hidden: false},
    {key: "information", value: "information", hidden: false}
  ],
  data: [DEVICE1, DEVICE2, DEVICE3, DEVICE4]
};
export const DATASOURCE_BIKE_DATA = {
  id: "bike",
  columns: [
    {key: "inUse", value: "inUse", hidden: false},
    {key: "coordinates", value: "coordinates", hidden: false},
    {key: "information", value: "information", hidden: false}
  ],
  data: [DEVICE1, DEVICE2, DEVICE3, DEVICE4]
};
export const DATASOURCE_BUS_DATA = {
  id: "bus",
  columns: [
    {key: "status", value: "status", hidden: false},
    {key: "coordinates", value: "coordinates", hidden: false},
    {key: "information", value: "information", hidden: false}
  ],
  data: [DEVICE1, DEVICE2, DEVICE3, DEVICE4]
};
export const DATASOURCE_TEMPERATURE_DATA = {
  id: "temperature",
  columns: [
    {key: "temperature", value: "temperature", hidden: false},
    {key: "timestamp", value: "timestamp", hidden: false}
  ],
  data: []
};

export const DATASOURCES_DATA = {
  "1": {
    parking: DATASOURCE_PARKING_DATA,
    bike: DATASOURCE_BIKE_DATA,
    bus: DATASOURCE_BUS_DATA
  },
  "2": {
    temperature: DATASOURCE_TEMPERATURE_DATA
  }
};

export const DASHBOARD_LIST_DATASOURCE = {
  "1": [DATASOURCE_PARKING, DATASOURCE_BIKE_SHARING, DATASOURCE_BUS_STATION],
  "2": [DATASOURCE_TEMPERATURE]
};
