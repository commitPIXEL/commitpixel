"use client";

import React, { useState } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import Loading from "./loading";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";

const WhiteListInput: React.FC<{
  handleClose: () => void;
  reqMethod: string;
  reqUrl: string;
  type: number;
}> = ({ handleClose, reqMethod, reqUrl, type }) => {
  const [url, setUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const customFetch = useFetchWithAuth();

  const urlChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setUrl(event.target.value);
  };

  const handleSubmit = async () => {
    if (url.trim().length === 0) return;

    setLoading(true);
    try {
      const data = await customFetch(reqUrl, {
        method: reqMethod,
        body: JSON.stringify({ type: type, content: url }),
      });
      console.log(data);
      window.alert("url 인가 요청이 제출되었습니다!!");
    } catch (err: unknown) {
      if (err instanceof Error) {
        console.error("Caught error message:", err.message);
        if ("status" in err && (err as any).status === 409) {
          window.alert("인가된 url을 입력하셨습니다!");
        }
      } else {
        console.error("기대하지 않은 에러 발생:");
        console.error(err);
      }
    } finally {
      setLoading(false);
      setUrl("");
      console.log("통신 끝!");
    }
  };

  return (
    <>
      <Loading open={loading} />
      <div className="mb-4 h-52">
        <TextField
          className="w-full"
          placeholder="https://naver.com"
          id="standard-start-adornment"
          onChange={urlChange}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <span className="text-gray-300">URL</span>
              </InputAdornment>
            ),
          }}
          variant="standard"
        />
      </div>
      <div className="flex justify-end space-x-4">
        <Button
          onClick={handleSubmit}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          제출
        </Button>
        <Button
          onClick={handleClose}
          className="bg-gray-500 text-white px-4 py-2 rounded"
        >
          취소
        </Button>
      </div>
    </>
  );
};

export default WhiteListInput;
