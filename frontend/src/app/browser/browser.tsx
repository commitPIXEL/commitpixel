"use client";

import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import CanvasContainer from "./canvas/canvasContainer";
import Nav from "./nav/nav";
import Sidebar from "./sidebar/sidebar";
import { setTool } from "@/store/slices/toolSlice";
import useFetchUser from "@/hooks/useFetchUser";

const Browser = () => {
  const dispatch = useDispatch();
  const setUser = useFetchUser();
  const user = useSelector((state: RootState) => state.user);
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );
  const isUrlInput = useSelector(
    (state: RootState) => state.urlInput.isUrlInput
  );
  
  useEffect(() => {
    console.log("어세스토큰 변화");
    const fetchUser = async () => {
      if (accessToken && !user.githubNickname) {
        console.log("어세스토큰이 있지만 닉네임이 없음 from Browser.tsx");
        try {
          await useFetchUser();
        } catch (error) {
          console.error(error);
        }
      };
    };
    fetchUser();
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
