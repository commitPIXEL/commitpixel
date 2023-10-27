import { RootState } from "@/store";
import { setTool } from "@/store/slices/toolSlice";
import { IconProp } from "@fortawesome/fontawesome-svg-core";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useDispatch, useSelector } from "react-redux";

const MenuItem = ({icon, toolName}: {
  icon: IconProp,
  toolName: string,
}) => {
  const dispatch = useDispatch();
  const tool = useSelector((state:RootState) => state.tool.tool);
  const isUsing = tool === toolName ? " bg-mainColor " : "";
  const handleToolClick = () => {
    if(tool === toolName) {
      dispatch(setTool(null));
    } else {
      dispatch(setTool(toolName));
    }
  }
  return (
    <div onClick={handleToolClick} className={`flex justify-center items-center ml-1 mr-1 p-2 cursor-pointer text-xl border rounded-full aspect-square w-10 ${isUsing}`}>
      <FontAwesomeIcon icon={icon} />
    </div>
  );
};

export default MenuItem;