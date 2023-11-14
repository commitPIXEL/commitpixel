import { useDispatch, useSelector } from "react-redux";
import CanvasContainer from "../browser/canvas/canvasContainer";
import Menu from "../browser/sidebar/picker/menu/pickerMenu";
import MobileNav from "./mobileNav";
import MobilePicker from "./mobilePicker";
import { RootState } from "@/store";
import LoginBtn from "@/components/loginBtn";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import { useEffect } from "react";
import { getUserInfo, updateUserPixel } from "@/store/slices/userSlice";
import { IUserInfo, IUserPixel } from "@/interfaces/browser";

const Mobile = () => {
  const dispatch = useDispatch();
  const customFetch = useFetchWithAuth();
  const user = useSelector((state: RootState) => state.user);
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );

  useEffect(() => {
    if (accessToken && !user.githubNickname) {
      fetchAsync();
    }
  }, [accessToken]);

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

      dispatch(updateUserPixel(pixelData));
    } catch (err) {
      console.error("Error:", err);
    }
  };


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