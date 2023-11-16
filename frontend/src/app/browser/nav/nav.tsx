"use client";

import Image from "next/image";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import {resetUser} from "@/store/slices/userSlice"
import { logout } from "@/store/slices/authorizationSlice";
import Button from '@mui/material/Button';
import { useRouter } from "next/navigation";
import { FlourishBtn, InstallPWA, TutorialsBtn } from "./navBtn";

const Nav = () => {
  const dispatch = useDispatch();
  const router = useRouter();
  const user = useSelector((state: RootState) => state.user);

  const clickLogout = () => {
    dispatch(logout());
    dispatch(resetUser());
  };

  const handleLogoClick = () => {
    router.push("/");
  };

  return user.githubNickname ? (
    <div className="bg-mainColor w-full h-[8%] flex justify-between items-center pl-2 pr-10">
      <div className="flex w-[50%] h-full items-center">
        <div
          className="relative w-[10%] h-full cursor-pointer"
          onClick={handleLogoClick}
        >
          <Image
            src="/icon.png"
            fill
            style={{
              objectFit: "contain",
            }}
            sizes="80px"
            priority
            alt="commit Pixel logo"
          />
        </div>
        <div
          className="flex flex-col justify-center cursor-pointer"
          onClick={handleLogoClick}
        >
          <div className="text-xl font-bold">commit</div>
          <div className="text-xl font-bold">Pixel</div>
        </div>
      </div>
      <div className="w-[20%] flex items-center justify-end">
        <TutorialsBtn />
        <FlourishBtn />
        <div>
          <Button
            variant="text"
            color="inherit"
            onClick={clickLogout}
            className="text-xl font-bold ml-16"
          >
            LOGOUT
          </Button>
        </div>
      </div>
    </div>
  ) : (
    <div className="bg-mainColor w-full h-[8%] flex justify-between items-center pl-2 pr-10">
      <div className="flex w-[20%] h-full justify-start items-center">
        <div
          className="relative w-[20%] h-full cursor-pointer"
          onClick={handleLogoClick}
        >
          <Image
            src="/icon.png"
            fill
            style={{
              objectFit: "contain",
            }}
            sizes="80px"
            priority
            alt="commit Pixel logo"
          />
        </div>
        <div
          className="flex flex-col justify-center cursor-pointer"
          onClick={handleLogoClick}
        >
          <div className="text-xl font-bold">commit</div>
          <div className="text-xl font-bold">Pixel</div>
        </div>
      </div>
      <div className="flex items-center justify-end w-[10%]">
        <TutorialsBtn />
        <FlourishBtn />
        <InstallPWA />
      </div>
    </div>
  );
}

export default Nav;
