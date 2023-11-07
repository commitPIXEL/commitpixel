export interface IUserInfo {
  githubNickname: string;
  profileImage: string;
  url: string;
  // TODO: 배포 터져서 임시로 타입 추가
  isSolvedACAuth?: string | boolean | undefined;
}

export interface IUserPixel {
  githubNickname: string;
  totalCredit: number;
  availablePixel: number;
}

export interface IIsSolvedACAuth {
// TODO: 배포 터져서 임시로 타입 추가
  isSolvedACAuth: string | boolean | undefined;
}
