import { Accordion, AccordionDetails, CircularProgress } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import RankItem from "../rank/rankItem";
import { useAutoAnimate } from "@formkit/auto-animate/react";

const RankAccordion = ({title, type, data, myRank, isLoading}: {
  title: string,
  type: string,
  data: any[],
  myRank?: any,
  isLoading: boolean,
}) => {
  const [animationParent] = useAutoAnimate();
  const myRankInfo = {
    githubNickname: "나",
    pixelNum: myRank?.pixelNum,
  };
  return (
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle title={title} />
      <AccordionDetails className="min-h-0 flex flex-col justify-center items-center pt-4 rounded-b">
        {isLoading ? (
          <CircularProgress />
        ) : (
          <div className="w-full max-h-[200px] no-scrollbar overflow-y-auto">
            {type === "pixel" && myRank ? <RankItem key="my" rank={ myRank?.rank } rankInfo={ myRankInfo } isUserRank={true} /> : null}
            <div ref={animationParent} className="w-full h-full flex flex-col pr-8">
              {type === "url"
                ? data?.map((item, index) => (
                    <RankItem
                      key={index}
                      rankInfo={item}
                      rank={index + 1}
                      isUrl={true}
                    />
                  ))
                : data?.map((item, index) => (
                    <RankItem key={index} rankInfo={item} rank={index + 1} />
                  ))}
            </div>
          </div>
        )}
      </AccordionDetails>
    </Accordion>
  );
};

export default RankAccordion;
