import { Accordion, AccordionDetails } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import Picker from "../picker/picker";
import Menu from "../picker/menu/pickerMenu";

const PickerAccordion = () => {
  return (
    <Accordion defaultExpanded={true} className="!rounded">
        <AccordionTitle title="색깔" />
        <AccordionDetails className="flex flex-col justify-center items-center pt-4 rounded-b">
          <Picker />
          <Menu />
        </AccordionDetails>
      </Accordion>
  );
};

export default PickerAccordion;
