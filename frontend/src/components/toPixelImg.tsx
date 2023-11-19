import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Rnd } from "react-rnd";
import { IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import LockIcon from "@mui/icons-material/Lock";
import LockOpenIcon from "@mui/icons-material/LockOpen";
import { RootState } from "@/store";
import { move, reset } from "@/store/slices/imgToPixelSlice";

const ToPixelImg = () => {
  const dispatch = useDispatch();
  const imgSrc = useSelector((state: RootState) => state.imgToPixel.imageSrc);
  const imgPosition = useSelector(
    (state: RootState) => state.imgToPixel.position
  );
  const [size, setSize] = useState({ width: 100, height: 100 }); //이미지 픽셀화 초기 크기
  const [isLocked, setIsLocked] = useState(false); // 고정 상태 관리

  const handleClose = () => {
    dispatch(reset());
    setIsLocked(false); // 고정 상태 해제
  };

  const toggleLock = () => {
    setIsLocked(!isLocked); // 고정 상태 토글
  };

  return (
    <>
      {imgSrc && (
        <Rnd
          size={size}
          position={imgPosition}
          onDragStop={(_e: any, d: any) => {
            if (!isLocked) {
              dispatch(move({ x: d.x, y: d.y }));
            }
          }}
          onResizeStop={(
            _e: any,
            _direction: any,
            ref: any,
            _delta: any,
            position: any
          ) => {
            if (!isLocked) {
              setSize({
                width: ref.offsetWidth,
                height: ref.offsetHeight,
              });
              dispatch(move(position));
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
          <div
            style={{
              position: "relative",
              height: "25px",
              marginBottom: "7px",
            }}
          >
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
            src={imgSrc}
            alt="Pixeled Image"
            style={{ width: "100%", height: "70%", opacity: "0.5" }}
          />
        </Rnd>
      )}
    </>
  );
};

export default ToPixelImg;
