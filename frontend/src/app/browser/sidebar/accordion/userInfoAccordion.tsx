import { Accordion, AccordionDetails, Input } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencil} from "@fortawesome/free-solid-svg-icons";
import React, { useRef, useState } from "react";

const UserInfoAccordion = () => {
  const urlInputRef = useRef<HTMLInputElement | null>(null);
  const [isEdit, setIsEdit] = useState(false);
  const [url, setUrl] = useState("");

  const handleEditClick = () => {
    if(!isEdit) {
      setIsEdit(true);
      setTimeout(() => {
        urlInputRef.current?.focus();
      }, 100);
    } else {
      setIsEdit(false);
    }
  };

  const handleInputChange = (e: any) => {
    setUrl(e.target.value);
  };

  const handleEnterClick = (e: any) => {
    if(e.keyCode === 13) {
      setIsEdit(false);
    }
  };

  return (
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle title={"githubNick"} isProfile={true} />
      <AccordionDetails className="flex flex-col justify-center items-center pt-4 rounded-b">
        <div className="w-full flex justify-between items-center mb-4">
          <div className="text-lg text-textGray">Pixel</div>
          <div className="flex justify-between text-textBlack">
            <div>{ Number(2400).toLocaleString("ko-KR") }</div>
            <div className="ml-2 mr-2">/</div>
            <div>{ Number(14293).toLocaleString("ko-KR") }</div>
          </div>
        </div>
        <div className="w-full flex items-center">
          <div className="w-fit text-textGray text-sm mr-2">홍보 URL</div>
          <FontAwesomeIcon onClick={handleEditClick} className="text-textGray cursor-pointer" icon={faPencil} />
        </div>
        <div className="w-full mt-1 pb-1">
          <Input onKeyDown={handleEnterClick} onChange={handleInputChange} inputRef={urlInputRef} className="w-full text-xs line-clamp-1" disabled={!isEdit} />
        </div>
      </AccordionDetails>
    </Accordion>
  );
};

export default UserInfoAccordion;
