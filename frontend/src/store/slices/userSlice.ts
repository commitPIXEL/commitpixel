import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  nickName: "",
  profileImage: "",
  totalPixel: 0,
  availablePixel: 0,
  url: ""
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers:{
    // payload type: string
    login: (state, action: PayloadAction<string>) => {
      state.nickName = action.payload;
    },
    logout: (state) => {
      state.nickName = "";
    }
  }
});

export const { login, logout } = userSlice.actions;
export default userSlice.reducer;

