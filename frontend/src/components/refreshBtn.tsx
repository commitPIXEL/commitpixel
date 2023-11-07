"use client";

import { Button } from "@mui/material";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import { IUserPixel } from "@/interfaces/browser";
import { useDispatch } from "react-redux";
import { updateUserPixel } from "@/store/slices/userSlice";
import { faArrowsRotate } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const RefreshBtn = () => {

    const dispatch = useDispatch();
    const customFetch = useFetchWithAuth();
    const handleRefresh = async () => {
      try {
        const resPixel = await customFetch("/user/refreshinfo");
        const pixelData: IUserPixel = await resPixel.json();

        if (pixelData.githubNickname as null) {
          window.alert("마지막 갱신 이후 15분이 지나지 않았습니다!");
        }
        dispatch(updateUserPixel(pixelData));
      } catch (err) {
        console.error("Error:", err);
      }
    }

    return (
      <div className="cursor-pointer flex justify-center items-center ml-2 text-[#008000]">
          <FontAwesomeIcon icon={faArrowsRotate} onClick={handleRefresh} />
      </div>
    );
}

export default RefreshBtn;