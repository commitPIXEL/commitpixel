import CanvasContainer from "../browser/canvas/canvasContainer";
import Menu from "../browser/sidebar/picker/menu/pickerMenu";
import MobileNav from "./mobileNav";
import MobilePicker from "./mobilePicker";

const Mobile = () => {

  return(
  <div className="w-screen h-screen items-center flex flex-col bg-bgColor text-white">
    <MobileNav />
    <CanvasContainer />
    <Menu />
    <MobilePicker />
  </div>
  )
}

export default Mobile;