import { useEffect, useState } from "react";
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Modal from '@mui/material/Modal';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select, { SelectChangeEvent } from '@mui/material/Select';
import BoardInput from './boardInput';
import WhiteListInput from './whiteListInput';
import { useDispatch } from "react-redux";
import { setUrlInputOff, setUrlInputOn } from "@/store/slices/urlInputSlice";

const BoardBtn = () => {

    const dispatch = useDispatch();
    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const [type, setType] = useState(0);

    useEffect(() => {
      if (open) {
        dispatch(setUrlInputOn());
      } else {
        dispatch(setUrlInputOff());
      }
      // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [open]);

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
                {type ? <WhiteListInput handleClose={handleClose} reqMethod='POST' reqUrl='/user/board' type={type} /> : <BoardInput handleClose={handleClose} reqMethod='POST' reqUrl='/user/board' type={type} />}
            </Box>
        </Modal>
    </>
    )
}

export default BoardBtn;