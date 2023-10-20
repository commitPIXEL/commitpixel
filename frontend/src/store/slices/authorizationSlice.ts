import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialAuthorization = "";

const initialState = {
  authorization: initialAuthorization,
};

const authorizationSlice = createSlice({
  name: "authorization",
  initialState,
  reducers:{
    // payload type: string
    login: (state, action: PayloadAction<string>) => {
      state.authorization = action.payload;
    },
    logout: (state) => {
      state.authorization = initialAuthorization;
    }
  }
});

export const { login, logout } = authorizationSlice.actions;
export default authorizationSlice.reducer;

