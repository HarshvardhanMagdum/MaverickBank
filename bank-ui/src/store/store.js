import { configureStore } from "@reduxjs/toolkit";
import UserReducer from "./reducers/UserReducer";
import UserInfoReducer from "./reducers/UserInfoReducer";
import AccByTokenReducer from "./reducers/AccByTokenReducer";

const store = configureStore({
    reducer: {
        user: UserReducer,
        userInfo : UserInfoReducer,
        account: AccByTokenReducer,
    }
})

export default store