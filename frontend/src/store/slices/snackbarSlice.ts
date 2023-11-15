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
    },
    setSnackbarOff: (state) => {
      state.isOpen = false;
    },
  },
});

export const { setSnackbarOpen, setSnackbarOff } = snackbarSlice.actions;
export default snackbarSlice.reducer;