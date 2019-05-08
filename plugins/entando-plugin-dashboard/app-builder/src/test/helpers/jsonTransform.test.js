import jsonTransform from 'helpers/jsonTransform';

const OBJECT = {
  allColumns: 'true',
  options: {
    downlodable: true,
    filtrable: true,
  },
  serverName: '14',
  datasource: 'Temperature',
  columns: {
    temperature: 'temperature',
    timestamp: 'timestamp',
  },
  title: {
    en: 'titolo',
  },
};

describe('jsonTransform', () => {
  it('verify correct executtion', () => {
    const transform = jsonTransform(OBJECT);
    expect(transform).toMatchObject({
      allColumns: 'true',
      'columns.temperature': 'temperature',
      'columns.timestamp': 'timestamp',
      datasource: 'Temperature',
      'options.downlodable': true,
      'options.filtrable': true,
      serverName: '14',
      'title.en': 'titolo',
    });
  });
});
