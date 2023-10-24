import { combineReducers } from "redux";
import colorReducer from "../slices/colorSlice";
import authorizationReducer from "../slices/authorizationSlice"
import toolReducer from "../slices/toolSlice";

const rootReducer = combineReducers({
    color: colorReducer,
    authorization: authorizationReducer,
    tool: toolReducer,
});

export default rootReducer;