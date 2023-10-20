"use client"

import { useEffect } from 'react';
import { useDispatch } from "react-redux";
import { login } from "@/store/slices/authorizationSlice";

export default function LoginHandler() {
    const dispatch = useDispatch();
    
    useEffect(() => {
        const code = new URL(window.location.href).searchParams.get("code");
        console.log(code);

        if(code) {
            fetch('https://jsonplaceholder.typicode.com/todos/1').then((res) => {
                console.log(res);
            }).catch((err) => {
                console.log(err);
            })
            console.log("백엔드에 토큰 요청!!");
            // window.location.href = '/';
        }
    },[])

    return(
        <>
            <p>로그인 중~~</p>    
        </>
    )
}