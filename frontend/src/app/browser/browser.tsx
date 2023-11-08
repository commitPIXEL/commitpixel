"use client";

import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getUserInfo, updateUserPixel } from "@/store/slices/userSlice";
import { RootState } from "@/store";
import CanvasContainer from "./canvas/canvasContainer";
import Nav from "./nav";
import Sidebar from "./sidebar/sidebar";
import { setTool } from "@/store/slices/toolSlice";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import { IUserInfo, IUserPixel } from "../../interfaces/browser";

const Browser = () => {
  const dispatch = useDispatch();
  const customFetch = useFetchWithAuth();
  const user = useSelector((state: RootState) => state.user);
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );
  const isUrlInput = useSelector(
    (state: RootState) => state.urlInput.isUrlInput
  );
  
  const fetchAsync = async () => {
    try {
      const resUser = await customFetch("/user/");
      const userData: IUserInfo = await resUser.json();

      dispatch(getUserInfo(userData));
    } catch (err) {
      console.error("Error:", err);
    }

    try {
      const resPixel = await customFetch("/user/refreshinfo");
      const pixelData: IUserPixel = await resPixel.json();

      if(pixelData.githubNickname === null) {
        window.alert("마지막 갱신 이후 15분이 지나지 않았습니다!")
      }
      dispatch(updateUserPixel(pixelData));
    } catch (err) {
      console.error("Error:", err);
    }
  };
  useEffect(() => {
    if (accessToken.length > 100) {
      fetchAsync();
    }
  }, [accessToken]);

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
        {/* TODO: 개발 버전과 Test버전 캔버스 잘 구별하기 */}
        <CanvasContainer />
        <Sidebar />
      </div>
    </main>
  );
};

export default Browser;
