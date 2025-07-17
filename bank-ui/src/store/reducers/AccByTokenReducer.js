const initialState = {
    account: []
}
const AccByTokenReducer = (state = initialState, action) => {
    if (action.type == 'FETCH_ACC_BY_TOKEN') {
        return {
            ...state,
            account: action.payload
        }
    } return state
}
export default AccByTokenReducer