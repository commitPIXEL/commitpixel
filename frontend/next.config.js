/** @type {import('next').NextConfig} */
const withPWA = require('next-pwa')({
  dest: 'public/manifest',
  register: true,
  skipWaiting: true,
})

module.exports = withPWA({
  output: 'standalone',
  reactStrictMode: false,
  images: {
    domains: ['avatars.githubusercontent.com', 'dev.commitpixel.com', 'commitpixel.com', 'developers.kakao.com'],
  }
})
