"use client"

import { Tooltip } from "@mui/material";

const RankItem = ({ rankInfo, isUrl, rank }: {
  rankInfo: {
    url: string,
    pixelNum: number,
    githubNickname: string,
  },
  isUrl?: boolean,
  rank: number
}) => {
  const urlStyle = "cursor-pointer text-xs";
  let rankStyle = "";
  if(rank === 1) {
    rankStyle = "text-[#FFCA10]";
  } else if(rank === 2) {
    rankStyle = "text-[#BAD1D6]";
  } else if(rank === 3) {
    rankStyle = "text-[#BC800A]";
  }

  return (
    <div className="cursor-default text-textBlack w-full min-h-[40px] grid grid-cols-10 gap-4 place-content-center place-items-center">
      <div className={`place-self-start col-span-1 text-lg ${rankStyle}`}>{rank}</div>
      <div className={`w-full h-full flex items-center col-span-5 line-clamp-1 ${isUrl ? urlStyle : ""}`}>
        { isUrl ? 
        <Tooltip title={rankInfo.url}>
          <a href={rankInfo.url} target="_blank">{rankInfo.url}</a>
        </Tooltip>
        : rankInfo.githubNickname}
      </div>
      <div className="place-self-end col-span-4 line-clamp-1 flex items-center">
        <div>{rankInfo.pixelNum}</div>
        <div className="ml-1 text-sm">pixels</div>
      </div>
    </div>
  );
}

export default RankItem;