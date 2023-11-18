import { faChartColumn, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Tooltip } from "@mui/material";
import { useRouter } from "next/navigation";
import InstallDesktopIcon from '@mui/icons-material/InstallDesktop';
import React, { useEffect, useState } from 'react';
import { IBeforeInstallPromptEvent, IBeforeInstallPromptEventListener } from '@/interfaces/browser';

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

export const InstallPWA = () => {
  const [installPrompt, setInstallPrompt] = useState<IBeforeInstallPromptEvent | null>(null);

  useEffect(() => {
    const handleBeforeInstallPrompt: IBeforeInstallPromptEventListener = (e) => {
      e.preventDefault();
      setInstallPrompt(e);
    };

    window.addEventListener('beforeinstallprompt', handleBeforeInstallPrompt as EventListener);

    return () => {
      window.removeEventListener('beforeinstallprompt', handleBeforeInstallPrompt as EventListener);
    };
  }, []);

  const isMobileView = window.innerWidth < 769 && window.innerWidth <= window.innerHeight;

  const handleInstallClick = async () => {
    if (installPrompt) {
      await installPrompt.prompt();
      const choiceResult = await installPrompt.userChoice;
      if (choiceResult.outcome === "accepted") {
        console.log("사용자가 설치 승인");
        setInstallPrompt(null);
      } else {
        console.log("사용자가 설치 거절");
      }
    }
  };

  if (!installPrompt) return null;

  return isMobileView ? (
    <button className="rounded-full" onClick={handleInstallClick}>
      <InstallDesktopIcon />
    </button>
  ) : (
    <div
      onClick={handleInstallClick}
      className="text-bgColor cursor-pointer text-lg flex flex-col justify-center items-center hover:text-gray-100 ease-in-out"
    >
      <Tooltip arrow title="App 설치하기">
        <InstallDesktopIcon className="w-[30px] h-[30px]" />
      </Tooltip>
    </div>
  );
}
