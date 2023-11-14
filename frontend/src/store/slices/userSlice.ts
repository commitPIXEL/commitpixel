import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  githubNickname: "",
  profileImage: "",
  totalCredit: 0,
  availablePixel: 0,
  url: "",
  isSolvedACAuth: false,
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    getUserNickname: (
      state,
      action: PayloadAction<string>
    ) => {
      state.githubNickname = action.payload;
    },
    getUserInfo: (
      state,
      action: PayloadAction<{
        githubNickname: string;
        profileImage: string;
        url: string;
        isSolvedACAuth: boolean;
      }>
    ) => {
      state.githubNickname = action.payload.githubNickname;
      state.profileImage = action.payload.profileImage;
      state.url = action.payload.url;
      state.isSolvedACAuth = action.payload.isSolvedACAuth;
    },
    updateUserPixel: (
      state,
      action: PayloadAction<{
        githubNickname: string | null;
        totalCredit: number;
        availablePixel: number;
      }>
    ) => {
      state.totalCredit = action.payload.totalCredit;
      state.availablePixel = action.payload.availablePixel;
    },
    connectSolvedAC: (state, action: PayloadAction<{totalCredit: number; availablePixel: number;}>) => {
      state.totalCredit = action.payload.totalCredit;
      state.availablePixel = action.payload.availablePixel;
      state.isSolvedACAuth = true;
    },
    resetUser: (state) => {
      state.githubNickname = "";
      state.profileImage = "";
      state.totalCredit = 0;
      state.availablePixel = 0;
      state.url = "";
      state.isSolvedACAuth = false;
    },
    updateUrl: (state, action: PayloadAction<{url: string}>) => {
      state.url = action.payload.url;
    },
    setAvailablePixel: (state, action) => {
      state.availablePixel = action.payload;
    },
  },
});

export const { getUserNickname, getUserInfo, updateUserPixel, resetUser, connectSolvedAC, setAvailablePixel, updateUrl } =
  userSlice.actions;
export default userSlice.reducer;
