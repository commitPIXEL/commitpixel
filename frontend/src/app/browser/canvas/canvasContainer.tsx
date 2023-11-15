import { useCallback, useEffect, useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Panzoom, { PanZoom } from "panzoom";
import useSocket from "@/hooks/useSocket";
import { CircularProgress } from '@mui/material';
import { apiUrl, height, width } from "../config";
import { RootState } from "@/store";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";
import { setAvailablePixel } from "@/store/slices/userSlice";
import { setSnackbarOff, setSnackbarOpen } from "@/store/slices/snackbarSlice";
import { BrowserSnackBar } from "./snackbar";
import rgbToHex from "@/app/utils/rbgUtils";
import { setPixel } from "@/app/utils/paintUtils";
import { ImyPaintPixel, IothersPaintPixel, IurlInfo } from "@/interfaces/pixel";

const CanvasContainer = () => {
  const dispatch = useDispatch();
  const { socket, setSocket, connectToSocket } = useSocket();

  const pcClass = " h-full col-span-3";
  const mobileClass = " h-full justify-center";
  const audio = new Audio('/sounds/zapsplat_foley_footstep_stamp_wood_panel_19196.mp3');

  const ref = useRef<HTMLDivElement | null>(null);
  const canvasWrapper = useRef<HTMLDivElement>(null);
  const canvasContainer = useRef<HTMLDivElement>(null);
  const canvasRef = useRef<HTMLCanvasElement | null>(null); 

  const [urlData, setUrlData] = useState<IurlInfo | null>(null);
  const [cursorPos, setCursorPos] = useState({ x: 0, y: 0 });
  const [isCanvasLoading, setIsCanvasLoading] = useState(false);
  const [ctx, setCtx] = useState<CanvasRenderingContext2D | null>();
  const [panzoomInstance, setPanzoomInstance] = useState<PanZoom | null>(null);

  const user = useSelector((state: RootState) => state.user);
  const tool = useSelector((state:RootState) => state.tool.tool);
  const color = useSelector((state:RootState) => state.color.color);
  const device = useSelector((state: RootState) => state.device.device);
  const isSnackbarOpen = useSelector((state: RootState) => state.snackbar.isOpen);

  // 픽셀 칠하기 함수 재활용
  const memoizedSetPixel = useCallback((pixel: ImyPaintPixel & {user: IurlInfo}) => {
    if(!ctx || !socket) return;
    setPixel(ctx, socket, pixel);  
  }, [ctx, socket]);

  // 웹소켓으로 다른 사람이 색칠한 픽셀 칠하는 함수 재활용
  const handlePixel = useCallback((pixel: IothersPaintPixel) => {
    if(!ctx) return;
    const [x, y, r, g, b] = pixel;
    ctx.fillStyle = `rgba(${r},${g}, ${b}, 255)`;
    ctx.fillRect(x, y, 1, 1);
  }, [ctx]);

  // 웹소켓으로 픽셀 정보 받기 함수 재활용
  const handleUrl = useCallback((urlData: IurlInfo) => {
    setUrlData(urlData);
  }, []);

  // 웹소켓으로 사용자 크레딧 수 변화 받기 함수 재활용
  const handleAvailableCredit = useCallback((availablePixel: number) => {
    dispatch(setAvailablePixel(availablePixel));
  }, [dispatch]);

  // 캔버스 컴포넌트 초기화
  const initCanvas = () => {
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
  };

  // 캔버스 위치 초기화: 캔버스를 삭제=>재생성으로 리렌더링 하는 대신 위치만 초기화하기 위해 initCanvas와 분리
  const resetCanvas = () => {
    if(!panzoomInstance) return;
    panzoomInstance.pause();
    const initialZoom = device === "mobile" ? 0.5 : 1;
    const container = canvasContainer.current;
    if (container) {
      panzoomInstance.zoomAbs(0, 0, initialZoom);
      const centerX = (container.offsetWidth / 2) - ((width * initialZoom) / 2);
      const centerY = (container.offsetHeight / 2) - ((height * initialZoom) / 2);
      panzoomInstance.moveTo(centerX, centerY);
    }
    panzoomInstance.resume();
  };

  useEffect(() => {
    console.log("socket 변경됨!");
  }, [socket]);

  // 웹소켓으로 pixel 받기
  useEffect(() => {
    if (socket && ctx) {
      socket.on("pixel", (pixel) => {
        handlePixel(pixel);
      });
      socket.on("url", (urlData) => {
        handleUrl(urlData);
      });
      socket.on("availableCredit", (availablePixel) => {
        handleAvailableCredit(availablePixel);
      });

      return () => {
        socket.off("pixel", handlePixel);
        socket.off("url", handleUrl);
        socket.off("availableCredit", handleAvailableCredit);
      };
    }
  }, [socket, ctx]);

  // 캔버스 컴포넌트가 생성되면 현재 캔버스 이미지 가져오기
  useEffect(() => {
    if(!ctx) return;
    setIsCanvasLoading(true);
    const img = new Image(width, height);
    fetch(`${apiUrl}/pixel/image/64`)
      .then((res) => res.text())
      .then((data) => {
        img.src = "data:image/png;base64," + data;
        img.crossOrigin = "Anonymouse";
        img.onload = () => {
          ctx?.drawImage(img, 0, 0);
          setIsCanvasLoading(false);
        };
      })
      .catch((err) => {
        console.log(err);
      });
  }, [ctx]);

  // 소켓이 연결되면 캔버스 초기화
  useEffect(() => {
    initCanvas();
  }, [socket]);

  // 캔버스 상호작용 함수
  useEffect(() => {
    const canvas = canvasRef.current;
    const wrapper = canvasWrapper.current;
    if (canvas && wrapper) {
      const ctx = canvas.getContext("2d");
      setCtx(ctx);

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
          if(!isSnackbarOpen) {
            socket?.emit("url", [x, y]);
            dispatch(setSnackbarOpen());
            return;
          } else {
            dispatch(setSnackbarOff());
          }
        }
        if(tool == "painting") {
          panzoomInstance?.pause();
          audio.play();
            memoizedSetPixel({
              x: x,
              y: y,
              color: color.rgb, 
              user: { githubNickname: user.githubNickname, url: user.url }
            });
        } else if(tool == "copying" && ctx) {
          panzoomInstance?.pause();
          const [r, g, b] = ctx.getImageData(x, y, 1, 1).data;
          const hex = rgbToHex(r, g, b);
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
        panzoomInstance?.resume();
      };

      const onFingerDown = (e: PointerEvent) => {
        if(e.pointerType === "mouse" || !user || !e.target) return;
        e.stopPropagation();

        const x = (Math.round(e.offsetX) - 1);
        const y = (Math.round(e.offsetY) - 1);
        if(tool === null || tool === undefined) {
          socket?.emit("url", [x, y]);
          return;
        } 
        if(tool == "painting") {
          panzoomInstance?.pause();
          audio.play();
            memoizedSetPixel({
              x: x,
              y: y,
              color: color.rgb, 
              user: { githubNickname: user.githubNickname, url: user.url }
            });
        } else if(tool == "copying" && ctx) {
          panzoomInstance?.pause();
          const [r, g, b] = ctx.getImageData(x, y, 1, 1).data;
          const hex = rgbToHex(r, g, b);
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
        if(e.pointerType === "mouse" || device !== "mobile") return;
        if (tool === null || tool === undefined) {
          dispatch(setSnackbarOpen());
        }
        panzoomInstance?.resume();
      };

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
  }, [color, setPixel, tool, panzoomInstance, rgbToHex, isSnackbarOpen]);

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
          { device === "mobile" ? null : 
          <div className="text-mainColor w-full text-center flex justify-center items-center">
            <div className="mr-8">{`( ${cursorPos.x} , ${cursorPos.y} )`}</div>
            <div onClick={() => resetCanvas()} className={`cursor-${!tool ? "pointer" : tool}`}>캔버스 원위치</div>
          </div>}
          <div
            className="overflow-hidden w-full h-full">
            <div className="w-max cursor-pointer" ref={ref}>
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
          {isCanvasLoading ? <div className="w-full h-full absolute flex justify-center items-center">
            <CircularProgress />
          </div> : null}
          { isSnackbarOpen && urlData ? <BrowserSnackBar urlData={urlData} /> : null }
        </div>
      )}
    </div>
  );
}

export default CanvasContainer;
