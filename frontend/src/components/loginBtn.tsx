"use client"

import GitHubIcon from '@mui/icons-material/GitHub';

interface loginProps {
    clientId: string,
    redirectUrl: string
}

export default function loginBtn({clientId, redirectUrl}: loginProps) {
    const oauthLogin = () => {
        const githubURL = `https://github.com/login/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUrl}&scope=repo%20read:user`;

        window.location.href = githubURL;
        console.log(clientId);
    }

    return(
        <>
            <button className="flex justify-around items-center space-x-4 font-bold bg-white rounded-md" onClick={oauthLogin}>
                <GitHubIcon />
                <p>Sign up with Github</p>
            </button>
        </>
    )
}