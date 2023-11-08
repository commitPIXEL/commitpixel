import { Accordion, AccordionDetails } from "@mui/material";
import AccordionTitle from "./accordionTitle";
import RankItem from "../rank/rankItem";

const RankAccordion = ({title, type}: {
  title: string,
  type: string,
}) => {
  return (
    <Accordion defaultExpanded={true} className="!rounded mb-6">
      <AccordionTitle title={title} />
      <AccordionDetails className="min-h-0 flex flex-col justify-center items-center pt-4 rounded-b">
        <div className="w-full max-h-[200px] flex flex-col no-scrollbar overflow-y-auto">
          {type === "url" ? urlData.map((item) => <RankItem key={item.rank} rankInfo={item} isUrl={true} />) : 
          pixelData.map((item) => <RankItem key={item.rank} rankInfo={item} />)}
        </div>
      </AccordionDetails>
    </Accordion>
  );
};

const pixelData = [ 
  {
    rank:1,
    content: "githubNick",
    pixels: 1000,
  },
  {
    rank:2,
    content: "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
    pixels: 900,
  },
  {
    rank:3,
    content: "orange",
    pixels: 800,
  },
  {
    rank:4,
    content: "eevee",
    pixels: 700,
  },
  {
    rank:5,
    content: "browniie",
    pixels: 600,
  },
  {
    rank:6,
    content: "teddybear",
    pixels: 500,
  },
  {
    rank:7,
    content: "knotted",
    pixels: 400,
  },
  {
    rank:8,
    content: "kurby",
    pixels: 300,
  },
  {
    rank:9,
    content: "meow",
    pixels: 200,
  },
  {
    rank:10,
    content: "mouse",
    pixels: 100,
  },
];

const urlData = [
  {
    rank:1,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 1000,
  },
  {
    rank:2,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 900,
  },
  {
    rank:3,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 800,
  },
  {
    rank:4,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 700,
  },
  {
    rank:5,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 600,
  },
  {
    rank:6,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 500,
  },
  {
    rank:7,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 400,
  },
  {
    rank:8,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 300,
  },
  {
    rank:9,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 200,
  },
  {
    rank:10,
    content: "https://edu.ssafy.com/edu/main/index.do",
    pixels: 100,
  },
];

export default RankAccordion;