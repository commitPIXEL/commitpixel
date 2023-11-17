import { faEyeDropper, faHand, faPaintBrush } from "@fortawesome/free-solid-svg-icons";
import MenuItem from "./pickerMenuItem";
import { useSelector } from "react-redux";
import { RootState } from "@/store";

const Menu = () => {
  const device = useSelector((state: RootState) => state.device.device);
  const pcClass = "mt-2 mb-6 flex justify-between items-center w-[70%]";
  const mobileClass = "flex w-[50%] justify-between";
  return <div className={device === "mobile" ? mobileClass : pcClass}>
    {device === "mobile" ? <MenuItem icon={faHand} toolName="panning" /> : null}
    <MenuItem icon={faPaintBrush} toolName="painting" />
    <MenuItem icon={faEyeDropper} toolName="copying" />
  </div>;
};

export default Menu;