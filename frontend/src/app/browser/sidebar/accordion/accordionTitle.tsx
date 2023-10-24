import { AccordionSummary } from "@mui/material";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

const AccordionTitle = ({title, isProfile}: {
  title: string,
  isProfile ?: boolean,
}) => {
  return (
    <AccordionSummary
          className="w-full !bg-mainColor flex justify-between items-center !rounded h-10 line-clamp-1"
          expandIcon={<ExpandMoreIcon />}
        >
          { isProfile ?
          <div className="w-full h-full flex pr-4 items-center justify-between text-lg">
            <div className="w-11 aspect-square rounded-full bg-pink-400"></div>
            <div className="w-[90%] text-lg text-right line-clamp-1">githubNick</div>
          </div> : <div className="text-lg">{title}</div>}
        </AccordionSummary>
  );
};// aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

export default AccordionTitle;
