import { Dispatch, SetStateAction } from "react";

export interface IUserInfo {
  githubNickname: string;
  profileImage: string;
  url: string;
  isSolvedACAuth: boolean;
}

export interface IUserPixel {
  githubNickname: string | null;
  totalCredit: number;
  availablePixel: number;
}

export interface IIsSolvedACAuth {
  isSolvedACAuth: boolean;
}

export interface IResSolvedAC {
  totalCredit: number;
  availablePixel: number;
}

export interface IBoardInputProps {
  open: boolean;
  setOpen: Dispatch<SetStateAction<boolean>>;
}

export interface IBeforeInstallPromptEvent extends Event {
  readonly platforms: string[];
  readonly userChoice: Promise<{ outcome: 'accepted' | 'dismissed', platform: string }>;
  prompt(): Promise<void>;
}

export interface IBeforeInstallPromptEventListener {
  (evt: IBeforeInstallPromptEvent): void;
}