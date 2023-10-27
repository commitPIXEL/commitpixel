import Panzoom from "panzoom";
import { useEffect, useRef } from "react";

const TestCanvas = () => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null); 
  const ref = useRef<HTMLDivElement | null>(null);
  const canvasWrapper = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const div = ref.current;
    const initialZoom = 0.3;
    if (div && div.parentElement) {
      const panzoom = Panzoom(div, {
        zoomDoubleClickSpeed: 1,
        initialZoom: initialZoom,
      });
      
      const viewportWidth = div.parentElement.clientWidth;
      const viewportHeight = div.parentElement.clientHeight;
      const elementWidth = div.clientWidth * initialZoom;
      const elementHeight = div.clientHeight * initialZoom;

      const x = (viewportWidth - elementWidth) / 2;
      const y = (viewportHeight - elementHeight) / 2;

      panzoom.moveTo(x, y);

      return () => {
        panzoom.dispose();
      };
    }
  }, []);

  return (
    <div className="w-full h-[50%] flex flex-col items-center justify-center">
      <div
        className="overflow-hidden bg-bgColor"
        style={{
          maxWidth: "95vw",
          maxHeight: "50vh",
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
              width={1024}
              height={1024}
              className="canvas"
              ref={canvasRef}
            >
              캔버스를 지원하지 않는 브라우저입니다. 크롬으로 접속해주세요!
            </canvas>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TestCanvas;