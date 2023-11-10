import { Accordion, AccordionDetails, CircularProgress } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import RankItem from "../rank/rankItem";

const RankAccordion = ({title, type, data, isLoading}: {
  title: string,
  type: string,
  data: any[],
  isLoading: boolean,
}) => {
  return (
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle title={title} />
      <AccordionDetails className="min-h-0 flex flex-col justify-center items-center pt-4 rounded-b">
        {isLoading ? <CircularProgress /> : <div className="w-full max-h-[200px] flex flex-col no-scrollbar overflow-y-auto">
          {type === "url" ? data?.map((item, index) => <RankItem key={index} rankInfo={item} rank={index + 1} isUrl={true} />) : 
          data?.map((item, index) => <RankItem key={index} rankInfo={item} rank={index + 1} />)}
        </div>}
      </AccordionDetails>
    </Accordion>
  );
};

export default RankAccordion;