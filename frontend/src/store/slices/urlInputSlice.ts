import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isUrlInput: false
};

const urlInputSlice = createSlice({
  name: "input",
  initialState,
  reducers:{
    setUrlInputOn: (state) => {
      state.isUrlInput = true;
    },
    setUrlInputOff: (state) => {
      state.isUrlInput = false;
    }
  }
});

export const { setUrlInputOn, setUrlInputOff } = urlInputSlice.actions;
export default urlInputSlice.reducer;