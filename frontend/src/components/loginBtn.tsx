"use client"

import GitHubIcon from '@mui/icons-material/GitHub';

const LoginBtn = ({color}: {
  color?: string,
}) => {
  const clientId = process.env.NEXT_PUBLIC_CLIENT_ID;
  const redirectUrl = process.env.NEXT_PUBLIC_CALLBACK_URL;

  const oauthLogin = () => {
    const githubURL = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUrl}`;

    window.location.href = githubURL;
  };

  return (
    <>
      <button
        className={`flex justify-around items-center space-x-4 font-bold text-bgColor ${color === "main" ? " bg-mainColor " : " bg-white "} rounded h-12 mt-6 p-3`}
        onClick={oauthLogin}
      >
        <GitHubIcon />
        <p className="flex-1">Sign up with Github</p>
      </button>
    </>
  );
}

export default LoginBtn;