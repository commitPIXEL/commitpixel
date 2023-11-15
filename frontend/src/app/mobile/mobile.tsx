"use client";

import { useDispatch, useSelector } from "react-redux";
import CanvasContainer from "../browser/canvas/canvasContainer";
import Menu from "../browser/sidebar/picker/menu/pickerMenu";
import MobileNav from "./mobileNav";
import MobilePicker from "./mobilePicker";
import { RootState } from "@/store";
import LoginBtn from "@/components/loginBtn";
import { useEffect } from "react";
import useFetchUser from "@/hooks/useFetchUser";

const Mobile = () => {
  const setUser = useFetchUser();
  const user = useSelector((state: RootState) => state.user);
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );

  useEffect(() => {
    if (accessToken && !user.githubNickname) {
      setUser;
      console.log("어세스토큰이 있지만 닉네임이 없음 from Mobile.tsx");
    }
  }, [accessToken]);


  return(
  <div className="w-screen h-screen items-center flex flex-col bg-bgColor text-white">
    <MobileNav />
    <CanvasContainer />
    {user?.githubNickname !== "" ? <Menu /> : null}
    {user?.githubNickname !== "" ? <MobilePicker /> : null}
    {user?.githubNickname === "" ? <div className="mt-10"> 로그인 시 캔버스 색칠 해금!</div> : null}
    {user?.githubNickname === "" ? <LoginBtn /> : null}
  </div>
  )
}

export default Mobile;