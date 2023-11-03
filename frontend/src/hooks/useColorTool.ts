import { useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { pick } from "@/store/slices/colorSlice";
import { setTool as setToolAction } from "@/store/slices/toolSlice";
import { RootState } from '@/store';

const useColorTool = (setPixel: any, socket: any, ctx: any, panzoomInstance: any) => {
  const dispatch = useDispatch();
  const tool = useSelector((state: RootState) => state.tool.tool);
  const color = useSelector((state: RootState) => state.color.color);
  const audio = new Audio('/sounds/zapsplat_foley_footstep_stamp_wood_panel_19196.mp3');

  const toHex = useCallback((rgbData: any) => {
    let hex = rgbData.toString(16);
    return hex.length === 1 ? "0" + hex : hex;
  }, []);

  const rgbToHex = useCallback((r: number, g: number, b: number) => `#${toHex(r)}${toHex(g)}${toHex(b)}`, [toHex]);

  const useTool = useCallback((x: number, y: number) => {
    if (!tool) {
      panzoomInstance?.pause();
      socket?.emit("url", [x, y]);
      return;
    }
    
    if (tool === "painting") {
      panzoomInstance?.pause();
      audio.play();
      const { r, g, b } = color.rgb;
      setPixel(x, y, { r, g, b }, "githubNick", "https://www.naver.com/");
    } else if (tool === "copying" && ctx) {
      panzoomInstance?.pause();
      const pixelData = ctx.getImageData(x, y, 1, 1).data;
      const [r, g, b] = pixelData;
      const hex = rgbToHex(r, g, b);
      dispatch(pick({
        hex: hex,
        rgb: { r, g, b, a: 1 },
      }));
      dispatch(setToolAction("painting"));
    }
  }, [tool, color, setPixel, socket, panzoomInstance, audio, dispatch, rgbToHex, ctx]);

  return useTool;
};

export default useColorTool;