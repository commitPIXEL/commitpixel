"use client";

import axios from "axios";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { login } from "@/store/slices/authorizationSlice";
import { apiUrl } from "@/app/browser/config";
import useFetchUser from "@/hooks/useFetchUser";
import { RootState } from "@/store";

export default function LoginHandler() {
  const dispatch = useDispatch();
  const setUser = useFetchUser();
  const accessToken = useSelector(
    (state: RootState) => state.authorization.authorization
  );

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
          alert("토큰 받기 실패!");
          window.location.href = "/";
        });
    }
  }, []);

  useEffect(() => {
    if(accessToken) {
      setUser;
      window.location.href = "/";
    }
  }, [accessToken]);

  return (
    <>
      <p>로그인 중~~</p>
    </>
  );
}
