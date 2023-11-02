"use client"
import Nav from "../browser/nav";
import TutorialDesc from "./tutorialDescription";
import MoveButton from "./moveButton";
import { faEyeDropper, faPaintBrush } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import PatchNote from "./patchNote";
import { useEffect } from "react";

export default function Tutorials () {
  useEffect(() => {
    localStorage.setItem("isVisited", JSON.stringify(true));
  }, []);

  return (
    <div className="w-screen h-screen">
      <Nav />
      <div className="bg-bgColor w-full h-[92%] text-white flex flex-col p-6">
        <div className="flex w-full h-full justify-between">
          <div className="w-[50%] flex flex-col justify-between h-full">
            <TutorialDesc />
            <PatchNote />
          </div>
          <div className="w-[50%] h-full">
            <div className="w-full h-full grid grid-cols-2 grid-rows-7 text-xl items-center">
              {/* <div className="col-span-1 row-span-1">마우스 왼쪽 클릭: </div> */}
              <img src="/video/mouseleft.gif" width={"75px"} height={"75px"} alt="url 클릭" />
              <div className="col-span-1 row-span-1">픽셀 URL 정보 보기</div>
              {/* <div className="col-span-1 row-span-1">마우스 오른쪽 클릭:</div> */}
              <img src="/video/mouseright.gif" width={"75px"} height={"75px"} alt="이동하기" />
              <div className="col-span-1 row-span-1">캔버스 이동</div>
              <div className="col-span-1 row-span-1 flex items-center justify-between w-[50%]">
                <div
                  className={`flex justify-center items-center ml-1 mr-1 p-2 text-xl border-2 rounded-full aspect-square w-10 bg-mainColor text-bgColor`}
                >
                  <FontAwesomeIcon icon={faPaintBrush} />
                </div>
                <div>+</div>
                {/* <div>마우스 왼쪽 클릭:</div> */}
                <img src="/video/mouseleft.gif" width={"75px"} height={"75px"} alt="색칠하기" />
              </div>
              <div className="col-span-1 row-span-1">색칠하기</div>
              <div className="col-span-1 row-span-1 flex items-center justify-between w-[50%]">
                <div
                  className={`flex justify-center items-center ml-1 mr-1 p-2 text-xl border-2 rounded-full aspect-square w-10 bg-mainColor text-bgColor`}
                >
                  <FontAwesomeIcon icon={faEyeDropper} />
                </div>
                <div>+</div>
                {/* <div>마우스 왼쪽 클릭:</div> */}
                <img src="/video/mouseleft.gif" width={"75px"} height={"75px"} alt="복사하기" />
              </div>
              <div className="col-span-1 row-span-1">복사하기</div>
              <div className="col-span-1 row-span-1">B 키:</div>
              <div className="col-span-1 row-span-1">브러시</div>
              <div>I 키:</div>
              <div className="col-span-1 row-span-1">스포이드</div>
              <div className="col-span-1 row-span-1">Esc 키:</div>
              <div className="col-span-1 row-span-1">도구 해제</div>
              <div className="col-span-2 row-span-1 place-self-end">
                <MoveButton />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
