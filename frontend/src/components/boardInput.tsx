"use client"

import Button from '@mui/material/Button';

const BoardInput: React.FC<{handleClose: () => void}> = ({handleClose}) => {
    return(
        <>
            <div className="mb-4 h-52 ">
                <p className="text-xl font-bold mb-2">내용</p>
                <textarea className="w-full p-2 border rounded h-40" />
            </div>
            <div className="flex justify-end space-x-4">
                <Button onClick={handleClose} className="bg-blue-500 text-white px-4 py-2 rounded">제출</Button>
                <Button onClick={handleClose} className="bg-gray-500 text-white px-4 py-2 rounded">취소</Button>
            </div>
        </>
    )
}

export default BoardInput;