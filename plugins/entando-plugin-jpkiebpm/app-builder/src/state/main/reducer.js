const initialState = {
  reduxStatus: 'works',
};

const reducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case 'sample-action':
      return { reduxStatus: 'sample-action triggered' };
    default:
      return state;
  }
};

export default reducer;
