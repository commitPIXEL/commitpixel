"use client";

import { useSelector } from "react-redux";
import CanvasContainer from "../browser/canvas/canvasContainer";
import Menu from "../browser/sidebar/picker/menu/pickerMenu";
import MobileNav from "./mobileNav";
import MobilePicker from "./mobilePicker";
import { RootState } from "@/store";
import LoginBtn from "@/components/loginBtn";
import PopupPWA from "@/components/popupPWA";

const Mobile = () => {
  const user = useSelector((state: RootState) => state.user);

  return(
  <div className="w-screen h-screen items-center flex flex-col bg-bgColor text-white">
    <PopupPWA />
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