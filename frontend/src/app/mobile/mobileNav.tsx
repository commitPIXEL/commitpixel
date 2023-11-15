"use client";

import { RootState } from "@/store";
import { logout } from "@/store/slices/authorizationSlice";
import { resetUser } from "@/store/slices/userSlice";
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Image from "next/image";
import { useDispatch, useSelector } from "react-redux";

const MobileNav = () => {
  const dispatch = useDispatch();
  const user = useSelector((state: RootState) => state.user);

  const clickLogout = () => {
    dispatch(logout());
    dispatch(resetUser());
  };

  return (
    <div className="bg-mainColor text-textBlack pl-2 pr-2 w-full h-[50px] flex items-center justify-between text-xs">
      <div className="relative w-[15%] h-full">
        <Image
          src="/icon.png"
          fill
          style={{
            objectFit: "contain",
          }}
          sizes="40px"
          priority
          alt="commit pixel logo"
        />
      </div>
      {user?.githubNickname && (
        <>
          <div>{user.githubNickname}</div>
          <div className="flex items-center">
            <div className="line-clamp-1">
              {user.availablePixel}
            </div>
            <div className="ml-1 mr-1">/</div>
            <div>{user.totalCredit}</div>
            <div className="ml-1">p</div>
          </div>
          <div className="text-sm text-bgColor flex justify-center items-center">
            <FontAwesomeIcon onClick={clickLogout} icon={faRightFromBracket} />
          </div>
        </>
      )}
    </div>
  );
};

export default MobileNav;