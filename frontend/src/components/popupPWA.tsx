import { IBeforeInstallPromptEvent, IBeforeInstallPromptEventListener } from "@/interfaces/browser";
import Image from "next/image";
import { useEffect, useState } from "react";
import IosShareIcon from '@mui/icons-material/IosShare';

const PopupPWA = () => {
    const [installPrompt, setInstallPrompt] = useState<IBeforeInstallPromptEvent | null>(null);
    const [isIOS, setIsIOS] = useState(false);
    const [isShown, setIsShown] = useState(false);
    const [isChecked, setIsChecked] = useState(false);

  useEffect(() => {
    const isDeviceIOS = /iPad|iPhone|iPod/.test(navigator.userAgent);
    setIsIOS(isDeviceIOS);

    const handleBeforeInstallPrompt: IBeforeInstallPromptEventListener = (e) => {
      const disableUntil = localStorage.getItem("disablePopupUntil");
      if (disableUntil && new Date() < new Date(disableUntil)) {
        return;
      }
    
      e.preventDefault();
      setInstallPrompt(e);
      setIsShown(true);
    };

    window.addEventListener('beforeinstallprompt', handleBeforeInstallPrompt as EventListener);

    return () => {
      window.removeEventListener('beforeinstallprompt', handleBeforeInstallPrompt as EventListener);
    };
  }, []);

  const handleInstallClick = async () => {
    setIsShown(false);
    if (installPrompt) {
      await installPrompt.prompt();
      const choiceResult = await installPrompt.userChoice;
      if (choiceResult.outcome === "accepted") {
        console.log("사용자가 설치 승인");
        setInstallPrompt(null);
      } else {
        console.log("사용자가 설치 거절");
        const oneMinutesLater = new Date();
        oneMinutesLater.setMinutes(oneMinutesLater.getMinutes() + 1);
        localStorage.setItem(
          "disablePopupUntil",
          oneMinutesLater.toISOString()
        );
      }
    }
  };

  const handleClose = () => {
    if(isChecked) {
      const oneWeekLater = new Date();
      oneWeekLater.setDate(oneWeekLater.getDate() + 7);
      localStorage.setItem("disablePopupUntil", oneWeekLater.toISOString());
    }
    setIsShown(false);
  }

  const handleCheckboxChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setIsChecked(event.target.checked);
  };

  if (!isIOS && !isShown) return null;

  if(!isIOS && isShown) {
    return (
      <div className="fixed bottom-0 left-0 right-0 bg-gray-600 bg-opacity-50 h-full w-full flex justify-center items-end z-50">
        <div className="mx-auto p-5 border w-full shadow-lg bg-white rounded-t-lg">
          <div className="flex justify-center mb-4">
            <Image
              src="/static/images/pixelFox.png"
              alt="PixelFox 아이콘"
              width={48}
              height={48}
            />
          </div>
          <p className="text-center text-gray-700 text-sm mb-2">
            <span className="text-yellow-500">commitPixel</span>은 앱에서 원활한
            사용을 할 수 있습니다.
          </p>
          <p className="text-center text-gray-900 text-lg mb-4">
            설치하시겠습니까?
          </p>
          <div className="flex justify-center mb-2">
            <button
              onClick={handleInstallClick}
              className="py-2 px-4 bg-blue-500 hover:bg-blue-700 text-white font-bold mr-2"
            >
              설치하기
            </button>
            <button
              onClick={handleClose}
              className="py-2 px-4 bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold"
            >
              닫기
            </button>
          </div>
          <div className="flex justify-end mt-2">
            <label className="flex items-center space-x-2">
              <input
                type="checkbox"
                checked={isChecked}
                onChange={handleCheckboxChange}
                className="form-checkbox text-blue-500"
              />
              <span className="text-sm text-gray-700">일주일간 보지 않기</span>
            </label>
          </div>
        </div>
      </div>
    );
  }

  if(isIOS && isShown) {
    return (
      <div className="fixed bottom-0 left-0 right-0 bg-gray-600 bg-opacity-50 h-full w-full flex justify-center items-end">
        <div className="mx-auto mb-10 p-5 border w-full shadow-lg bg-white">
          <div className="flex justify-center mb-4">
            <Image
              src="/static/images/pixelFox.png"
              alt="카카오톡 공유 보내기 버튼 "
              width={48}
              height={48}
            />
          </div>
          <p className="text-center text-gray-700 text-sm mb-2">
            <span className="text-yellow-500">commitPixel</span>은 앱에서 원활한
            사용을 할 수 있습니다.
          </p>
          <p className="text-center text-gray-900 text-lg mb-4">
            <IosShareIcon fontSize="large" />를 클릭 후 &quot;
            <span>홈 화면에 추가하기</span>&quot;를 통해
            설치해주세요
          </p>
          <div className="flex justify-center">
            <button
              onClick={handleInstallClick}
              className="py-2 px-4 mr-2 bg-blue-500 hover:bg-blue-700 text-white font-bold"
            >
              설치하기
            </button>
            <button
              onClick={() => setIsShown(false)}
              className="py-2 px-4 bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold"
            >
              닫기
            </button>
          </div>
        </div>
      </div>
    );
  }
}

export default PopupPWA;