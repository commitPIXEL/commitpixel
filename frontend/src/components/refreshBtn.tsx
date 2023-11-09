"use client";

import { Button, Tooltip } from "@mui/material";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import { IUserPixel } from "@/interfaces/browser";
import { useDispatch } from "react-redux";
import { updateUserPixel } from "@/store/slices/userSlice";
import { faArrowsRotate } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";

const RefreshBtn = () => {

    const dispatch = useDispatch();
    const customFetch = useFetchWithAuth();
    const [loading, setLoading] = useState(false);
    const handleRefresh = async () => {
      try {
        setLoading(true);
        const resPixel = await customFetch("/user/refreshinfo");
        const pixelData: IUserPixel = await resPixel.json();

        if(pixelData.githubNickname === null) {
          window.alert("로그인 혹은 마지막 갱신 이후 15분이 지나지 않았습니다.")
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
          {loading ? <div className="cursor-pointer hover:cursor-not-allowed animate-spin-slow cursor-pointer flex justify-center items-center ml-2 text-[#008000]">
            <FontAwesomeIcon icon={faArrowsRotate} />
          </div> : <div className="cursor-pointer flex justify-center items-center ml-2 text-[#008000]">
            <FontAwesomeIcon icon={faArrowsRotate} onClick={handleRefresh} />
          </div>}
        </Tooltip>
      </>
    );
}

export default RefreshBtn;