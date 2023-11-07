"use client"
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/store";
import BrowserTutorial from "./browserTutorial";
import { setDevice } from "@/store/slices/deviceSlice";
import MobileTutorial from "./mobileTutorial";

export default function Tutorials () {
  const dispatch = useDispatch();
  const device = useSelector((state: RootState) => state.device.device);

  const handleResize = () => {
    if (window.innerWidth < 769 && window.innerWidth <= window.innerHeight) {
      dispatch(setDevice("mobile"));
    } else if (window.innerHeight < 769 && window.innerWidth < 1024 && window.innerWidth > window.innerHeight) {
      dispatch(setDevice("mobile-warning"));
    } else if (window.innerHeight >= 769 && window.innerWidth < window.innerHeight) { 
      dispatch(setDevice("browser-warning"));
    }else {
      dispatch(setDevice("browser"));
    }
  };

  useEffect(() => {
    handleResize();
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  return (
    device == "mobile" ? <MobileTutorial /> : <BrowserTutorial />
  );
};
