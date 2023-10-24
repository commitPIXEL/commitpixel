import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  tool: null
};

const toolSlice = createSlice({
  name: "tool",
  initialState,
  reducers:{
    setTool: (state, action) => {
      state.tool = action.payload;
    }
  }
});

export const { setTool } = toolSlice.actions;
export default toolSlice.reducer;
