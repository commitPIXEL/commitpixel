"use client";
import { BrowserView, MobileView } from "react-device-detect";
import Browser from "./browser/browser";
import Mobile from "./mobile/mobile";

export default function Home() {

  return (
    <>
      <BrowserView>
        <Browser />
      </BrowserView>
      <MobileView>
        <Mobile />
      </MobileView>
    </>
  );
}