"use client"

import React, {useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import Loading from './loading';
import FetchWithAuth from '@/app/utils/fetchWithAuth';

const WhiteListInput: React.FC<{handleClose: () => void, reqMethod: string, reqUrl: string}> = ({handleClose, reqMethod, reqUrl}) => {
    const [url, setUrl] = useState('');
    const [loading, setLoading] = useState(false);
    
    const urlChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setUrl(event.target.value);
    }

    const handleSubmit = async () => {
        if(url.trim().length === 0) return;

        setLoading(true);
        await FetchWithAuth(reqUrl, {method: reqMethod, body: JSON.stringify({content: url})})
        .then(res => {
            console.log("res 값")
            console.log(res);
        }).catch(err => {
            console.log("err 발생");
            console.log(err);
        }).finally(() => {
            setLoading(false);
            window.alert("건의사항이 제출되었습니다!!");
        }); 
    }

    return(
        <>
            <Loading open={loading} />
            <div className="mb-4 h-52">
                <TextField
                    className="w-full"
                    placeholder='https://naver.com'
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
                <Button onClick={handleSubmit} className="bg-blue-500 text-white px-4 py-2 rounded">제출</Button>
                <Button onClick={handleClose} className="bg-gray-500 text-white px-4 py-2 rounded">취소</Button>
            </div>
        </>
    )
}

export default WhiteListInput;