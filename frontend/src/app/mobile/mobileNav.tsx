import { faDoorOpen, faRightFromBracket } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Image from "next/image";

const MobileNav = () => {
  return (
    <div className="bg-mainColor text-textBlack pl-2 pr-2 w-full h-[50px] flex items-center justify-between text-xs">
      <div className="relative w-[15%] h-full">
        <Image src="/icon.png" fill style={{
          objectFit: "contain",
        }} sizes="40px" priority alt="commit pixel logo" />
      </div>
      <div>githubNick</div>
      <div className="flex items-center">
        <div className="line-clamp-1">{Number(2400).toLocaleString()}</div>
        <div className="ml-1 mr-1">/</div>
        <div>{Number(14293).toLocaleString()}</div>
        <div className="ml-1">p</div>
      </div>
      <div className="text-sm text-bgColor flex justify-center items-center">
        <FontAwesomeIcon icon={faRightFromBracket} />
      </div>
    </div>
  );
};

export default MobileNav;