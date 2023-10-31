import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import useSocket from "@/hooks/useSocket";
import { useCallback, useEffect, useRef, useState } from "react";
import { height, imageUrl, width } from "../config";
import Panzoom from "panzoom";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";
import { rgbToHex } from "../utils";

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
  const audio = new Audio('/sounds/zapsplat_foley_footstep_stamp_wood_panel_19196.mp3');


  // 픽셀 그리기
  const setPixel = useCallback((
    x: number,
    y: number,
    color: {
      r: number,
      g: number,
      b: number,
    },
    url: string,
    userId: string
  ) => {
    if(ctx && socket) {
      ctx.fillStyle = `rgba(${color.r},${color.g}, ${color.b}, 255)`;
      ctx.fillRect(x, y, 1, 1);
      socket?.emit("pixel", [x, y, color.r, color.g, color.b, userId, url]);
    }
  }, [ctx, socket]);

  // 웹소켓으로 pixel 받기
  useEffect(() => {
    if (socket && ctx) {
      socket.on("pixel", (pixel) => {
        console.log(pixel);
        const [x, y, r, g, b] = pixel;
        ctx.fillStyle = `rgba(${r},${g}, ${b}, 255)`;
        ctx.fillRect(x, y, 1, 1);
      });
    }
  }, [socket, ctx]);

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

      panzoom.setMaxZoom(50);
      panzoom.setMinZoom(0.3);

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

      const toHex = (rgbData: number) => {
        let hex = rgbData.toString(16);
        return hex.length == 1 ? "0" + hex : hex;
      };
    
      const rgbtoHex = (r: number, g: number, b: number) => {
        return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
      };

      const setCursor = (e: MouseEvent) => {
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        setCursorPos({ x, y });
      };

      const canvasClick = (e: MouseEvent) => {
        e.preventDefault();
        panzoomInstance.pause();
        setTimeout(panzoomInstance.resume(), 200);
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        let r, g, b;
        if(tool === null) return;
        switch (tool) {
          case "panning":
            break;
          case "painting":
            if(!panzoomInstance.isPaused) {
              console.log("Panzoom is not paused");
              break;
            }
            audio.play();
            panzoomInstance.pause();
            r = color.rgb.r;
            g = color.rgb.g;
            b = color.rgb.b;
            setPixel(x, y, { r, g, b }, "githubNick", "https://www.naver.com/");
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

      wrapper.addEventListener("mousemove", setCursor);
      wrapper.addEventListener("mousedown", canvasClick);
      wrapper.addEventListener("mouseup", onMouseUp);

      return () => {
        wrapper.removeEventListener("contextmenu", setCursor);
        wrapper.removeEventListener("mousedown", canvasClick);
        wrapper.removeEventListener("mouseup", onMouseUp);
      };
    }
  }, [color, setPixel, tool, panzoomInstance, rgbToHex]);

  useEffect(() => {
    if ((tool == "panning" || tool === null || tool === undefined) && panzoomInstance?.isPaused()){
      panzoomInstance.resume();
    }
  }, [tool, panzoomInstance]);

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
              console.log(socket);
            }}
          >
            Reconnect
          </button>
        </div>
      )}
      {socket && (
        <div className="w-full h-full flex flex-col col-span-3">
          <div className="text-mainColor w-full text-center">{`( ${cursorPos.x} , ${cursorPos.y} )`}</div>
          <div
            className="overflow-hidden bg-bgColor"
            style={{
              maxWidth: "100vw",
              maxHeight: "85vh",
            }}
          >
            <div className="w-max" ref={ref}>
              <div
                className="bg-slate-200"
                style={{ padding: 0.5 }}
                ref={canvasWrapper}
              >
                <canvas
                  id="canvas"
                  width={width}
                  height={height}
                  className="canvas"
                  ref={canvasRef}
                >
                  캔버스를 지원하지 않는 브라우저입니다. 크롬으로 접속해주세요!
                </canvas>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CanvasContainer;
