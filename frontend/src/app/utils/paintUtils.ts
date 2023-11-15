import { ImyPaintPixel, IurlInfo } from "@/interfaces/pixel";
import { Socket } from "socket.io-client";

const handleIsPixelSuccess = (ctx:CanvasRenderingContext2D, r: number, g: number, b: number, x: number, y: number) => {
  ctx.fillStyle = `rgba(${r},${g}, ${b}, 255)`;
  ctx.fillRect(x, y, 1, 1);
};

export const setPixel = (
  ctx: CanvasRenderingContext2D,
  socket: Socket,
  pixel: ImyPaintPixel & {user: IurlInfo}
  ) => {
  const {x, y, color, user} = pixel;
  if(ctx && socket) {
    socket?.emit("pixel", [x, y, color.r, color.g, color.b, user.githubNickname, user.url]);
    socket.on("isPixelSuccess", (response: boolean) => {
      if(response) {
        handleIsPixelSuccess(ctx, color.r, color.g, color.b, x, y);
      } else {
        alert("크레딧이 부족합니다!");
      }
      socket.off("isPixelSuccess");
    });
    socket.on("tooFrequent", () => {
      console.log("tooFrequent 실행");
      alert("픽셀 찍는 속도가 너무 빠릅니다!");
      socket.off("tooFrequent");
    });
  }
};