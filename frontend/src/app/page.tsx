"use client"
import { useDispatch, useSelector } from "react-redux";
import Browser from "./browser/browser";
import Mobile from "./mobile/mobile";
import { useEffect } from "react";
import { setDevice } from "@/store/slices/deviceSlice";
import { RootState } from "@/store";
import { useRouter } from "next/navigation";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

export default function Home() {
  const router = useRouter();
  const dispatch = useDispatch();
  const device = useSelector((state: RootState) => state.device.device);
  const queryClient = new QueryClient();

  const handleResize = () => {
    if (window.innerWidth < 769 && window.innerWidth <= window.innerHeight) {
      dispatch(setDevice("mobile"));
    } else if (window.innerHeight < 769 && window.innerWidth < 1032 && window.innerWidth > window.innerHeight) {
      dispatch(setDevice("mobile-warning"));
    } else if (window.innerHeight >= 769 && window.innerWidth < window.innerHeight) { 
      dispatch(setDevice("browser-warning"));
    }else {
      dispatch(setDevice("broswer"));
    }
  };

  useEffect(() => {
    handleResize();
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  useEffect(() => {
    const isVisited = localStorage.getItem("isVisited");
    if (!isVisited) {
      router.push("tutorials");
  }}, []);

  return (
    <>
    <QueryClientProvider client={queryClient}>
      {device ? 
        (device.includes("mobile") ? 
        (device.includes("warning") ? <div className="bg-mainColor w-screen h-screen flex justify-center items-center text-2xl">세로 모드로 접속해주세요.</div> : <Mobile />)
        : (device.includes("warning") ? <div  className="bg-mainColor w-screen h-screen flex justify-center items-center text-4xl">가로 모드로 접속해주세요.</div> : <Browser />)) 
        : null}
        </QueryClientProvider>
    </>
  );
};
