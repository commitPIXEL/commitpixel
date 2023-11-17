import React, { useCallback, useRef, useState } from "react";
import axios from "axios";
import { Rnd } from "react-rnd";
import { apiUrl } from "../config";
import CircularProgress from "@mui/material/CircularProgress";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import LockIcon from "@mui/icons-material/Lock";
import LockOpenIcon from "@mui/icons-material/LockOpen";

const ImageToPixelModal = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [imageSrc, setImageSrc] = useState("");
  const inputRef = useRef<HTMLInputElement | null>(null);
  const [size, setSize] = useState({ width: 100, height: 100 }); //이미지 픽셀화 초기 크기
  const [position, setPosition] = useState({ x: -500, y: 0 }); //이미지 픽셀화 나타날 위치
  const [isLocked, setIsLocked] = useState(false); // 고정 상태 관리

  const handleUploadImage = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) {
      return;
    }
    setIsLoading(true);
    let formData = new FormData();
    formData.append("file", e?.target.files[0]);

    axios
      .post(`${apiUrl}/image/convert?type=1`, formData, {
        responseType: "blob",
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((res) => {
        const blob = new Blob([res.data], { type: "image/jpeg" });
        const imageURL = window.URL.createObjectURL(blob);
        setImageSrc(imageURL);
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
  }, []);

  const handleClose = () => {
    setImageSrc(""); // 이미지 주소 초기화
    setIsLocked(false); // 고정 상태 해제
  };

  const toggleLock = () => {
    setIsLocked(!isLocked); // 고정 상태 토글
  };

  return (
    <>
      <label
        htmlFor="image_to_pixel"
        className="cursor-pointer drop-shadow-md w-[45%] bg-mainColor rounded min-h-[40px] flex justify-center items-center"
      >
        {isLoading ? <CircularProgress className="p-2" /> : "이미지 픽셀화"}
      </label>
      <input
        className="hidden"
        ref={inputRef}
        onChange={handleUploadImage}
        id="image_to_pixel"
        type="file"
        accept="image/*"
      />
      {imageSrc && !isLoading && (
        <Rnd
          size={size}
          position={position}
          onDragStop={(_e: any, d:any) => {
            if (!isLocked) {
              setPosition({ x: d.x, y: d.y });
            }
          }}
          onResizeStop={(_e: any, _direction: any, ref: any, _delta: any, position: any) => {
            if (!isLocked) {
              setSize({
                width: ref.offsetWidth,
                height: ref.offsetHeight,
              });
              setPosition(position);
            }
          }}
          enableResizing={!isLocked}
          disableDragging={isLocked}
          style={{
            border: "1px solid #333", //진한회색
            overflow: "hidden",
            pointerEvents: isLocked ? "none" : "auto",
          }}
        >
          <div style={{ position: "relative", height: "25px", marginBottom:"7px" }}>
            <IconButton
              onClick={handleClose}
              style={{
                position: "absolute",
                top: 0,
                right: 0,
                zIndex: 1001,
              }}
            >
              <CloseIcon />
            </IconButton>
            <IconButton
              onClick={toggleLock}
              style={{
                position: "absolute",
                top: 0,
                left: 0,
                zIndex: 1001,
                pointerEvents: "auto",
              }}
            >
              {isLocked ? <LockIcon /> : <LockOpenIcon />}
            </IconButton>
          </div>
            <img
              src={imageSrc}
              alt="Pixeled Image"
              style={{ width: "100%", height: "70%", opacity: "0.5" }}
            />
        </Rnd>
      )}
    </>
  );
};

export default ImageToPixelModal;