import { Accordion, AccordionDetails, AccordionSummary, Typography } from "@mui/material";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import LoginBtn from "@/components/loginBtn";
import BoardBtn from "@/components/boardBtn"
import Menu from "./picker/menu/pickerMenu";

const Sidebar = () => {
  const clientId = process.env.NEXT_PUBLIC_CLIENT_ID;
  const redirectUrl = process.env.NEXT_PUBLIC_CALLBACK_URL;

  return (
    <div className="flex flex-col col-span-1 bg-bgColor w-full h-full pt-10 pb-10 pr-10 overflow-y-scroll">
      <BoardBtn />
      <Accordion defaultExpanded={true}>
        <AccordionSummary
          className="bg-mainColor rounded h-10"
          expandIcon={<ExpandMoreIcon />}
        >
          <Typography className="font-bold text-lg">색깔</Typography>
        </AccordionSummary>
        <AccordionDetails className="flex flex-col justify-center items-center pt-4">
          <Menu />
        </AccordionDetails>
      </Accordion>
      <LoginBtn
        clientId={clientId as string}
        redirectUrl={redirectUrl as string}
      />
    </div>
  );
};

export default Sidebar;