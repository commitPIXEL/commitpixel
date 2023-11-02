"use client"

import GitHubIcon from '@mui/icons-material/GitHub';

export default function loginBtn() {
  const clientId = process.env.NEXT_PUBLIC_CLIENT_ID;
  const redirectUrl = process.env.NEXT_PUBLIC_CALLBACK_URL;

  const oauthLogin = () => {
    const githubURL = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUrl}&scope=repo%20read:user`;

    window.location.href = githubURL;
    console.log(clientId);
  };

  return (
    <>
      <button
        className="flex justify-around items-center space-x-4 font-bold bg-white rounded h-12 mt-6"
        onClick={oauthLogin}
      >
        <GitHubIcon />
        <p>Sign up with Github</p>
      </button>
    </>
  );
}