"use client";

import axios from "axios";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { login } from "@/store/slices/authorizationSlice";
import { apiUrl } from "@/app/browser/config";
import useFetchUser from "@/hooks/useFetchUser";
import { RootState } from "@/store";
import { FAIL_TO_GET_TOKEN } from "@/constants/message";

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
          alert(FAIL_TO_GET_TOKEN);
        });
    }
  }, []);

  useEffect(() => {
    if(accessToken) {
      const resUser = setUser().then((res) => {
        if(res.success) {
          window.location.href = "/";
        } else {
          console.log(res.error);
        }
      })
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [accessToken]);

  return (
    <div className="w-full h-full bg-mainColor flex justify-center items-center">
      <p className="text-bgColor">로그인 중~~</p>
    </div>
  );
}
