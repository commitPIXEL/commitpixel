import Image from "next/image";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import {resetUser} from "@/store/slices/userSlice"
import { logout } from "@/store/slices/authorizationSlice";
import Button from '@mui/material/Button';
import { useRouter } from "next/navigation";

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

  const handleTutorialClick = () => {
    router.push("tutorials");
  };

  const handleFlourishClick = () => {
    router.push("https://public.flourish.studio/visualisation/15619978/");
  };

  return user.githubNickname ? (
    <div className="bg-mainColor w-full h-[8%] flex justify-between items-center pl-2 pr-10">
      <div className="flex w-[50%] h-full items-center">
        <div className="relative w-[10%] h-full cursor-pointer" onClick={handleLogoClick}>
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
        <div className="flex flex-col justify-center cursor-pointer" onClick={handleLogoClick}>
          <div className="text-xl font-bold">commit</div>
          <div className="text-xl font-bold">Pixel</div>
        </div>
      </div>
      <div
        onClick={handleTutorialClick}
        className="text-bgColor ml-16 cursor-pointer text-lg flex flex-col justify-center items-center"
      >
        <div>Tutorials &</div>
        <div>Patch Note</div>
      </div>
      <div
        onClick={handleFlourishClick}
        className="text-bgColor ml-16 cursor-pointer text-lg flex flex-col justify-center items-center"
      >
        <div>Flourish</div>
        <div>랭킹 변화</div>
      </div>
      <div>
        <Button
          variant="text"
          color="inherit"
          onClick={clickLogout}
          className="text-xl font-bold"
        >
          LOGOUT
        </Button>
      </div>
    </div>
  ) : (
    <div className="bg-mainColor w-full h-[8%] flex items-center pl-2 pr-10">
      <div className="flex w-[20%] h-full justify-start items-center">
        <div className="relative w-[20%] h-full cursor-pointer" onClick={handleLogoClick}>
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
        <div className="flex flex-col justify-center cursor-pointer" onClick={handleLogoClick}>
          <div className="text-xl font-bold">commit</div>
          <div className="text-xl font-bold">Pixel</div>
        </div>
      </div>
      <div className="flex items-center justify-between w-[20%]">
        <div
          onClick={handleTutorialClick}
          className="text-bgColor cursor-pointer text-lg flex flex-col justify-center items-center"
        >
          <div>Tutorials &</div>
          <div>Patch Note</div>
        </div>
        <div
          onClick={handleFlourishClick}
          className="text-bgColor cursor-pointer text-lg flex flex-col justify-center items-center"
        >
          <div>Flourish</div>
          <div>랭킹 변화</div>
        </div>
      </div>
    </div>
  );
}

export default Nav;
