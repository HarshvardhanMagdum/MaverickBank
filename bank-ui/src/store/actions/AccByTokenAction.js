import axios from "axios"

export const fetchAccByToken = (dispatch) => (token) => {
    axios.get("http://localhost:8080/api/account/get-one", {
        headers: { Authorization: "Bearer " + token }
    }).then(function (response) {
        dispatch({
            'payload': response.data,
            'type': "FETCH_ACC_BY_TOKEN"
        })
    })
}