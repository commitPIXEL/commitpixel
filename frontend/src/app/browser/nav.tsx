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

  const handleTutorialClick = () => {
    router.push("tutorials");
  };

  return user.githubNickname ? (
    <div className="bg-mainColor w-full h-[8%] flex justify-between items-center">
      <div className="flex w-full h-full items-center">
        <div className="relative w-[5%] h-full">
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
        <div className="flex flex-col justify-center">
          <div className="text-xl font-bold">commit</div>
          <div className="text-xl font-bold">Pixel</div>
        </div>
      </div>
      <div onClick={handleTutorialClick} className="text-bgColor ml-16 cursor-pointer text-xl">Tutorials & Patch Note</div>
      <div>
        <Button variant="text" color="inherit" onClick={clickLogout} className="text-xl font-bold">LOGOUT</Button>
      </div>
    </div>
  ) : (
    <div className="bg-mainColor w-full h-[8%] flex items-center">
      <div className="relative w-[5%] h-full">
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
      <div className="flex flex-col justify-center">
        <div className="text-xl font-bold">commit</div>
        <div className="text-xl font-bold">Pixel</div>
      </div>
      <div onClick={handleTutorialClick} className="text-bgColor ml-16 cursor-pointer text-xl">Tutorials & Patch Note</div>
    </div>
  );
}

export default Nav;
