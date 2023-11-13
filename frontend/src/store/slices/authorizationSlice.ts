import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  authorization: "",
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
      state.authorization = "";
    }
  }
});

export const { login, logout } = authorizationSlice.actions;
export default authorizationSlice.reducer;

