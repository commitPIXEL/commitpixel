import { configureStore } from "@reduxjs/toolkit";
import { persistReducer, persistStore } from "redux-persist";
import storage from "redux-persist/lib/storage";
import rootReducer from "./reducers/rootReducer";
import thunk from "redux-thunk";

const persistConfig = {
    key: "root",    // 스토리지에 저장할 때 키값을 지정
    storage,    // 저장할 스토리지 지정(local)
    whitelist: ["color", "authorization", "tool", "device"]
}

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
    reducer: persistedReducer,
    middleware: [thunk],
});

export const persistor = persistStore(store);   // 상태의 영속성을 관리하기 위해 export
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
export default store;