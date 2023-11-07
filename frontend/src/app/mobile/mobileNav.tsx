import { RootState } from "@/store";
import { faRightFromBracket } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Image from "next/image";
import { useSelector } from "react-redux";
import LoginBtn from "@/components/loginBtn";

const MobileNav = () => {
  const user = useSelector((state: RootState) => state.user);
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
      { user?.githubNickname !== "" ?
        <>
          <div>{user.githubNickname}</div>
          <div className="flex items-center">
            <div className="line-clamp-1">
              {user.availablePixel.toLocaleString()}
            </div>
            <div className="ml-1 mr-1">/</div>
            <div>{user.totalCredit.toLocaleString()}</div>
            <div className="ml-1">p</div>
          </div>
          <div className="text-sm text-bgColor flex justify-center items-center">
            <FontAwesomeIcon icon={faRightFromBracket} />
          </div>
        </> : <LoginBtn color={"main"} />
      }
    </div>
  );
};

export default MobileNav;