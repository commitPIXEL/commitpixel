import { Accordion, AccordionDetails } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import Picker from "../picker/picker";
import Menu from "../picker/menu/pickerMenu";
import ImageToPixel from "../picker/imageToPixel";
import Timelapse from "../picker/timelapse";

const PickerAccordion = () => {
  return (
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle title="색깔" />
      <AccordionDetails className="flex flex-col justify-center items-center pt-4 rounded-b">
        <Menu />
        <Picker />
        <div className=" mt-8 w-full min-h-[40px] flex justify-between items-center">
          <ImageToPixel />
          <Timelapse />
        </div>
      </AccordionDetails>
    </Accordion>
  );
};

export default PickerAccordion;
