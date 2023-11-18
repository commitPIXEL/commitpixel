"use client";

import { useState } from "react";
import Button from '@mui/material/Button';
import FeedbackOutlinedIcon from '@mui/icons-material/FeedbackOutlined';
import BoardInput from "./boardInput";

const BoardBtn = () => {

    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    const isMobileView = window.innerWidth < 769 && window.innerWidth <= window.innerHeight;
  
    return (
      <>
        {isMobileView ? (
          <button className="rounded-full" onClick={handleOpen}>
            <FeedbackOutlinedIcon />
          </button>
        ) : (
          <Button
            onClick={handleOpen}
            variant="contained"
            className="!mt-1 rounded-lg bg-black text-white p-3 hover:bg-gray-800"
          >
            건의하기
          </Button>
        )}

        <BoardInput open={open} setOpen={setOpen} />
      </>
    );
}

export default BoardBtn;