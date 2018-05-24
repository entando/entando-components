// eslint-disable-next-line import/prefer-default-export
export const OVERRIDES = [
  {
    _sourceId: null,
    field: 'applicant_income',
    id: 3,
    overrides: {
      list: [
        {
          type: 'defaultValueOverride',
          defaultValue: 'default value!',
        },
      ],
    },
    containerId: 'itorders_1.0.0-SNAPSHOT',
    date: 1527095587691,
    processId: 'com.redhat.bpms.examples.mortgage.MortgageApplication',
  },
  {
    _sourceId: null,
    field: 'property_price',
    id: 4,
    overrides: {
      list: [
        {
          type: 'placeHolderOverride',
          placeHolder: 'change me asap!',
        },
      ],
    },
    containerId: 'itorders_1.0.0-SNAPSHOT',
    date: null,
    processId: 'com.redhat.bpms.examples.mortgage.MortgageApplication',
  },
];
