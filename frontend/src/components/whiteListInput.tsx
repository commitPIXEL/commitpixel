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
    } catch (err) {
      console.error("Error:", err);
    } finally {
      setLoading(false);
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
