"use client";

import { useSelector } from "react-redux";
import CanvasContainer from "../browser/canvas/canvasContainer";
import Menu from "../browser/sidebar/picker/menu/pickerMenu";
import MobileNav from "./mobileNav";
import MobilePicker from "./mobilePicker";
import { RootState } from "@/store";
import LoginBtn from "@/components/loginBtn";
import PopupPWA from "@/components/popupPWA";
import ControlSpeedDial from "./controlSpeedDial";
import { Switch } from "@mui/material";
import { useState } from "react";
import ToPixelImg from "@/components/toPixelImg";

const Mobile = () => {
  const user = useSelector((state: RootState) => state.user);
  const [hidden, setHidden] = useState(true);

  const handleHiddenChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setHidden(event.target.checked);
  };

  return (
    <>
      <div className="w-screen h-screen items-center flex flex-col bg-bgColor text-white">
        <PopupPWA />
        <MobileNav />
        <CanvasContainer />
        <div className="relative z-10">
          <ToPixelImg />
        </div>
        <div className="flex justify-between items-center">
          {user?.githubNickname !== "" ? <Menu /> : null}
          {user?.githubNickname !== "" ? (
            <Switch
              checked={hidden}
              onChange={handleHiddenChange}
              color="default"
              sx={{
                "& .MuiSwitch-track": {
                  backgroundColor: hidden ? "darkgrey" : undefined,
                },
              }}
            />
          ) : null}
        </div>
        {user?.githubNickname !== "" ? <MobilePicker /> : null}
        {user?.githubNickname === "" ? (
          <div className="mt-10"> 로그인 시 캔버스 색칠 해금!</div>
        ) : null}
        {user?.githubNickname === "" ? <LoginBtn /> : null}
      </div>
      {user?.githubNickname !== "" && <ControlSpeedDial hidden={hidden} />}
    </>
  );
};

export default Mobile;
