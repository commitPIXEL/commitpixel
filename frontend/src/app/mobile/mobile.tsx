import CanvasContainer from "../browser/canvas/canvasContainer";
import MobileNav from "./mobileNav";
import MobilePicker from "./mobilePicker";
import TestCanvas from "./testCanvas";

const Mobile = () => {
  return(
  <div className="w-screen h-screen flex flex-col items-center bg-bgColor text-white">
    <MobileNav />
    <TestCanvas />
    {/* <CanvasContainer /> */}
    <MobilePicker />
  </div>
  )
}

export default Mobile;