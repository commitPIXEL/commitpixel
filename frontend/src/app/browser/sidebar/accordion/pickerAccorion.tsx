import { Accordion, AccordionDetails } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import Picker from "../picker/picker";
import Menu from "../picker/menu/pickerMenu";

const PickerAccordion = ({isConnected}: {
  isConnected: boolean,
}) => {
  return (
    // <Accordion disabled={!isConnected} defaultExpanded={true && isConnected} className="!rounded mb-6">
    // TODO: 개발 버전과 TEST버전 잘 구별하기
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle title="색깔" />
      <AccordionDetails className="flex flex-col justify-center items-center pt-4 rounded-b">
        <Menu />
        <Picker />
      </AccordionDetails>
    </Accordion>
  );
};

export default PickerAccordion;
