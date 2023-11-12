import { Accordion, AccordionDetails } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import Picker from "../picker/picker";
import Menu from "../picker/menu/pickerMenu";

const PickerAccordion = () => {
  return (
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle title="색깔" />
      <AccordionDetails className="flex flex-col justify-center items-center rounded-b">
        <Menu />
        <Picker />
      </AccordionDetails>
    </Accordion>
  );
};

export default PickerAccordion;
