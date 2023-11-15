import Nav from "../browser/nav/nav";
import PatchNote from "./patchNote";
import TutorialDesc from "./tutorialDescription";
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