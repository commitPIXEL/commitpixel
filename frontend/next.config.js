/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: false,
}

module.exports = {
  output: 'standalone',
  reactStrictMode: false,
  images: {
    domains: ['avatars.githubusercontent.com'],
  }
}
