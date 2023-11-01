const TutorialDesc = () => {
  return (
    <div className="h-[40%] text-xl grid grid-cols-1 grid-rows-3">
      <div className="text-4xl mb-8 row-span-1">튜토리얼</div>
      <div className="col-span-1 row-span-1">
        <div>
          <span className="text-3xl">commitPixel</span>은
          <span className="ml-4 text-mainColor text-2xl">
            {" "}
            Github commit 수
          </span>
          를 기반으로
        </div>
        <div className="mt-2">
          <span className="text-mainColor text-2xl">픽셀을 색칠</span>
          하고<span className="mr-4"></span> 자신의
          <span className="text-mainColor text-2xl"> URL을 홍보</span>
          하는 웹서비스 입니다.
        </div>
      </div>
      <div className="mt-4">
        <div>Github commit이 부족하다면 <span className="text-mainColor text-2xl font">백준 solved.ac와 연동</span>하여</div>
        <div>푼 문제만큼 찍을 수 있는 픽셀을 제공합니다.</div>
      </div>
    </div>
  );
};

export default TutorialDesc;