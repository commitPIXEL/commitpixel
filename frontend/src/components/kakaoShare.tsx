"use client";

import Image from "next/image";

declare const window: Window & typeof globalThis & {
  Kakao: any;
};

const KakaoShare = ({color}: {
  color?: string,
}) => {
  const handleKakaoShare = () => {
    if (window.Kakao && !window.Kakao.isInitialized()) {
      window.Kakao.init(process.env.NEXT_PUBLIC_JS_KEY);
    }

    if (window.Kakao.isInitialized()) {
      const templateId = Number(process.env.NEXT_PUBLIC_TEMPLATE_ID);

      window.Kakao.Share.sendCustom({
        templateId: templateId,
      });
    }
  };

  return (
    <>
      <button
        className={`flex justify-around items-center mb-6 space-x-4 font-bold text-bgColor ${color === "main" ? " bg-mainColor " : " bg-white "} rounded h-12 mt-6 p-3`}
        onClick={handleKakaoShare}
      >
        <Image
          src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png"
          alt="카카오톡 공유 보내기 버튼 "
          width={24}
          height={24}
        />
        <p className="flex-1">Kakao Share</p>
      </button>
    </>
  );
};

export default KakaoShare;
