export const setUserDetails = (dispatch) => (user) => {
    dispatch({
        'payload': user,
        'type': 'SET_USER_DETAILS'
    })
}