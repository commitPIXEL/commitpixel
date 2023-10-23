"use client"

import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';

import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';

import style from './boardBtn.module.css'

const BoardBtn = () => {

    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const [type, setType] = React.useState(0);
    const handleChange = (event: SelectChangeEvent) => {
        setType(Number(event.target.value));
    };

    return(
    <>
        <Button onClick={handleOpen} variant="contained" className="shadow-md rounded-lg bg-black text-white p-2 hover:bg-gray-800">건의하기</Button>

        <Modal
        className="fixed top-0 left-0 w-full h-full flex items-center justify-center"
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
      >
        <Box className="bg-white p-6 rounded shadow-md w-3/4 max-w-xl">
            <FormControl className="mb-4 w-full">
                <Select
                    value={type.toString()}
                    onChange={handleChange}
                    displayEmpty
                    inputProps={{ 'aria-label': 'Without label' }}
                    className="w-full border p-2 rounded"
                >
                    <MenuItem value={0}>건의하기</MenuItem>
                    <MenuItem value={1}>url 등록하기</MenuItem>    
                </Select>
            </FormControl>
            <div className="mb-4">
                <p className="text-xl font-bold mb-2">내용</p>
                <textarea className="w-full p-2 border rounded h-40" />
            </div>
            <div className="flex justify-end space-x-4">
                <Button onClick={handleClose} className="bg-blue-500 text-white px-4 py-2 rounded">제출</Button>
                <Button onClick={handleClose} className="bg-gray-500 text-white px-4 py-2 rounded">취소</Button>
            </div>
        </Box>
      </Modal>
    </>
    )
}

export default BoardBtn;