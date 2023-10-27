import { combineReducers } from "redux";
import colorReducer from "../slices/colorSlice";
import authorizationReducer from "../slices/authorizationSlice"
import toolReducer from "../slices/toolSlice";
import deviceReducer from "../slices/deviceSlice";

const rootReducer = combineReducers({
    color: colorReducer,
    authorization: authorizationReducer,
    tool: toolReducer,
    device: deviceReducer,
});

export default rootReducer;