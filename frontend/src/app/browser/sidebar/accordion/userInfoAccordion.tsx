"use client";

import { Accordion, AccordionDetails, Input } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencil} from "@fortawesome/free-solid-svg-icons";
import React, { useEffect, useRef, useState } from "react";
import SolvedacBtn from "@/components/solvedacBtn";
import useFetchAuth from "@/hooks/useFetchAuth";
import Loading from "@/components/loading";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import { setUrlInputOff, setUrlInputOn } from "@/store/slices/urlInputSlice";
import RefreshBtn from "@/components/refreshBtn";
import { updateUrl } from "@/store/slices/userSlice";
import BoardInput from "@/components/boardInput";

const UserInfoAccordion = () => {
  const dispatch = useDispatch();
  const customFetch = useFetchAuth();
  const urlInputRef = useRef<HTMLInputElement | null>(null);
  const user = useSelector(
    (state: RootState) => state.user
  );
  const [open, setOpen] = useState(false);
  const [isEdit, setIsEdit] = useState(false);
  const [url, setUrl] = useState(user.url);
  const [isChangeUrl, setIsChangeUrl] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if(isEdit) {
      dispatch(setUrlInputOn());
    } else {
      dispatch(setUrlInputOff());
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isEdit]);

  const urlUpdate = async () => {
    const lowerUrl = url.toLowerCase();
    const isHttp = lowerUrl.startsWith("https://") || lowerUrl.startsWith("http://");
    if(!isHttp) {
      window.alert("잘못된 형식의 url입니다. 입력 예시를 확인해 주세요.");
      setUrl(user.url);
      return;
    }

    try {
      setLoading(true);
      const resFromUser = await customFetch("/user/url", {
        method: "PATCH",
        body: JSON.stringify({ url: url }),
      });
      const resUrl: string = await resFromUser.text();

      if(resUrl === user.url) {
        window.alert("인가된 url이 아닙니다!");
        setOpen(true);
      } else {
        dispatch(updateUrl({url: resUrl}));
        window.alert("홍보 url이 변경되었습니다!");
      }
      setUrl(resUrl);
    } catch (err) {
        console.log(err);
        window.alert("알 수 없는 에러...");
    } finally {
      setLoading(false);
    }
  }

  const handleEditClick = () => {
    if(!isEdit) {
      setIsEdit(true);
      setTimeout(() => {
        urlInputRef.current?.focus();
      }, 100);
    } else {
      setIsEdit(false);
      if(isChangeUrl) urlUpdate();
    }
  };

  const handleInputChange = (e: any) => {
    setUrl(e.target.value);
    setIsChangeUrl(true);
  };

  const handleEnterClick = (e: any) => {
    if(e.keyCode === 13) {
      setIsEdit(false);
      if(isChangeUrl) urlUpdate();
    }
  };

  return (
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle
        title={user?.githubNickname}
        profileImage={user?.profileImage}
      />
      <AccordionDetails className="flex flex-col justify-center items-center rounded-b">
        <div className="w-full flex justify-between items-center mb-4">
          <div className="flex items-center gap-1">
            <div className="text-lg text-textGray h-full">Pixel</div>
            <RefreshBtn />
          </div>
          <div className="flex justify-between text-textBlack">
            <div>{user.availablePixel}</div>
            <div className="ml-2 mr-2">/</div>
            <div>{user.totalCredit}</div>
          </div>
        </div>
        <div className="w-full flex items-center">
          <div className="w-fit text-textGray text-sm mr-2">홍보 URL</div>
          <FontAwesomeIcon
            onClick={handleEditClick}
            className="text-textGray cursor-pointer"
            icon={faPencil}
          />
        </div>
        <div className="w-full mt-1 pb-1">
          <Input
            onKeyDown={handleEnterClick}
            value={url}
            onChange={handleInputChange}
            inputRef={urlInputRef}
            className="w-full text-xs line-clamp-1"
            disabled={!isEdit}
          />
          <span className="text-xs text-gray-300 opacity-75 mt-2">
            입력 예시: https://commitpixel.com<br/>
            http도 가능합니다.
          </span>
        </div>
        {!user.isSolvedACAuth && (
          <div className="w-full mt-4">
            <SolvedacBtn />
          </div>
        )}
        <Loading open={loading} />
      </AccordionDetails>
      <BoardInput open={open} setOpen={setOpen} />
    </Accordion>
  );
};

export default UserInfoAccordion;
