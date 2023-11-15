"use client";

import axios from "axios";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { login } from "@/store/slices/authorizationSlice";
import { apiUrl } from "@/app/browser/config";

export default function LoginHandler() {
  const dispatch = useDispatch();

  useEffect(() => {
    const code = new URL(window.location.href).searchParams.get("code");

    if (code) {
      axios
        .get(`${apiUrl}/auth/login/github?code=${code}`)
        .then((res) => {
          if (res.status === 200) {
            const accesstoken: string = res.headers.accesstoken;
            dispatch(login(accesstoken));
          }
        })
        .catch((err) => {
          console.log(err);
          alert("깃허브 로그인 실패!");
        })
        .finally(() => {
          window.location.href = "/";
        });
    }
  }, []);

  return (
    <>
      <p>로그인 중~~</p>
    </>
  );
}
