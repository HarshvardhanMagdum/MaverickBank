const initialState = {
    userInfo: []
}
const UserInfoReducer = (state=initialState, action) => {
    if(action.type == 'FETCH_USER_INFO') {
        // console.log(action.payload)
        return {
            ...state,
            userInfo: action.payload
        }
    }
    return state
}
export default UserInfoReducer