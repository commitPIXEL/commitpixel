import { combineReducers } from "redux";
import colorReducer from "../slices/colorSlice";
import authorizationReducer from "../slices/authorizationSlice";
import toolReducer from "../slices/toolSlice";
import deviceReducer from "../slices/deviceSlice";
import userReducer from "../slices/userSlice";
import urlInputReducer from "../slices/urlInputSlice";

const rootReducer = combineReducers({
  color: colorReducer,
  authorization: authorizationReducer,
  tool: toolReducer,
  device: deviceReducer,
  user: userReducer,
  urlInput: urlInputReducer,
});

export default rootReducer;
