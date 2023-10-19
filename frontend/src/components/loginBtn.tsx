"use client"

import GitHubIcon from '@mui/icons-material/GitHub';
import Style from './loginBtn.module.css'

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
            <button className={Style.btn} onClick={oauthLogin}>
                <GitHubIcon />
                <p>Sign up with Github</p>
            </button>
        </>
    )
}