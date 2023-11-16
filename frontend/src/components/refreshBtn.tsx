"use client";

import { Tooltip } from "@mui/material";
import useFetchAuth from "@/hooks/useFetchAuth";
import { IUserPixel } from "@/interfaces/browser";
import { useDispatch } from "react-redux";
import { updateUserPixel } from "@/store/slices/userSlice";
import { faArrowsRotate } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { NOT_PASS_ENOUGH_TIME } from "@/constants/message";

const RefreshBtn = () => {

    const dispatch = useDispatch();
    const customFetch = useFetchAuth();
    const [loading, setLoading] = useState(false);
    const handleRefresh = async () => {
      try {
        setLoading(true);
        const resPixel = await customFetch("/user/refreshinfo");
        const pixelData: IUserPixel = await resPixel.json();

        if(pixelData.githubNickname === null) {
          window.alert(NOT_PASS_ENOUGH_TIME)
        }
        dispatch(updateUserPixel(pixelData));
      } catch (err) {
        console.error("Error:", err);
      } finally {
        setLoading(false);
      }
    }

    return (
      <>
        <Tooltip title="15분이 지나야 갱신할 수 있습니다">
          {loading ? <div className="hover:cursor-not-allowed animate-spin-slow cursor-pointer flex justify-center items-center ml-2 text-[#008000]">
            <FontAwesomeIcon icon={faArrowsRotate} />
          </div> : <div className="cursor-pointer flex justify-center items-center ml-2 text-[#008000]">
            <FontAwesomeIcon icon={faArrowsRotate} onClick={handleRefresh} />
          </div>}
        </Tooltip>
      </>
    );
}

export default RefreshBtn;