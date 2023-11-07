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
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );
  const isUrlInput = useSelector(
    (state: RootState) => state.urlInput.isUrlInput
  );
  
  const fetchAsync = async () => {
    try {
      // const [resUser, resFromPixel] = await Promise.all([
      //   customFetch("/user"),
      //   customFetch("/pixel"),
      // ]);

      // const userData: UserInfo = await resUser.json();
      // const pixelData: UserPixel = await resFromPixel.json();
      // dispatch(getUserInfo(userData));
      // dispatch(updateUserPixel(pixelData));

      const resUser = await customFetch("/user/");
      console.log("resUser: ");
      console.log(resUser);
      const userData: IUserInfo = await resUser.json();
      console.log("userData: ");
      console.log(userData);

      dispatch(getUserInfo(userData));
    } catch (err) {
      console.error("Error:", err);
    }

    try {
      const resPixel = await customFetch("/user/refreshinfo");
      console.log("resPixel: ");
      console.log(resPixel);
      const pixelData: IUserPixel = await resPixel.json();
      console.log("pixelData: ");
      console.log(pixelData);
      dispatch(updateUserPixel(pixelData));
    } catch (err) {
      console.error("Error:", err);
    }
  };
  useEffect(() => {
    if (accessToken.length) {
      fetchAsync();
    }
  }, [accessToken]);

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (isUrlInput) return;
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
