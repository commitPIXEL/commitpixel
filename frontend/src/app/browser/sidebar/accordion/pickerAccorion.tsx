import { Accordion, AccordionDetails } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import Picker from "../picker/picker";
import Menu from "../picker/menu/pickerMenu";

const PickerAccordion = ({isConnected}: {
  isConnected: boolean,
}) => {
  return (
    <Accordion disabled={!isConnected} defaultExpanded={true && isConnected} className="!rounded mb-6">
      <AccordionTitle title="색깔" />
      <AccordionDetails className="flex flex-col justify-center items-center pt-4 rounded-b">
        <Picker />
        <Menu />
      </AccordionDetails>
    </Accordion>
  );
};

export default PickerAccordion;
