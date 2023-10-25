const RankItem = ({ rankInfo, isUrl }: {
  rankInfo: {
    rank: number,
    content: string,
    pixels: number,
  },
  isUrl?: boolean
}) => {
  const urlStyle = "cursor-pointer text-xs";
  let rankStyle = "";
  if(rankInfo.rank === 1) {
    rankStyle = "text-[#FFCA10]";
  } else if(rankInfo.rank === 2) {
    rankStyle = "text-[#BAD1D6]";
  } else if(rankInfo.rank === 3) {
    rankStyle = "text-[#BC800A]";
  }

  return (
    <div className="cursor-default text-textBlack w-full min-h-[40px] grid grid-cols-10 gap-4 place-content-center place-items-center">
      <div className={`place-self-start col-span-1 text-lg ${rankStyle}`}>{rankInfo.rank}</div>
      <div className={`w-full h-full flex items-center col-span-5 line-clamp-1 ${isUrl ? urlStyle : ""}`}>
        { isUrl ? <a href={rankInfo.content} target="_blank">{rankInfo.content}</a> : rankInfo.content}
      </div>
      <div className="place-self-end col-span-4 line-clamp-1 flex items-center">
        <div>{rankInfo.pixels}</div>
        <div className="ml-1 text-sm">pixels</div>
      </div>
    </div>
  );
}

export default RankItem;