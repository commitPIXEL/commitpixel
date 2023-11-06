"use client";

import { Accordion, AccordionDetails, Input } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencil} from "@fortawesome/free-solid-svg-icons";
import React, { useEffect, useRef, useState } from "react";
import SolvedacBtn from "@/components/solvedacBtn";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import Loading from "@/components/loading";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import { setUrlInputOff, setUrlInputOn } from "@/store/slices/urlInputSlice";

const UserInfoAccordion = () => {
  const dispatch = useDispatch();
  const customFetch = useFetchWithAuth();
  const urlInputRef = useRef<HTMLInputElement | null>(null);
  const user = useSelector(
    (state: RootState) => state.user
  );
  const [isEdit, setIsEdit] = useState(false);
  const [url, setUrl] = useState("");
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
    try {
      setLoading(true);
      const resFromUser = await customFetch("/user/url", {
        method: "PATCH",
        body: JSON.stringify({ url: url }),
      });

      console.log(resFromUser);

      const data: {newUrl: string} = await resFromUser.json(); 
      console.log(data)
      setUrl(data.newUrl);
      window.alert("홍보 url이 변경되었습니다!");
    } catch (err) {
        setUrl("변경 예정");
        console.error("Error:", err);
        window.alert("인가된 url이 아닙니다!");
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
      <AccordionTitle title={user?.githubNickname} profileImage={user?.profileImage} />
      <AccordionDetails className="flex flex-col justify-center items-center pt-4 rounded-b">
        <div className="w-full flex justify-between items-center mb-4">
          <div className="text-lg text-textGray">Pixel</div>
          <div className="flex justify-between text-textBlack">
            <div>{ user.availablePixel }</div>
            <div className="ml-2 mr-2">/</div>
            <div>{ user.totalCredit }</div>
          </div>
        </div>
        <div className="w-full flex items-center">
          <div className="w-fit text-textGray text-sm mr-2">홍보 URL</div>
          <FontAwesomeIcon onClick={handleEditClick} className="text-textGray cursor-pointer" icon={faPencil} />
        </div>
        <div className="w-full mt-1 pb-1">
          <Input onKeyDown={handleEnterClick} onChange={handleInputChange} inputRef={urlInputRef} className="w-full text-xs line-clamp-1" disabled={!isEdit} defaultValue={user.url} />
        </div>
        <div className="w-full mt-4">
          <SolvedacBtn />
        </div>
        <Loading open={loading} />
      </AccordionDetails>
    </Accordion>
  );
};

export default UserInfoAccordion;
