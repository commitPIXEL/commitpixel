import { createSlice, PayloadAction } from "@reduxjs/toolkit";

const initialState = {
  imageSrc: "",
  position: { x: -500, y: 0 },
};

const imgToPixelSlice = createSlice({
  name: "imgToPixel",
  initialState,
  reducers: {
    mobileInput: (state, action: PayloadAction<string>) => {
      state.imageSrc = action.payload;
      const mobilePosition = {
        x: window.innerWidth / 2 - 50,
        y: window.innerHeight / 2 - 50,
      };
      state.position = mobilePosition;
    },
    browserInput: (state, action: PayloadAction<string>) => {
      state.imageSrc = action.payload;
    },
    reset: (state) => {
      state.imageSrc = "";
      state.position = { x: -500, y: 0 };
    },
    move: (state, action: PayloadAction<{ x: number; y: number }>) => {
      state.position = action.payload;
    },
  },
});

export const { mobileInput, browserInput, reset, move } =
  imgToPixelSlice.actions;
export default imgToPixelSlice.reducer;
