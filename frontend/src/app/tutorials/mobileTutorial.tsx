import MobileNav from "../mobile/mobileNav";
import PatchNote from "./patchNote";
import TutorialDesc from "./tutorialDescription";
import MoveButton from "./moveButton";

const MobileTutorial = () => {
  return (
    <div className="w-screen h-fit">
      <div className="w-full h-full overflow-y-auto no-scrollbar">
        <MobileNav />
        <div className="bg-bgColor w-full h-[92%] text-white flex flex-col p-6">
          <TutorialDesc isMobile={true} />
          <PatchNote isMobile={true} />
          <MoveButton />
        </div>
      </div>
    </div>
  );
};

export default MobileTutorial;