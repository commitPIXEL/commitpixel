import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  color: {
    hex: "#000000",
    rgb: {
      r: 0,
      g: 0,
      b: 0,
      a: 1,
    }
  }
};

const colorSlice = createSlice({
  name: "color",
  initialState,
  reducers:{
    pick: (state, action) => {
      state.color = action.payload;
    }
  }
});

export const { pick } = colorSlice.actions;
export default colorSlice.reducer;

