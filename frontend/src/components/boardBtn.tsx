"use client";

import { useEffect, useState } from "react";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import Modal from '@mui/material/Modal';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import Loading from "./loading";
import useFetchWithAuth from "@/hooks/useFetchWithAuth";
import { useDispatch } from "react-redux";
import { setUrlInputOff, setUrlInputOn } from "@/store/slices/urlInputSlice";

const BoardBtn = () => {

    const customFetch = useFetchWithAuth();
    const dispatch = useDispatch();
    const [open, setOpen] = useState(false);
    const [type, setType] = useState(0);
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (open) {
          dispatch(setUrlInputOn());
        } else {
          dispatch(setUrlInputOff());
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
      }, [open]);

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleChange = (event: SelectChangeEvent) => {
        setType(Number(event.target.value));
    };
    const contentChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        setContent(event.target.value);
    };

    const handleSubmit = async () => {
        if (content.trim().length === 0) {
          window.alert("내용을 입력해주세요!");
          return;
        }
    
        setLoading(true);
        try {
          const data = await customFetch("/user/board", {
            method: "POST",
            body: JSON.stringify({ type: type, content: content }),
          });
          console.log(data);
          window.alert("건의사항이 제출되었습니다!!");
        } catch (err: unknown) {
          if (err instanceof Error) {
            console.error("Caught error message:", err.message);
            const status = parseInt(err.message, 10);
            if (status) {
              window.alert("인가된 url을 입력하셨습니다!");
            }
          } else {
            console.error("기대하지 않은 에러 발생:");
            console.error(err);
          }
        } finally {
          setLoading(false);
          setContent("");
        }
      };

    return (
      <>
        <Button
          onClick={handleOpen}
          variant="contained"
          className="!mt-6 rounded-lg bg-black text-white p-2 hover:bg-gray-800"
        >
          건의하기
        </Button>

        <Modal
          className="fixed top-0 left-0 w-full h-full flex items-center justify-center"
          open={open}
          onClose={handleClose}
          aria-labelledby="modal-modal-title"
          aria-describedby="modal-modal-description"
        >
          <Box className="bg-white p-6 rounded shadow-md w-3/4 max-w-xl">
            <FormControl className="!mb-4 w-full">
              <Select
                value={type.toString()}
                onChange={handleChange}
                displayEmpty
                inputProps={{ "aria-label": "Without label" }}
                className="w-full border p-2 rounded"
              >
                <MenuItem value={0}>건의하기</MenuItem>
                <MenuItem value={1}>url 등록하기</MenuItem>
              </Select>
            </FormControl>
            {type ? (
              <>
                <Loading open={loading} />
                <div className="mb-4 h-52">
                  <TextField
                    className="w-full"
                    placeholder="https://naver.com"
                    id="standard-start-adornment"
                    value={content}
                    onChange={contentChange}
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
            ) : (
              <>
                <Loading open={loading} />
                <div className="mb-4 h-52 ">
                  <p className="text-xl font-bold mb-2">내용</p>
                  <textarea
                    value={content}
                    onChange={contentChange}
                    className="w-full p-2 border rounded h-40"
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
            )}
          </Box>
        </Modal>
      </>
    );
}

export default BoardBtn;