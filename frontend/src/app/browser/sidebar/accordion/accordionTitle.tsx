import { AccordionSummary } from "@mui/material";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Image from 'next/image';

const AccordionTitle = ({githubNickname, profileImage}: {
  githubNickname: string,
  profileImage: string,
}) => {
  return (
    <AccordionSummary
      className="w-full !bg-mainColor flex justify-between items-center !rounded h-10 line-clamp-1"
      expandIcon={<ExpandMoreIcon />}
    >
      <div className="w-full h-full flex pr-4 items-center justify-between text-lg">
        <Image
          src={profileImage}
          alt="GitHub Profile"
          className="w-11 h-11 aspect-square rounded-full object-cover"
        />
        <div className="w-[90%] text-lg text-right line-clamp-1">{githubNickname}</div>
      </div>
    </AccordionSummary>
  );
};// aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

export default AccordionTitle;
