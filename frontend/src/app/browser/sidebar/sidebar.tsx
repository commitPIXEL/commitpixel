import Picker from "./picker";
import LoginBtn from "@/components/loginBtn";
import BoardBtn from "@/components/boardBtn"

const Sidebar = () => {
  const clientId = process.env.NEXT_PUBLIC_CLIENT_ID;
  const redirectUrl = process.env.NEXT_PUBLIC_CALLBACK_URL;

  return (
  <div className="col-span-1 bg-white w-full h-full">
    <LoginBtn clientId={clientId as string} redirectUrl={redirectUrl as string} />
    <BoardBtn />
    <Picker />
  </div>);
};

export default Sidebar;