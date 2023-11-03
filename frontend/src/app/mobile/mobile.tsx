import CanvasContainer from "../browser/canvas/canvasContainer";
import Menu from "../browser/sidebar/picker/menu/pickerMenu";
import MobileNav from "./mobileNav";
import MobilePicker from "./mobilePicker";
import TestCanvas from "./testCanvas";

const Mobile = () => {
  return(
  <div className="w-screen h-screen items-center flex flex-col bg-bgColor text-white">
    <MobileNav />
    {/* <TestCanvas /> */}
    <CanvasContainer />
    <Menu />
    <MobilePicker />
  </div>
  )
}

export default Mobile;