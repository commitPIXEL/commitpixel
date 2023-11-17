import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  device: "",
};

const deviceSlice = createSlice({
  name: "device",
  initialState,
  reducers:{
    setDevice: (state, action) => {
      state.device = action.payload;
    }
  }
});

export const { setDevice } = deviceSlice.actions;
export default deviceSlice.reducer;