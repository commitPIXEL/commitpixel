const TutorialDesc = ({isMobile}: {
  isMobile?: boolean,
}) => {
  const mobileClass = "w-fit flex flex-col mb-16";
  return (
    <div className={isMobile ? mobileClass : "h-[60%] text-xl flex flex-col"}>
      <div className="text-4xl mb-6 row-span-1">튜토리얼</div>
      <div className="col-span-1 row-span-1">
        <div className="flex flex-col">
          <div className="flex items-end">
            <div className="text-3xl">commitPixel</div>
            <div className="ml-1">은</div>
          </div>
          <div className="flex items-end mt-1">
            <div className=" text-mainColor text-2xl">Github commit 수</div>
            <div className="ml-1">를 기반으로</div>
          </div>
          <div className="flex items-end mt-1">
            <div className="text-mainColor text-2xl">픽셀을 색칠</div>
            <div className="ml-1">하고</div>
          </div>
          <div className="flex items-end mt-1">
            <div className="mr-1">자신의</div>
            <div className="text-mainColor text-2xl"> URL을 홍보</div>
            <div className="ml-1">하는 웹서비스 입니다.</div>
          </div>
        </div>
      </div>
      <div className="flex flex-col mt-6">
        <div className="mb-1">Github commit이 부족하다면 </div>
        <div className="mb-1 text-mainColor text-2xl font">
          백준 solved.ac와 연동
        </div>
        <div className="whitespace-nowrap">하여 푼 문제만큼 찍을 수 있는 픽셀을 제공합니다.</div>
      </div>
    </div>
  );
};

export default TutorialDesc;