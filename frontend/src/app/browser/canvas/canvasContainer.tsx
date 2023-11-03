import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import useSocket from "@/hooks/useSocket";
import { useCallback, useEffect, useRef, useState } from "react";
import { height, imageUrl, width } from "../config";
import Panzoom from "panzoom";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";
import { rgbToHex } from "../utils";
import { BrowserSnackBar } from "./snackbar";

const CanvasContainer = () => {
  const dispatch = useDispatch();
  const { socket } = useSocket();
  const canvasRef = useRef<HTMLCanvasElement | null>(null); 
  const ref = useRef<HTMLDivElement | null>(null);
  const canvasWrapper = useRef<HTMLDivElement>(null);

  const [panzoomInstance, setPanzoomInstance] = useState<any | null>(null);
  const [ctx, setCtx] = useState<CanvasRenderingContext2D | null>();
  const [cursorPos, setCursorPos] = useState({ x: 0, y: 0 });
  const [urlData, setUrlData] = useState<any>(null);
  const [open, setOpen] = useState(false);
  const color = useSelector((state:RootState) => state.color.color);
  const tool = useSelector((state:RootState) => state.tool.tool);
  const device = useSelector((state: RootState) => state.device.device);
  const audio = new Audio('/sounds/zapsplat_foley_footstep_stamp_wood_panel_19196.mp3');
  const canvasContainer = useRef<HTMLDivElement>(null);

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
    const initialZoom = device === "mobile" ? 0.6 : 1;
    const dividerWidth = device === "mobile" ? 3 : 3.5;
    const dividerHeight = device === "mobile" ? 8 : 3.5;
    if (div) {
      const panzoom = Panzoom(div, {
        zoomDoubleClickSpeed: 1,
        initialZoom: initialZoom,
      });
      panzoom.moveTo(
        window.innerWidth / dividerWidth - (width / dividerWidth) * initialZoom,
        window.innerHeight / dividerHeight - (height / dividerHeight) * initialZoom,
      );

      panzoom.setMaxZoom(50);
      panzoom.setMinZoom(0.8);

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

      const setFinger = (e: PointerEvent) => {
        if(e.type === "mouse") return;
        const [x, y] = [parseInt(String(e.offsetX)), parseInt(String(e.offsetY))];
        setCursorPos({ x, y });
      };

      const setCursor = (e: MouseEvent) => {
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        setCursorPos({ x, y });
      };

      const canvasClick = (e: MouseEvent) => {
        if(e.button !== 0) return;
        if(e.detail == 2) {
          e.preventDefault();
        }
        const [x, y] = [e.offsetX - 1, e.offsetY - 1];
        if(tool === null || tool === undefined) {
          socket?.emit("url", [x, y]);
          return;
        } 
        let r, g, b;
        if(tool == "painting") {
          panzoomInstance?.pause();
          audio.play();
            r = color.rgb.r;
            g = color.rgb.g;
            b = color.rgb.b;
            setPixel(x, y, { r, g, b }, "githubNick", "https://www.naver.com/");
        } else if(tool == "copying" && ctx) {
          panzoomInstance?.pause();
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
      };

      const onMouseUp = (e: MouseEvent) => {
        if (tool === null && e.button !== 2) {
          setOpen(true);
        } 
        panzoomInstance.resume();
      };

      const onFingerDown = (e: PointerEvent) => {
        e.preventDefault();
        e.stopPropagation();
        if(e.type === "mouse") return;
        if(device !== "mobile" || !e.target) {
          return;
        }

        const x = (Math.round(e.offsetX) - 1);
        const y = (Math.round(e.offsetY) - 1);
        if(tool === null || tool === undefined) {
          socket?.emit("url", [x, y]);
          return;
        } 
        let r, g, b;
        if(tool == "painting") {
          panzoomInstance?.pause();
          audio.play();
            r = color.rgb.r;
            g = color.rgb.g;
            b = color.rgb.b;
            setPixel(x, y, { r, g, b }, "githubNick", "https://www.naver.com/");
        } else if(tool == "copying" && ctx) {
          panzoomInstance?.pause();
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
      };

      const onFingerUp = (e: PointerEvent) => {
        if(e.type === "mouse") return;
        if(device !== "mobile") {
          return;
        }
        if (tool === null || tool === undefined) {
          setOpen(true);
        }
        panzoomInstance.resume();
      }

      wrapper.addEventListener("pointerdown", onFingerDown);
      wrapper.addEventListener("pointerup", onFingerUp);
      wrapper.addEventListener("pointermove", setFinger);
      wrapper.addEventListener("mousemove", setCursor);
      wrapper.addEventListener("mousedown", canvasClick);
      wrapper.addEventListener("mouseup", onMouseUp);

      return () => {
        wrapper.removeEventListener("pointerdown", onFingerDown);
        wrapper.removeEventListener("pointerup", onFingerUp);
        wrapper.removeEventListener("pointermove", setFinger);
        wrapper.removeEventListener("mousemove", setCursor);
        wrapper.removeEventListener("mousedown", canvasClick);
        wrapper.removeEventListener("mouseup", onMouseUp);
      };
    }
  }, [color, setPixel, tool, panzoomInstance, rgbToHex]);

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div className={device === "mobile" ? "w-full h-[50%]" : "col-span-3 w-full max-h-full"}>
      {!socket && (
        <div className="flex flex-col items-center justify-center gap-2">
          <span>Not connected</span>
        </div>
      )}
      {socket && (
        <div className={"w-full flex flex-col items-center" + (device === "mobile" ? mobileClass : pcClass)}>
          { device === "mobile" ? null : <div className="text-mainColor w-full text-center">{`( ${cursorPos.x} , ${cursorPos.y} )`}</div>}
          <div
            className="overflow-hidden w-[95%] h-full">
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
                <div
                  style={{
                    position: "absolute",
                    top: cursorPos.y + 1,
                    left: cursorPos.x + 1.25,
                    width: 0,
                    height: 0,
                  }}
                ></div>
              </div>
            </div>
          </div>
          <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
            <div className="bg-white rounded p-4">
              <div className="cursor-pointer">{urlData?.userId}</div> 
              <div className="cursor-pointer" onClick={() => {window.open("https://www.naver.com/", "_blank")}}>{urlData?.url}</div> 
            </div>
          </Snackbar>
        </div>
      )}
    </div>
  );
}

export default CanvasContainer;

