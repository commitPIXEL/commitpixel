import { NOT_ENOUGH_CREDIT, TOO_FREQUENT_PIXEL } from "@/constants/message";
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
    
    socket.off("isPixelSuccess");
    socket.off("tooFrequent");

    socket.on("isPixelSuccess", (response: boolean) => {
      if(response) {
        handleIsPixelSuccess(ctx, color.r, color.g, color.b, x, y);
      } else {
        alert(NOT_ENOUGH_CREDIT);
      }
    });

    socket.on("tooFrequent", () => {
      alert(TOO_FREQUENT_PIXEL);
    });
  }
};
