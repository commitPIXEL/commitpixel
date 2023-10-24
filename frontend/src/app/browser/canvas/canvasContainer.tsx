"use client";

import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import useSocket from "@/hooks/useSocket";
import { useCallback, useEffect, useRef, useState } from "react";
import { height, imageUrl, width } from "../config";
import Panzoom from "panzoom";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";

const CanvasContainer = () => {
  const dispatch = useDispatch();
  const { socket, setSocket, connectToSocket } = useSocket();
  const canvasRef = useRef<HTMLCanvasElement | null>(null); 
  const ref = useRef<HTMLDivElement | null>(null);
  const canvasWrapper = useRef<HTMLDivElement>(null);

  const [panzoomInstance, setPanzoomInstance] = useState<any | null>(null);
  const [ctx, setCtx] = useState<CanvasRenderingContext2D | null>();
  const [cursorPos, setCursorPos] = useState({ x: 0, y: 0 });
  const color = useSelector((state:RootState) => state.color.color);
  const tool = useSelector((state:RootState) => state.tool.tool);

  // 픽셀 그리기
  const setPixel = useCallback((
    x: number,
    y: number,
    color: {
      r: number,
      g: number,
      b: number,
    }, emit = false
  ) => {
    if(ctx && socket) {
      ctx.fillStyle = `rgba(${color.r},${color.g}, ${color.b}, 255)`;
      ctx.fillRect(x, y, 1, 1);
      if (emit) {
        socket?.emit("pixel", [x, y, color.r, color.g, color.b]);
      }
    }
  }, [ctx, socket]);

  useEffect(() => {
    const handleMouseDown = (e: MouseEvent) => {
      if (e.button === 1) {
        e.preventDefault();
        return false;
      }
    };
  
    document.body.addEventListener('mousedown', handleMouseDown);
  
    // 컴포넌트가 언마운트될 때 이벤트 리스너를 제거합니다.
    return () => {
      document.body.removeEventListener('mousedown', handleMouseDown);
    };
  }, []);

  // 웹소켓으로 pixel 받기
  useEffect(() => {
    if (socket) {
      socket.on("pixel", (pixel) => {
        const [x, y, r, g, b] = pixel;
        setPixel(x, y, { r, g, b });
      });
    }
  }, [socket, setPixel]);

  useEffect(() => {
    if(imageUrl){
      const img = new Image(width, height);
      img.src = imageUrl;
      img.crossOrigin = "Anonymous";
      img.onload = () => {
        ctx?.drawImage(img, 0, 0);
      };
    }
  }, [ctx, socket]);

  useEffect(() => {
    const div = ref.current;
    const initialZoom = 0.5;
    if (div) {
      const panzoom = Panzoom(div, {
        zoomDoubleClickSpeed: 1,
        initialZoom: initialZoom,
      });
      panzoom.moveTo(
        window.innerWidth / 4 - (width / 4) * initialZoom,
        window.innerHeight / 4 - (height / 4) * initialZoom,
      );

      setPanzoomInstance(panzoom);

      return () => {
        panzoom.dispose();
      };
    }
  }, [socket]);

  useEffect(() => {
    const canvas = canvasRef.current;
    const wrapper = canvasWrapper.current;
    if (canvas && wrapper) {
      const ctx = canvas.getContext("2d");
      setCtx(ctx);

      // 마우스 클릭으로 픽셀 색칠하기
      const putPixel = (e: MouseEvent) => {
        e.preventDefault();
        // 우클릭 + Ctrl 키 = 색깔 복사
        if ( e.ctrlKey && ctx) {
          const [x, y] = [e.offsetX - 1, e.offsetY - 1];
          const [r, g, b] = ctx.getImageData(x, y, 1, 1).data;
          const hex = rgbtoHex(r, g, b);
            dispatch(pick({
              hex: hex,
              rgb: { r: r, g: g, b: b, a: 1 }
            }));
        // 우클릭 = 픽셀 색칠
        } else {
          const [x, y] = [e.offsetX - 1, e.offsetY - 1];
          const r = color.rgb.r;
          const g = color.rgb.g;
          const b = color.rgb.b;
          setPixel(x, y, { r, g, b }, true);
        }
      }

      // 마우스 클릭으로 색깔 복사하기
      const copyColor = (e: MouseEvent) => {
        e.preventDefault();
        if (ctx) {
          if (e.button === 1) {
            const [x, y] = [e.offsetX - 1, e.offsetY - 1];
            const [r, g, b] = ctx.getImageData(x, y, 1, 1).data;
            const hex = rgbtoHex(r, g, b);
            dispatch(pick({
              hex: hex,
              rgb: { r: r, g: g, b: b, a: 1 }
            }));
          }
        }
      };

      const setCursor = (e: MouseEvent) => {
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        setCursorPos({ x, y });
      };

      const canvasClick = (e: MouseEvent) => {
        e.preventDefault();
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        let r, g, b;
        if(tool === null) return;
        switch (tool) {
          case "panning":
            break;
          case "painting":
            panzoomInstance.pause();
            r = color.rgb.r;
            g = color.rgb.g;
            b = color.rgb.b;
            setPixel(x, y, { r, g, b }, true);
            break;
          case "copying":
            if (ctx) {
              panzoomInstance.pause();
              const [r, g, b] = ctx.getImageData(x, y, 1, 1).data;
              const hex = rgbtoHex(r, g, b);
              dispatch(
                pick({
                  hex: hex,
                  rgb: { r: r, g: g, b: b, a: 1 },
                })
              );
              dispatch(setTool("painting"));
            }
            break;
          default:
            break;
        }
      };

      const onMouseUp = (e: MouseEvent) => {
        if(tool === "painting" || tool === "copying") {
          panzoomInstance.resume();
        }
      };


      wrapper.addEventListener("contextmenu", putPixel);
      wrapper.addEventListener("auxclick", copyColor);
      wrapper.addEventListener("mousemove", setCursor);
      wrapper.addEventListener("mousedown", canvasClick);
      wrapper.addEventListener("mouseup", onMouseUp);

      return () => {
        wrapper.removeEventListener("contextmenu", putPixel);
        wrapper.removeEventListener("auxclick", copyColor);
        wrapper.removeEventListener("contextmenu", setCursor);
        wrapper.removeEventListener("mousedown", canvasClick);
        wrapper.removeEventListener("mouseup", onMouseUp);
      };
    }
  }, [color, setPixel, tool, panzoomInstance]);

  useEffect(() => {
    if ((tool == "panning" || tool === null || tool === undefined) && panzoomInstance?.isPaused()){
      panzoomInstance.resume();
    }
  }, [tool]);

  const toHex = (rgbData: number) => {
    let hex = rgbData.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
  }

  const rgbtoHex = (r: number, g: number, b: number) => {
    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
  };

  return (
    <div className="col-span-3 max-h-full">
      {!socket && (
        <div className="flex flex-col items-center justify-center gap-2">
          <span>Not connected</span>
          <button
            className="px-8 py-2 border"
            onClick={() => {
              const socket = connectToSocket();
              setSocket(socket);
            }}
          >
            Reconnect
          </button>
        </div>
      )}
      {socket && (
        <div className="w-full h-full flex flex-col col-span-3">
          <div className="text-mainColor w-full text-center">{`( ${cursorPos.x} , ${cursorPos.y} )`}</div>
          <div className="overflow-hidden bg-bgColor" style={{
    maxWidth: '100vw',
    maxHeight: '85vh'
  }} >
            <div className="w-max" ref={ref}>
              <div className="bg-gray-300" style={{ padding: 0.5 }} ref={canvasWrapper}>
                <canvas
                  id="canvas"
                  width={width}
                  height={height}
                  className="canvas"
                  ref={canvasRef}
                >
                  Canvas not supported on your browser.
                </canvas>
              </div>
            </div>
          </div>
          <div className="fixed bottom-0 left-0 right-0 z-50 flex items-center justify-between gap-4 px-4 py-2 bg-gray-200 bg-opacity-50 backdrop-blur-sm ">
            <div className="flex items-center gap-4">
              <span className="text-lg font-medium">Controls</span>
              <div className="flex items-center gap-2">
                <svg
                  width="23"
                  height="42"
                  viewBox="0 0 23 42"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M11.4999 41C8.00642 41 1.01944 38.5238 1.01944 28.6191C1.01944 16.2381 0.0666756 10.5238 11.4999 10.5238"
                    stroke="black"
                  />
                  <path
                    d="M21.9805 22.9048C17.9293 24.3124 14.7539 24.9972 11.5 24.9685M1.01953 22.9048C5.15525 24.2501 8.36405 24.9407 11.5 24.9685M11.5 10.5238V24.9685"
                    stroke="black"
                  />
                  <path
                    d="M11.5001 10.5238C10.5473 5.7619 18.1694 6.71429 11.5001 1"
                    stroke="black"
                    strokeLinecap="round"
                  />
                  <path
                    d="M11.5001 41C14.9936 41 21.9806 38.5238 21.9806 28.6191C21.9806 16.2381 22.9333 10.5238 11.5001 10.5238"
                    stroke="black"
                  />
                  <rect
                    x="9"
                    y="14"
                    width="5"
                    height="8"
                    rx="2.5"
                    fill="black"
                  />
                </svg>
                Copy color
              </div>
              <div className="flex items-center gap-2">
                <svg
                  width="23"
                  height="42"
                  viewBox="0 0 23 42"
                  fill="none"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    d="M11.4999 41C8.00642 41 1.01944 38.5238 1.01944 28.6191C1.01944 16.2381 0.0666756 10.5238 11.4999 10.5238"
                    stroke="black"
                  />
                  <path
                    d="M21.9805 22.9048C17.9293 24.3124 14.7539 24.9972 11.5 24.9685M1.01953 22.9048C5.15525 24.2501 8.36405 24.9407 11.5 24.9685M11.5 10.5238V24.9685"
                    stroke="black"
                  />
                  <path
                    d="M11.5001 10.5238C10.5473 5.7619 18.1694 6.71429 11.5001 1"
                    stroke="black"
                    strokeLinecap="round"
                  />
                  <path
                    d="M11.5001 41C14.9936 41 21.9806 38.5238 21.9806 28.6191C21.9806 16.2381 22.9333 10.5238 11.5001 10.5238"
                    stroke="black"
                  />
                  <path
                    d="M13.4055 21.9524V12.4286C20.0749 14.3333 19.1221 20.0476 19.1221 21L13.4055 21.9524Z"
                    fill="black"
                  />
                </svg>
                Place pixel
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CanvasContainer;