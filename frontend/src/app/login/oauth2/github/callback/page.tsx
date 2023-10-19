"use client"

import { useDispatch } from "react-redux";
import { login } from "@/store/slices/authorizationSlice";

export default function LoginHandler() {
    const dispatch = useDispatch();
    const code = new URL(window.location.href).searchParams.get("code");
    console.log(code);

    if(code) {
        // fetch("API end point", {
        //     method: 'Post',
        // }).then((res) => {
        //     const authHeader = res.headers.authorization;
        //     if (authHeader) {
        //         dispatch(login());
        //         window.location.href = '/';
        //     } else {
        //         console.error('Authorization header is missing in the response.');
        //     }

        // }).catch((err) => {
        //     console.log(err);
        // })
        console.log("백엔드에 토큰 요청!!");
        // window.location.href = '/';
    }

    return(
        <>
            <p>로그인 중~~</p>    
        </>
    )
}