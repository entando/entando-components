// eslint-disable-next-line import/prefer-default-export
export const CASE_DEFINITION_LIST = {
  definitions: [
    {
      'container-id': 'credit-dispute-case_1.0-SNAPSHOT',
      'adhoc-fragments': [
        {
          name: 'Start Process Fraud',
          type: 'ActionNode',
        },
        {
          name: 'Milestone 1: Automated Chargeback',
          type: 'MilestoneNode',
        },
        {
          name: 'Milestone 2: Manual Chargeback',
          type: 'MilestoneNode',
        },
        {
          name: 'Milestone 3: Credit Account',
          type: 'MilestoneNode',
        },
      ],
      roles: {
        'fraud-manager': 1,
      },
      name: 'FraudDispute',
      'case-id-prefix': 'FR',
      stages: [],
      id: 'CreditCardDisputeCase.FraudDispute',
      milestones: [
        {
          'milestone-id': '_5DF9B265-4140-42B1-B34B-2A85ED6DBFBF',
          'milestone-name': 'Milestone 1: Automated Chargeback',
          'milestone-mandatory': false,
        },
        {
          'milestone-id': '_BF6327BB-57ED-4316-BEBB-CC0B43F4DE13',
          'milestone-name': 'Milestone 2: Manual Chargeback',
          'milestone-mandatory': false,
        },
        {
          'milestone-id': '_AA3A4A87-9512-40CD-A573-D2913D67FBA4',
          'milestone-name': 'Milestone 3: Credit Account',
          'milestone-mandatory': false,
        },
      ],
      version: '1.0',
    },
  ],
};
