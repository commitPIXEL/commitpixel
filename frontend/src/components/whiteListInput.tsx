"use client"

import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';

const WhiteListInput: React.FC<{handleClose: () => void}> = ({handleClose}) => {
    return(
        <>
            <div className="mb-4 h-52">
                <TextField
                    className="w-full"
                    placeholder='https://naver.com'
                    id="standard-start-adornment"
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
                <Button onClick={handleClose} className="bg-blue-500 text-white px-4 py-2 rounded">제출</Button>
                <Button onClick={handleClose} className="bg-gray-500 text-white px-4 py-2 rounded">취소</Button>
            </div>
        </>
    )
}

export default WhiteListInput;