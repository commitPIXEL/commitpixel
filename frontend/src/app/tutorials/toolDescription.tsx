import { faEyeDropper, faPaintBrush } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import MoveButton from "./moveButton";

const ToolDescription = () => {
  return (
    <div className="w-full h-full grid grid-cols-2 gap-4 grid-rows-7 text-xl items-center">
      <img
        className="place-self-center"
        src="/video/mouseleft.gif"
        width={"75px"}
        height={"75px"}
        alt="url 클릭"
      />
      <div className="place-self-center">픽셀 URL 보기</div>
      <img
        src="/video/mouseright.gif"
        width={"75px"}
        height={"75px"}
        alt="이동하기"
        className="place-self-center"
      />
      <div className="place-self-center">캔버스 이동</div>
      <div className="place-self-center flex items-center justify-between w-[50%]">
        <div
          className={`flex justify-center items-center ml-1 mr-1 p-2 text-xl border-2 rounded-full aspect-square w-10 bg-mainColor text-bgColor`}
        >
          <FontAwesomeIcon icon={faPaintBrush} />
        </div>
        <div>+</div>
        {/* <div>마우스 왼쪽 클릭:</div> */}
        <img
          src="/video/mouseleft.gif"
          width={"75px"}
          height={"75px"}
          alt="색칠하기"
          className="place-self-center"
        />
      </div>
      <div className="place-self-center">색칠하기</div>
      <div className="place-self-center flex items-center justify-between w-[50%]">
        <div
          className={`flex justify-center items-center ml-1 mr-1 p-2 text-xl border-2 rounded-full aspect-square w-10 bg-mainColor text-bgColor`}
        >
          <FontAwesomeIcon icon={faEyeDropper} />
        </div>
        <div>+</div>
        {/* <div>마우스 왼쪽 클릭:</div> */}
        <img
          src="/video/mouseleft.gif"
          width={"75px"}
          height={"75px"}
          alt="복사하기"
          className="place-self-center"
        />
      </div>
      <div className="place-self-center">복사하기</div>
      <div className="place-self-center">B 키:</div>
      <div className="place-self-center">브러시</div>
      <div className="place-self-center">I 키:</div>
      <div className="place-self-center">스포이드</div>
      <div className="place-self-center">Esc 키:</div>
      <div className="place-self-center">도구 해제</div>
      <div className="col-span-2 row-span-1 place-self-end">
        <MoveButton />
      </div>
    </div>
  );
}

export default ToolDescription;