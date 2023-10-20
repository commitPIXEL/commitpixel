import { combineReducers } from "redux";
import colorReducer from "../slices/colorSlice";
import authorizationReducer from "../slices/authorizationSlice"

const rootReducer = combineReducers({
    color: colorReducer,
    authorization: authorizationReducer,
});

export default rootReducer;