import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Nav from "../browser/nav";
import PatchNote from "./patchNote";
import TutorialDesc from "./tutorialDescription";
import { faEyeDropper, faPaintBrush } from "@fortawesome/free-solid-svg-icons";
import MoveButton from "./moveButton";
import ToolDescription from "./toolDescription";

const BrowserTutorial = () => {
  return (
    <div className="w-screen h-screen">
      <Nav />
      <div className="bg-bgColor w-full h-[92%] text-white flex flex-col p-6">
        <div className="flex w-full h-full justify-between">
          <div className="w-[50%] flex flex-col justify-between h-full">
            <TutorialDesc />
            <PatchNote />
          </div>
          <div className="w-[50%] h-full">
            <ToolDescription />
          </div>
        </div>
      </div>
    </div>
  );
}

export default BrowserTutorial;