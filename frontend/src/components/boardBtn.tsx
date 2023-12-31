"use client";

import { useState } from "react";
import Button from '@mui/material/Button';
import BoardInput from "./boardInput";

const BoardBtn = () => {

    const [open, setOpen] = useState(false);

    const handleOpen = () => setOpen(true);
  
    return (
      <>
        <Button
          onClick={handleOpen}
          variant="contained"
          className="!mt-1 rounded-lg bg-black text-white p-3 hover:bg-gray-800"
        >
          건의하기
        </Button>

        <BoardInput open={open} setOpen={setOpen} />
      </>
    );
}

export default BoardBtn;