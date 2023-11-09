import { ReduxProvider } from "@/store/reduxProvider";
import "./globals.css";
import "../../public/static/fonts/style.css";
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import Head from "next/head";
import Script from "next/script";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "commit Pixel",
  description: "commit Pixel by SSAFY 9기 자율 프로젝트 진짜_진짜_최종_팀",
  applicationName: "SSAFY 9기 진짜_진짜_최종_팀의 commit Pixel",
  verification: {
    google: "7K6yIIQPoGYgAfRJq9vZliAIG5ZEGaQVA_EaD9wEY3A",
  },
  authors: [
    {
      name: "이용현",
    },
    { name: "김희조" },
    { name: "권도현" },
    { name: "한다솜" },
    { name: "김인범" },
    { name: "이예린" },
  ],
  generator: "Next.js",
  keywords: [
    "commit Pixel",
    "commit pixel",
    "Commit Pixel",
    "commit",
    "pixel",
    "커밋",
    "깃허브",
    "픽셀",
    "커밋 픽셀",
    "깃허브 커밋",
    "백준",
    "solved.ac",
    "알고리즘 문제",
    "커밋하기",
    "픽셀 아트",
    "github",
    "Github",
    "Baekjoon",
    "baekjoon",
    "Solved.ac",
    "솔브드",
    "포트폴리오 홍보",
    "제품 홍보",
    "아트",
    "pixel art",
    "dot",
    "도트",
    "도트 찍기",
    "도트 아트",
    "commit",
    "git",
    "canvas",
    "r/place",
    "공유 캔버스",
    "공유 픽셀 아트",
    "url",
    "url홍보",
    "커밋하고 url홍보",
    "ssafy",
    "SSAFY",
    "ssafy 9기",
    "삼성청년SW아카데미",
  ],
  referrer: "origin",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ko">
      <Head>
        <meta property="og:title" content="COMMIT PIXEL" />
        <meta property="og:description" content="픽셀 찍고 나만의 URL을 홍보하세요." />
        <meta property="og:image" content="https://dev.commitpixel.com/static/images/pixelFox.png" />
        <meta property="og:url" content="https://dev.commitpixel.com/" />
        <meta name="viewport" content="initial-scale=1.0, width=device-width" />
      </Head>
      <body className={inter.className}>
        <Script
          src="https://t1.kakaocdn.net/kakao_js_sdk/2.4.0/kakao.min.js"
          integrity="sha384-mXVrIX2T/Kszp6Z0aEWaA8Nm7J6/ZeWXbL8UpGRjKwWe56Srd/iyNmWMBhcItAjH"
          crossOrigin="anonymous"
        ></Script>
        <ReduxProvider>{children}</ReduxProvider>
      </body>
    </html>
  );
}
