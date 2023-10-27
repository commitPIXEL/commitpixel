import { faEyeDropper, faHand, faPaintBrush } from "@fortawesome/free-solid-svg-icons";
import MenuItem from "./pickerMenuItem";

const Menu = () => {
  return <div className="mt-6 flex justify-between items-center w-[70%]">
    <MenuItem icon={faHand} toolName="panning" />
    <MenuItem icon={faPaintBrush} toolName="painting" />
    <MenuItem icon={faEyeDropper} toolName="copying" />
  </div>;
};

export default Menu;