import { createSlice } from "@reduxjs/toolkit";

const initialColor = "#000000";

const initialState = {
  color: initialColor,
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

