// eslint-disable-next-line import/prefer-default-export
export const SERVER_CONFIG_LIST = [
  {
    id: 'kieserver1',
    active: true,
    name: 'ansia.serv.run',
    username: 'bpmUser1',
    password: 'bpmUser1psw',
    hostName: 'ansia.serv.run',
    schema: 'http',
    port: 8080,
    webappName: 'kie-server',
    timeout: 500,
    debug: false,
  },
  {
    id: 'kieserver2',
    active: false,
    name: 'another.serv.run',
    username: 'bpmUser2',
    password: 'bpmUser2psw',
    hostName: 'another.serv.run',
    schema: 'https',
    port: 8081,
    webappName: 'kie-server',
    timeout: 1000,
    debug: true,
  },
];
