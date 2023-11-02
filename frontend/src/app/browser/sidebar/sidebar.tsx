import LoginBtn from "@/components/loginBtn";
import BoardBtn from "@/components/boardBtn"
import PickerAccordion from "./accordion/pickerAccorion";
import UserInfoAccordion from "./accordion/userInfoAccordion";
import RankAccordion from "./accordion/rankAccordion";
import { useSelector } from "react-redux";
import { RootState } from "@/store";

const Sidebar = () => {
  const user = useSelector((state: RootState) => state.user);

  return user.nickName ? (
    <div className="no-scrollbar pb-20 flex flex-col col-span-1 bg-bgColor w-full h-full pr-10 overflow-y-scroll">
      <UserInfoAccordion />
      <PickerAccordion />
      <RankAccordion title="URL 랭킹" type="url" />
      <RankAccordion title="Pixel 랭킹" type="pixel" />
      <BoardBtn />
    </div>
  ) : (
    <div className="no-scrollbar pb-20 flex flex-col col-span-1 bg-bgColor w-full h-[90%] pr-10 overflow-y-scroll">
      <RankAccordion title="URL 랭킹" type="url" />
      <RankAccordion title="Pixel 랭킹" type="pixel" />
      <LoginBtn />
    </div>
  );
};

export default Sidebar;