"use client";
import { useDispatch, useSelector } from "react-redux";
import { useCallback, useRef, useState } from "react";
import BlurOnIcon from "@mui/icons-material/BlurOn";
import CircularProgress from "@mui/material/CircularProgress";
import axios from "axios";
import { browserInput, mobileInput } from "@/store/slices/imgToPixelSlice";

const ToPixelBtn = () => {
  const dispatch = useDispatch();
  const [isLoading, setIsLoading] = useState(false);
  const inputRef = useRef<HTMLInputElement | null>(null);

  const handleUploadImage = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      if (!e.target.files) {
        return;
      }
      setIsLoading(true);
      let formData = new FormData();
      formData.append("file", e?.target.files[0]);

      axios
        .post(
          `${process.env.NEXT_PUBLIC_API_URL}/image/convert?type=1`,
          formData,
          {
            responseType: "blob",
            headers: {
              "Content-Type": "multipart/form-data",
            },
          }
        )
        .then((res) => {
          const blob = new Blob([res.data], { type: "image/jpeg" });
          const imageURL = window.URL.createObjectURL(blob);
          if (isMobileView) {
            dispatch(mobileInput(imageURL));
          } else {
            dispatch(browserInput(imageURL));
          }
          setIsLoading(false);
        })
        .catch((error) => {
          console.error(error);
          alert("이미지 픽셀화 실패!");
          setIsLoading(false);
        })
        .finally(() => {
          if (e.target.files) {
            e.target.value = ""; // 입력 필드 초기화
          }
        });
    },
    []
  );

  const isMobileView =
    window.innerWidth < 769 && window.innerWidth <= window.innerHeight;

  return (
    <>
      {isMobileView ? (
        <label htmlFor="image_to_pixel" className="cursor-pointer">
          <BlurOnIcon />
        </label>
      ) : (
        <label
          htmlFor="image_to_pixel"
          className="cursor-pointer drop-shadow-md w-[45%] bg-mainColor rounded min-h-[40px] flex justify-center items-center"
        >
          {isLoading ? <CircularProgress className="p-2" /> : "이미지 픽셀화"}
        </label>
      )}
      <input
        className="hidden"
        ref={inputRef}
        onChange={handleUploadImage}
        id="image_to_pixel"
        type="file"
        accept="image/*"
      />
    </>
  );
};

export default ToPixelBtn;
