"use client";

import { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo, getUserPixel } from "@/store/slices/userSlice";
import { RootState } from "@/store";
import CanvasContainer from "./canvas/canvasContainer";
import Nav from "./nav";
import Sidebar from "./sidebar/sidebar";
import { setTool } from "@/store/slices/toolSlice";
import TestCanvas from "./canvas/testCanvas";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import { UserInfo, UserFixel } from "../../interfaces/browser";

const Browser = () => {
  const dispatch = useDispatch();
  const customFetch = useFetchWithAuth();
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );
  const user = useSelector((state: RootState) => state.user);

  // useEffect(() => {
  //   const fetchAsync = async () => {
  //     try {
  //       const [resFromUser, resFromFixel] = await Promise.all([
  //         customFetch("/user"),
  //         customFetch("/fixel"),
  //       ]);

  //       const userData: UserInfo = await resFromUser.json();
  //       const fixelData: UserFixel = await resFromFixel.json();
  //       dispatch(getUserInfo(userData));
  //       dispatch(getUserPixel(fixelData));
  //     } catch (err) {
  //       console.error("Error:", err);
  //     }
  //   };

  //   if (accessToken.length) {
  //     fetchAsync();
  //   }
  // }, [accessToken]);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    console.log(e.key);
    if (e.key === "h" || e.key === "H") {
      dispatch(setTool("panning"));
    } else if (e.key === "b" || e.key === "B") {
      dispatch(setTool("painting"));
    } else if (e.key === "i" || e.key === "I") {
      dispatch(setTool("copying"));
    } else if (e.key === "Escape") {
      dispatch(setTool(null));
    }
  };
  return (
    <main onKeyDown={handleKeyDown} className="w-screen h-screen bg-bgColor">
      <Nav />
      <div className="grid grid-cols-4 w-full h-[92%] place-items-stretch p-4">
        {/* TODO: 개발 버전과 Test버전 캔버스 잘 구별하기 */}
        <CanvasContainer />
        {/* <TestCanvas /> */}
        <Sidebar />
      </div>
    </main>
  );
};

export default Browser;
