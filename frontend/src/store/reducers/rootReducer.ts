import { combineReducers } from "redux";
import colorReducer from "../slices/colorSlice";

const rootReducer = combineReducers({
    color: colorReducer,
});

export default rootReducer;