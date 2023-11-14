import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
        'gradient-conic':
          'conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))',
      },
      colors: {
        "mainColor": "#FFBC00",
        "bgColor": "#222222",
        "textGray": "#7B7B7B",
        "textBlack": "#222222",
      },
      animation: {
        'spin-slow': 'spin 3s linear infinite', // 'spin'은 @keyframes의 이름, 'spin-slow'는 이 애니메이션의 별칭입니다.
      },
      keyframes: {
        spin: {
          'from': { transform: 'rotate(0deg)' },
          'to': { transform: 'rotate(360deg)' },
        },
      },
      cursor: {
        "painting": "url('/static/images/pencil.png'), auto",
        "copying": "url('/static/images/spoid.png'), auto",
      }
    },
  },
  plugins: [],
}
export default config