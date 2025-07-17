import axios from "axios"

export const fetchUserInfo = (dispatch) => () => {
    axios.get("http://localhost:8080/api/user/get/all")
    .then(function (response) {
        // console.log(response)
        dispatch({
            'payload': response.data,
            'type': 'FETCH_USER_INFO'
        })
    })
}