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

export const SERVER_CONFIG_LIST = [
  {
    active: false,
    id: "6ce983ca69c5448599328173084710ef20180504T094803416",
    name: "Entando IoT Server Devices",
    username: "admin",
    password: "passwordAdmin",
    hostName: "entando.iot.com",
    token: "token-code",
    port: 80,
    webappName: "iotDevices",
    timeout: null
  }
];

export const CONTEXT_PARKING = {
  id: "parking",
  description: "IOT Parking Area",
  devicesNumber: 4,
  devices: [DEVICE1, DEVICE2, DEVICE3, DEVICE4]
};

export const SERVER_CONFIG_LIST_CONTEXT = {
  "6ce983ca69c5448599328173084710ef20180504T094803416": {
    context: [CONTEXT_PARKING]
  }
};
