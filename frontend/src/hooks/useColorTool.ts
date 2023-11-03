import Icolor from "@/interfaces/color";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";

const useColorTool = (setPixel: any, socket: any, dispatch: any, ctx: any, panzoomInstance: any, tool: string | null, color: Icolor, x: number, y: number) => {
  const audio = new Audio('/sounds/zapsplat_foley_footstep_stamp_wood_panel_19196.mp3');

  const toHex = (rgbData: number) => {
    let hex = rgbData.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
  };
  
  const rgbtoHex = (r: number, g: number, b: number) => {
    return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
  };
  
  if(tool === null || tool === undefined) {
    panzoomInstance?.pause();
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

export default useColorTool;