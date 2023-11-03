import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import useSocket from "@/hooks/useSocket";
import { useCallback, useEffect, useRef, useState } from "react";
import { height, imageUrl, width } from "../config";
import Panzoom from "panzoom";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";
import { rgbToHex } from "../utils";
import useColorTool from "@/hooks/useColorTool";
import { BrowserSnackBar } from "./snackbar";

const CanvasContainer = () => {
  const dispatch = useDispatch();
  const { socket, setSocket, connectToSocket } = useSocket();
  const canvasRef = useRef<HTMLCanvasElement | null>(null); 
  const ref = useRef<HTMLDivElement | null>(null);
  const canvasWrapper = useRef<HTMLDivElement>(null);
  const canvasContainer = useRef<HTMLDivElement>(null);

  const [panzoomInstance, setPanzoomInstance] = useState<any | null>(null);
  const [ctx, setCtx] = useState<CanvasRenderingContext2D | null>();
  const [cursorPos, setCursorPos] = useState({ x: 0, y: 0 });
  const [urlData, setUrlData] = useState<any>(null);
  const [open, setOpen] = useState(false);
  const color = useSelector((state:RootState) => state.color.color);
  const tool = useSelector((state:RootState) => state.tool.tool);
  const device = useSelector((state: RootState) => state.device.device);

  const pcClass = " h-full col-span-3";
  const mobileClass = " h-full justify-center";


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

  const useTool = useColorTool(setPixel, socket, ctx, panzoomInstance);

  // 웹소켓으로 pixel 받기
  useEffect(() => {
    if (socket && ctx) {
      socket.on("pixel", (pixel) => {
        console.log(pixel);
        const [x, y, r, g, b] = pixel;
        ctx.fillStyle = `rgba(${r},${g}, ${b}, 255)`;
        ctx.fillRect(x, y, 1, 1);
      });
      socket.on("url", (urlData) => {
        setUrlData(urlData);
        console.log(urlData);
      });
    }
  }, [socket, ctx]);

  useEffect(() => {
    if(imageUrl){
      const img = new Image(width, height);
      const imgData = fetch("https://dev.commitpixel.com/api/pixel/image/64").then((res) => {
        console.log(res);
        /* 
        img.src = "data:image/png;base64," + res;
        img.crossOrigin = "Anonymouse";
        img.onload = () => {
          ctx?.drawImage(img, 0, 0);
        };
        */
      }).then((err) => {
        console.log(err);
      });
    }
  }, [ctx, socket]);

  useEffect(() => {
    const div = ref.current;
    const initialZoom = device === "mobile" ? 0.5 : 1;
    const container = canvasContainer.current;
    if (div && container) {
      const panzoom = Panzoom(div, {
        zoomDoubleClickSpeed: 1,
        initialZoom: initialZoom,
      });
      const centerX = (container.offsetWidth / 2) - ((width * initialZoom) / 2);
      const centerY = (container.offsetHeight / 2) - ((height * initialZoom) / 2)
      panzoom.moveTo(centerX, centerY);

      panzoom.setMaxZoom(50);
      panzoom.setMinZoom(0.5);

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

      const setCursor = (e: MouseEvent) => {
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        setCursorPos({ x, y });
      };

      const onMouseUp = (e: MouseEvent) => {
        if (tool === null && e.button !== 2) {
          setOpen(true);
        } 
        panzoomInstance.resume();
      };

      const onMouseDown = (e: MouseEvent) => {
        if(e.button !== 0) return;
        if(e.detail == 2) {
          e.preventDefault();
        }
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        useTool(x, y);
      };
    
      const onFingerDown = (e: TouchEvent) => {
        if(device !== "mobile" || e.touches.length !== 1) {
          return;
        }
        const x = e.touches[0].clientX - 1;
        const y = e.touches[0].clientY - 1;
        useTool(x, y);
      };

      const onFingerUp = (e: TouchEvent) => {
        if(device !== "mobile" || e.touches.length !== 1) {
          return;
        }
        if (tool === null || tool === undefined) {
          setOpen(true);
        }
        panzoomInstance.resume();
      }

      wrapper.addEventListener("touchstart", onFingerDown);
      wrapper.addEventListener("touchend", onFingerUp);
      wrapper.addEventListener("mousemove", setCursor);
      wrapper.addEventListener("mousedown", onMouseDown);
      wrapper.addEventListener("mouseup", onMouseUp);

      return () => {
        wrapper.removeEventListener("touchstart", onFingerDown);
        wrapper.removeEventListener("touchend", onFingerUp);
        wrapper.removeEventListener("mousemove", setCursor);
        wrapper.removeEventListener("mousedown", onMouseDown);
        wrapper.removeEventListener("mouseup", onMouseUp);
      };
    }
  }, [color, setPixel, tool, panzoomInstance, rgbToHex]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div className={device === "mobile" ? "w-full h-[48%]" : "col-span-3 w-full max-h-full"}>
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
        <div ref={canvasContainer} className={"w-full flex flex-col items-center" + (device === "mobile" ? mobileClass : pcClass)}>
          { device === "mobile" ? null : <div className="text-mainColor w-full text-center">{`( ${cursorPos.x} , ${cursorPos.y} )`}</div>}
          <div
            className="overflow-hidden w-full h-full">
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
          <BrowserSnackBar open={open} handleClose={handleClose} urlData={urlData} />
        </div>
      )}
    </div>
  );
}

export default CanvasContainer;