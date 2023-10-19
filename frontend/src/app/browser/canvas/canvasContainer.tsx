"use client";

import { useSelector } from "react-redux";

import {
  useEffect,
  useRef,
  useState
} from "react";
import { RootState } from "@/store";

type CanvasProps = {
  canvasWidth: number;
  canvasHeight: number;
};

export default function Canvas(props: CanvasProps) {
  const color = useSelector((state:RootState) => state.color.color);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [parentWidth, setParentWidth] = useState<number>(0);
  const [parentHeight, setParentHeight] = useState<number>(0);
  const [zoomLevel, setZoomLevel] = useState<number>(1);

  useEffect(() => {
    const parent = canvasRef.current?.parentElement;
    if(parent !== undefined && parent !== null){
      setParentWidth(parent.offsetWidth);
      setParentHeight(parent.offsetHeight);
    }
  }, []);

  const handleCanvasClick = (e: any) => {
    console.log("canvas clicked");
    const canvas = canvasRef.current;
    if (canvas !== null) {
      const context = canvas.getContext("2d");
      const rect = canvas.getBoundingClientRect();

      // 화면상의 canvas 크기와 실제 canvas 픽셀 크기의 비율을 계산
      const scaleX = canvas.width / rect.width;
      const scaleY = canvas.height / rect.height;

      // 비율을 사용하여 실제 canvas 픽셀에 대한 마우스 위치를 계산
      const x = ((e.clientX - rect.left) * scaleX) / zoomLevel;
      const y = ((e.clientY - rect.top) * scaleY) / zoomLevel;

      console.log("x:" + x + " y:" + y);
      if (context !== null) {
        console.log(color);
        context.fillStyle = color;
        let pixelSize = 1;
        if(zoomLevel > 1) {
          pixelSize = zoomLevel * 5;
        }
        // 여기에서 픽셀의 크기를 줌 레벨에 따라 조정합니다.
        context.fillRect(x, y, pixelSize * scaleX * zoomLevel, pixelSize * scaleY * zoomLevel);
      }
    }
  };

  useEffect(() => {
    const handleWheel = (e: WheelEvent) => {
      e.preventDefault();
      if(e.deltaY < 0) {
        setZoomLevel(prev => Math.min(prev + 0.1, 3));
      } else {
        setZoomLevel(prev => Math.max(prev - 0.1, 1));
      }
    };
  
    const canvas = canvasRef.current;
    if (canvas) {
      canvas.addEventListener("wheel", handleWheel);
    }
  
    return () => {
      if (canvas) {
        canvas.removeEventListener("wheel", handleWheel);
      }
    };
  }, []);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (canvas && canvas.getContext) {
      const ctx = canvas.getContext("2d");
      if (ctx) {
        ctx.save();
        ctx.clearRect(0, 0, canvas.width, canvas.height); // 캔버스 지우기
        ctx.scale(zoomLevel, zoomLevel); // 줌 레벨에 따라 확대/축소
        // TODO: 여기에 그림을 그리는 코드를 추가하십시오.
        ctx.restore();
      }
    }
  }, [zoomLevel]);

  return (
    <div className="col-span-3 h-[90%] aspect-square bg-white flex flex-col justify-center items-center">
      <canvas
        onClick={handleCanvasClick}
        ref={canvasRef}
        width={props.canvasWidth}
        height={props.canvasHeight}
        style={{
          width: `${parentWidth}px`,
          height: `${parentHeight}px`
        }}
      ></canvas>
    </div>
  );
}
