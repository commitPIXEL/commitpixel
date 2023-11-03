import React, { useState } from "react";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import TextField from "@mui/material/TextField";
import CampaignIcon from '@mui/icons-material/Campaign';
import InputAdornment from "@mui/material/InputAdornment";
import Loading from "./loading";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";

const SolvedacBtn = () => {
  const customFetch = useFetchWithAuth();
  const [loading, setLoading] = useState(false);
  const [id, setId] = useState("");
  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const idChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setId(event.target.value);
  };

  const handleSubmit = async () => {
    if (id === "" || id.trim().length === 0) {
        window.alert("ID를 입력해주세요!");
        return;
    }

    try {
        setLoading(true);
        const resFromUser = await customFetch("/user/solvedac/auth", {
          method: "PATCH",
          body: JSON.stringify({ solvedAcId: id }),
        });

        console.log(resFromUser);
        console.log(resFromUser.json())
        window.alert("solvedac 연동 성공!!");
    } catch (err) {
        console.error("Error:", err);
        window.alert("solvedac 연동 실패!!");
    } finally {
      setLoading(false);
      setOpen(false);
    }
  };

  return (
    <div className="w-full">
      <Button
        onClick={handleOpen}
        variant="contained"
        sx={{ bgcolor: 'green', '&:hover': { bgcolor: 'darkgreen' } }}
        className="shadow-md rounded-lg text-white p-2 w-full"
      >
        solved.ac 연동
      </Button>

      <Modal
        className="fixed top-0 left-0 w-full h-full flex items-center justify-center"
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box className="bg-white p-6 rounded shadow-md w-3/4 max-w-xl">
          <Loading open={loading} />

          <div className="flex items-center space-x-2 mb-4">
            <CampaignIcon />
            <span>
              <a
                href="https://solved.ac"
                target="_blank"
                rel="noopener noreferrer"
                className="text-green-600 hover:text-green-800"
              >
                solved.ac
              </a>
              의 프로필 메시지에 &quot;commitpixel.com&quot; 입력 필수
            </span>
          </div>
          <div className="mb-4">
            <TextField
              className="w-full"
              placeholder="solved.ac 아이디 입력"
              id="standard-start-adornment"
              onChange={idChange}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <span className="text-gray-300">ID</span>
                  </InputAdornment>
                ),
              }}
              variant="standard"
            />
          </div>
          <div className="flex justify-end space-x-4">
            <Button
              onClick={handleSubmit}
              className="px-4 py-2 rounded"
            >
              제출
            </Button>
            <Button
              onClick={handleClose}
              className="px-4 py-2 rounded"
            >
              취소
            </Button>
          </div>
        </Box>
      </Modal>
    </div>
  );
};

export default SolvedacBtn;