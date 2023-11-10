import LoginBtn from "@/components/loginBtn";
import BoardBtn from "@/components/boardBtn";
import PickerAccordion from "./accordion/pickerAccordion";
import UserInfoAccordion from "./accordion/userInfoAccordion";
import { useSelector } from "react-redux";
import { RootState } from "@/store";
import KakaoShare from "@/components/kakaoShare";
import RankAccordion from "./accordion/rankAccordion";
import { apiUrl } from "../config";
import axios from "axios";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";

const Sidebar = () => {
  const user = useSelector((state: RootState) => state.user);
  const [userRank, setUserRank] = useState<any>(null);
  const [urlRank, setUrlRank] = useState<any>(null);
  const { isLoading, error } = useQuery(
    ["ranking-list"],
    async () => {
      const response = await axios.get(apiUrl + "/rank");
      setUrlRank(response?.data?.urlRankList);
      setUserRank(response?.data?.userRankList);
      return response.data;
    },
    { 
      refetchInterval: 10000, 
    }
  );

  return user.githubNickname ? (
    <div className="no-scrollbar pb-20 flex flex-col col-span-1 bg-bgColor w-full h-full pr-10 overflow-y-scroll">
      <UserInfoAccordion />
      <PickerAccordion />
      <RankAccordion title="URL 랭킹" type="url" data={urlRank} isLoading={isLoading} />
      <RankAccordion title="Pixel 랭킹" type="pixel" data={userRank} isLoading={isLoading} />
      <KakaoShare />
      <BoardBtn />
    </div>
  ) : (
    <div className="no-scrollbar pb-20 flex flex-col col-span-1 bg-bgColor w-full h-full pr-10 overflow-y-scroll">
      {/* TODO: dev 테스트 용 */}
      {/* <UserInfoAccordion /> */}
      {/* <PickerAccordion /> */}
      <LoginBtn />
      <KakaoShare />
      <RankAccordion title="URL 랭킹" type="url" data={urlRank} isLoading={isLoading} />
      <RankAccordion title="Pixel 랭킹" type="pixel" data={userRank} isLoading={isLoading} />
      {/* <BoardBtn /> */}
    </div>
  );
};

export default Sidebar;
