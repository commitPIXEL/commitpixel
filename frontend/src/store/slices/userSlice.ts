import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  githubNickname: "",
  profileImage: "",
  totalPixel: 0,
  availablePixel: 0,
  url: "",
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    getUserInfo: (
      state,
      action: PayloadAction<{
        githubNickname: string;
        profileImage: string;
        url: string;
      }>
    ) => {
      state.githubNickname = action.payload.githubNickname;
      state.profileImage = action.payload.profileImage;
      state.url = action.payload.url;
    },
    getUserPixel: (
      state,
      action: PayloadAction<{ totalPixel: number; availablePixel: number }>
    ) => {
      state.totalPixel = action.payload.totalPixel;
      state.availablePixel = action.payload.availablePixel;
    },
    pickPixel: (state, action: PayloadAction<{ availablePixel: number }>) => {
      state.availablePixel = action.payload.availablePixel;
    },
    addPixel: (state, action: PayloadAction<{ totalPixel: number; availablePixel: number }>) => {
      state.totalPixel = action.payload.totalPixel;
      state.availablePixel = action.payload.availablePixel;
    },
    logout: (state) => {
      state.githubNickname = "";
      state.profileImage = "";
      state.totalPixel = 0;
      state.availablePixel = 0;
      state.url = "";
    },
  },
});

export const { getUserInfo, getUserPixel, pickPixel, addPixel, logout } =
  userSlice.actions;
export default userSlice.reducer;
