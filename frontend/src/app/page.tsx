"use client";
import { BrowserView, MobileView } from "react-device-detect";
import Browser from "./browser/browser";
import Mobile from "./mobile/mobile";
import { Provider } from "react-redux";
import store from "../store/index";

export default function Home() {
  return (
    <>
      <Provider store={store}>
        <BrowserView>
          <Browser />
        </BrowserView>
        <MobileView>
          <Mobile />
        </MobileView>
      </Provider>
    </>
  );
}