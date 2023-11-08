"use client"

import GitHubIcon from '@mui/icons-material/GitHub';

export default function loginBtn({color}: {
  color?: string,
}) {
  const clientId = process.env.NEXT_PUBLIC_CLIENT_ID;
  const redirectUrl = process.env.NEXT_PUBLIC_CALLBACK_URL;

  const oauthLogin = () => {
    const githubURL = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUrl}&scope=repo%20read:user`;

    window.location.href = githubURL;
  };

  return (
    <>
      <button
        className={`flex justify-around items-center space-x-4 font-bold text-bgColor p-2 ${color === "main" ? " bg-mainColor " : " bg-white "} rounded h-12 mt-6 mb-6`}
        onClick={oauthLogin}
      >
        <GitHubIcon />
        <p>Sign up with Github</p>
      </button>
    </>
  );
}
