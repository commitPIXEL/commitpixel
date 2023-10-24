import { ReduxProvider } from '@/store/reduxProvider'
import './globals.css'
import '../../public/static/fonts/style.css'
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'commit Pixel',
  description: 'commit Pixel by SSAFY 9기 자율 프로젝트 진짜_진짜_최종_팀',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="ko">
      <body className={inter.className}>
        <ReduxProvider>
          {children}
        </ReduxProvider>
      </body>
    </html>
  )
}
