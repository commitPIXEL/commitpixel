"use client";

import Store from "../store/index";
import { Provider } from "react-redux";

export function ReduxProvider({ children }: { children: React.ReactNode }) {
  return <Provider store={Store}>{children}</Provider>;
}
