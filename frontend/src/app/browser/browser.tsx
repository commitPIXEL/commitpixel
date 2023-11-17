"use client";

import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import CanvasContainer from "./canvas/canvasContainer";
import Nav from "./nav/nav";
import Sidebar from "./sidebar/sidebar";
import { setTool } from "@/store/slices/toolSlice";

const Browser = () => {
  const dispatch = useDispatch();
  const isUrlInput = useSelector(
    (state: RootState) => state.urlInput.isUrlInput
  );

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (isUrlInput) return;
      if (e.key === "b" || e.key === "B") {
        dispatch(setTool("painting"));
      } else if (e.key === "i" || e.key === "I") {
        dispatch(setTool("copying"));
      } else if (e.key === "Escape") {
        dispatch(setTool(null));
      }
    };

    document.addEventListener("keydown", handleKeyDown);
    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    }
  }, [isUrlInput]);

  return (
    <main className="w-screen h-screen bg-bgColor">
      <Nav />
      <div className="grid grid-cols-4 w-full h-[92%] place-items-stretch p-4">
        <CanvasContainer />
        <Sidebar />
      </div>
    </main>
  );
};

export default Browser;
