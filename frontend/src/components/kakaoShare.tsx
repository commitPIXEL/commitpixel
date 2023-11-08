"use client";

const KakaoShare = () => {
  const handleKakaoShare = () => {
    if (window.Kakao && !window.Kakao.isInitialized()) {
      window.Kakao.init(process.env.NEXT_PUBLIC_JS_URL);
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
        className={`flex justify-around items-center space-x-4 font-bold text-bgColor p-2 bg-white rounded h-12 mt-6 mb-6`}
        onClick={handleKakaoShare}
      >
        <img
          className="w-6 h-6"
          src="https://developers.kakao.com/assets/img/about/logos/kakaotalksharing/kakaotalk_sharing_btn_medium.png"
          alt="카카오톡 공유 보내기 버튼 "
        />
        <p>KakaoTalk Share</p>
      </button>
    </>
  );
};

export default KakaoShare;
