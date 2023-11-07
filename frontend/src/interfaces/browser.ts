export interface IUserInfo {
  githubNickname: string;
  profileImage: string;
  url: string;
  isSolvedACAuth: boolean;
}

export interface IUserPixel {
  githubNickname: string;
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

