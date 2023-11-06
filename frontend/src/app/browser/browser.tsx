"use client";

import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo, getUserPixel } from "@/store/slices/userSlice";
import { RootState } from "@/store";
import CanvasContainer from "./canvas/canvasContainer";
import Nav from "./nav";
import Sidebar from "./sidebar/sidebar";
import { setTool } from "@/store/slices/toolSlice";
import TestCanvas from "./canvas/testCanvas";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import { IUserInfo, IUserFixel } from "../../interfaces/browser";

const Browser = () => {
  const dispatch = useDispatch();
  const customFetch = useFetchWithAuth();
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );

  useEffect(() => {
    const fetchAsync = async () => {
      try {
        const [resUser, resFixel] = await Promise.all([
          customFetch("/user/"),
          customFetch("/user/refreshinfo"),
        ]);
        console.log("resFixel: ");
        console.log(resFixel);

        const userData: IUserInfo = await resUser.json();
        const fixelData: IUserFixel = await resFixel.json();
        console.log("fixelData: ");
        console.log(fixelData);

        dispatch(getUserInfo(userData));
        dispatch(getUserPixel(fixelData));
      } catch (err) {
        console.error("Error:", err);
      }
    };

    if (accessToken.length) {
      fetchAsync();
    }
  }, [accessToken]);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    console.log(e.key);
    if (e.key === "b" || e.key === "B") {
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
