import LoginBtn from "@/components/loginBtn";
import BoardBtn from "@/components/boardBtn"
import PickerAccordion from "./accordion/pickerAccorion";
import UserInfoAccordion from "./accordion/userInfoAccordion";
import RankAccordion from "./accordion/rankAccordion";
import useSocket from "@/hooks/useSocket";

const Sidebar = () => {
  const clientId = process.env.NEXT_PUBLIC_CLIENT_ID;
  const redirectUrl = process.env.NEXT_PUBLIC_CALLBACK_URL;
  const { socket } = useSocket();
  const isConnected = socket ? true : false;

  return (
    <div className="no-scrollbar pb-20 flex flex-col col-span-1 bg-bgColor w-full h-[90%] pr-10 overflow-y-scroll">
      <UserInfoAccordion />
      <PickerAccordion isConnected={isConnected} />
      <RankAccordion title="URL 랭킹" type="url" />
      <RankAccordion title="Pixel 랭킹" type="pixel" />
      <LoginBtn
        clientId={clientId as string}
        redirectUrl={redirectUrl as string}
      />
      <BoardBtn />
    </div>
  );
};

export default Sidebar;