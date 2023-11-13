const PatchNote = ({ isMobile }: {
  isMobile?: boolean,
}) => {
  return (
    <div className={isMobile ? "mb-16 h-[60%]" : "h-[40%]"}>
      <div className="text-4xl">패치노트</div>
      <div className="mt-1 h-[90%] overflow-y-scroll no-scrollbar p-2 cursor-ns-resize">
        <div>
          <div className="text-mainColor text-xl">ver 1.0.0</div>
          <div className="mt-2 flex flex-col">
            <div className="text-mainColor mb-2">- 공유 캔버스 기능</div>
            <div className="text-mainColor mb-2">- 픽셀 클릭으로 URL 보기</div>
            <div className="text-mainColor mb-2">- 픽셀 색칠로 URL 홍보하기</div>
            <div className="text-mainColor mb-2">- 모바일 뷰 데모</div>
            <div className="text-mainColor">- Github 로그인 + Solved.ac 연동</div>
            <ul className="ml-4 mb-2">
              <li className="text-xs">
                Solver.ac 프로필 메시지에 commitpixel.com 추가 시 연동 가능
              </li>
            </ul>
            <div className="text-mainColor mb-2">- 사용자 정보 데이터</div>
            <ul className="ml-4 mb-2">
              <li>사용자 프로필</li>
              <li>사용자 닉네임</li>
              <li>
                <div>픽셀 데이터</div>
                <ol>
                  <li>픽셀 정보 새로고침 15분 쿨타임</li>
                  <li>사용 가능 픽셀 수</li>
                  <li>총 픽셀 수</li>
                </ol>
              </li>
            </ul>
            <div className="text-mainColor">- 건의하기</div>
            <ul className="ml-4 mb-2">
              <li>일반 건의 - 서비스 불편사항 건의</li>
              <li>특수 건의 - URL 인가 요청</li>
            </ul>
            <ul></ul>
            <div className="text-mainColor">- 홍보 가능 URL 목록</div>
            <div className="ml-4 text-mainColor">
              {"(한다솜[서울_7반_A709]팀원으로 MM 주시면 바로 추가해드립니다)"}
            </div>
            <ul className="ml-4">
              <li>github.com</li>
              <li>gitlab.com</li>
              <li>naver.com</li>
              <li>notion.site</li>
              <li>notion.so</li>
              <li>solved.ac</li>
              <li>tistory.com</li>
              <li>bnkrmall.co.kr</li>
              <li>commitpixel.com</li>
            </ul>
          </div>
        </div>
        <div>
          <div className="text-mainColor text-xl">ver 1.1.0</div>
          <div className="mt-2 flex flex-col">
            <div className="text-mainColor mb-2">- 현재 캔버스에서 가장 많은 픽셀을 차지한 사용자 랭킹</div>
            <div className="text-mainColor mb-2">- 현재 캔버스에서 가장 많이 홍보된 URL 랭킹</div>
            <div className="text-mainColor mb-2">- 원하는 이미지를 64픽셀로 바꿔서 참고해보세요, 이미지 픽셀화 기능</div>
            <div className="text-mainColor mb-2">- 오늘 하루 픽셀의 변화를 볼 수 있는 타임랩스</div>
            <div className="text-mainColor mb-2">- 카카오 공유하기 기능</div>
          </div>
        </div>
        <div>
          <div className="text-mainColor text-xl">ver 2.0.0</div>
          <div className="mt-2 flex flex-col">
            <div className="text-mainColor mb-2">- 오늘 하루 픽셀 변화를 볼 수 있던 타임랩스를 전체 기간 픽셀 변화를 볼 수 있는 타임랩스로 업그레이드</div>
            <div className="text-mainColor mb-2">- 이제는 편하게 이미지 보면서 픽셀 찍기! 이미지 픽셀화 기능 이미지 트레이싱 툴로 업그레이드</div>
            <div className="text-mainColor mb-2"> 버그 수정</div>
            <ul className="ml-4 mb-2">
              <li className="text-xs">No Pixel Data 문제 수정</li>
              <li className="text-xs">랭킹에서 21위까지 보여주는 문제 수정</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PatchNote;
