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

export const DASHBOARD_CONFIG_LIST = [
  {
    active: false,
    debug: false,
    id: "6ce983ca69c5448599328173084710ef20180504T094803416",
    name: "Entando IoT Server Devices",
    username: "admin",
    password: "passwordAdmin",
    hostName: "http://entando.iot.com:3303/iot",
    token: "token-code",
    timeout: 300
  }
];

export const DATASOURCE_PARKING = {
  id: "parking",
  datasource: "IOT Parking Area",
  datasourceUri: "parking/devices/",
  status: "ok"
};

export const DATASOURCE_PARKING_DATA = {
  id: "parking",
  columns: [
    "status",
    "inUse",
    "batteryLevel",
    "deviceCode",
    "deviceBrand",
    "expirationGuarantee",
    "coordinates",
    "information"
  ],
  data: [DEVICE1, DEVICE2, DEVICE3, DEVICE4]
};

export const DASHBOARD_LIST_DATASOURCE = {
  "6ce983ca69c5448599328173084710ef20180504T094803416": [DATASOURCE_PARKING]
};
