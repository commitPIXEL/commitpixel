import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isOpen: false,
};

const snackbarSlice = createSlice({
  name: "snackbar",
  initialState,
  reducers: {
    setSnackbarOpen: (state) => {
      state.isOpen = true;
      // console.log("isOpen: " + state.isOpen);
    },
    setSnackbarOff: (state) => {
      state.isOpen = false;
      // console.log("isOpen: " + state.isOpen);
    },
  },
});

export const { setSnackbarOpen, setSnackbarOff } = snackbarSlice.actions;
export default snackbarSlice.reducer;