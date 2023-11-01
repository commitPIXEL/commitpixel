"use client"

import axios from 'axios';
import { useEffect } from 'react';
import { useDispatch, useSelector } from "react-redux";
import { login } from "@/store/slices/authorizationSlice";

export default function LoginHandler() {
    const dispatch = useDispatch();
    
    useEffect(() => {
        const code = new URL(window.location.href).searchParams.get("code");
        console.log(code);

        if(code) {
            // axios.get(`http://localhost:8080/auth/login/github?code=${code}`) // 로컬용
            axios.get(`https://dev.commitpixel.com/api/auth/login/github?code=${code}`) // 배포용
            .then((res) => {
                console.log(res);
                console.log(res.headers);
                if(res.status === 200) {
                    const accesstoken: string = res.headers.accesstoken;
                    dispatch(login(accesstoken));
                }
            })
            .catch((err) => {
                console.log(err);
                alert("깃허브 로그인 실패!");
            }).finally(() => {
                window.location.href = "/";
            })
        }
    },[])

    return(
        <>
            <p>로그인 중~~</p>    
        </>
    )
}
