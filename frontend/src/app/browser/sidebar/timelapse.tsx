import React, { useState } from "react";
import axios from "axios";
import { apiUrl } from "../config";
import Modal from '@mui/material/Modal';
import Box from '@mui/material/Box';
import CircularProgress from "@mui/material/CircularProgress";

const TimelapseModal = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [open, setOpen] = useState(false);
  const [gifSrc, setGifSrc] = useState("");

  const handleTimelapseClick = () => {
    setIsLoading(true);
    axios.get(apiUrl + "/image/timelapse", {
      responseType: "blob"
    }).then((res) => {
      const url = window.URL.createObjectURL(new Blob([res.data], { type: "image/gif" }));
      setGifSrc(url);
      setOpen(true);
      setIsLoading(false);
    }).catch((err) => {
      console.error(err);
      alert("타임랩스 가져오기 실패!");
      setIsLoading(false);
    });
  };

  const handleClose = () => {
    setOpen(false);
    window.URL.revokeObjectURL(gifSrc);
  };

  return (
    <>
      <button onClick={handleTimelapseClick} className="drop-shadow-md w-[45%] bg-mainColor rounded min-h-[40px] flex justify-center items-center" type="button">
        {isLoading ? <CircularProgress className="p-2" /> : "타임랩스 보기"}
      </button>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="timelapse-modal-title"
        aria-describedby="timelapse-modal-description"
        className="flex items-center justify-center fixed inset-0 bg-black bg-opacity-25"
      >
        <Box className="bg-white rounded-lg shadow-xl h-[95%] aspect-square overflow-auto">
          {isLoading ? (
            <CircularProgress />
          ) : (
            <img src={gifSrc} alt="Timelapse GIF" className="w-full h-full" />
          )}
        </Box>
      </Modal>
    </>
  );
};

export default TimelapseModal;
