import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import useSocket from "@/hooks/useSocket";
import { useCallback, useEffect, useRef, useState } from "react";
import { height, width } from "../config";
import Panzoom from "panzoom";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";
import { rgbToHex } from "../utils";
import { BrowserSnackBar } from "./snackbar";

const CanvasContainer = () => {
  const dispatch = useDispatch();
  const { socket, setSocket, connectToSocket } = useSocket();
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
  const user = useSelector((state: RootState) => state.user);

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
    const img = new Image(width, height);
    fetch("https://dev.commitpixel.com/api/pixel/image/64")
      .then((res) => res.text())
      .then((data) => {
        img.src = "data:image/png;base64," + data;
        img.crossOrigin = "Anonymouse";
        img.onload = () => {
          ctx?.drawImage(img, 0, 0);
        };
      })
      .catch((err) => {
        console.log(err);
      });
  }, [ctx]);

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
        if(e.button !== 0 || !user) return;
        if(e.detail >= 2) {
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
            setPixel(x, y, { r, g, b }, user.githubNickname, user.url);
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
        if(e.type === "mouse" || !user) return;
        // e.preventDefault();
        e.stopPropagation();
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
            setPixel(x, y, { r, g, b }, user.githubNickname, user.url);
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
    <div className={device === "mobile" ? "w-full h-[48%]" : "col-span-3 w-full max-h-full"}>
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
        <div ref={canvasContainer} className={"w-full flex flex-col items-center" + (device === "mobile" ? mobileClass : pcClass)}>
          { device === "mobile" ? null : <div className="text-mainColor w-full text-center">{`( ${cursorPos.x} , ${cursorPos.y} )`}</div>}
          <div
            className="overflow-hidden w-full h-full">
            <div className="w-max" ref={ref}>
              <div style={{ padding: 0.5 }} ref={canvasWrapper} >
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