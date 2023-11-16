import { faChartColumn, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Tooltip } from "@mui/material";
import { useRouter } from "next/navigation";

export const TutorialsBtn = () => {
  const router = useRouter();
  const handleTutorialClick = () => {
    router.push("tutorials");
  };
  return (
    <div
      onClick={handleTutorialClick}
      className="text-bgColor cursor-pointer text-lg flex flex-col justify-center items-center hover:text-gray-100 ease-in-out"
    >
      <Tooltip arrow title="튜토리얼과 패치노트 보기">
        <FontAwesomeIcon className="h-[30px]" icon={faInfoCircle} />
      </Tooltip>
    </div>
  );
};

export const FlourishBtn = () => {
  const handleFlourishClick = () => {
    window.open("https://public.flourish.studio/visualisation/15619978/", "_blank", "noopener,noreferrer");
  };
  return (
    <div
      onClick={handleFlourishClick}
      className="text-bgColor ml-8 mr-8 cursor-pointer text-lg flex flex-col justify-center items-center ease-in-out hover:text-gray-100"
    >
      <Tooltip arrow title="Flourish로 인터랙티브 사용자 랭킹 변화를 구경하기!">
        <FontAwesomeIcon className="h-[30px]" icon={faChartColumn} />
      </Tooltip>
    </div>
  );
};
