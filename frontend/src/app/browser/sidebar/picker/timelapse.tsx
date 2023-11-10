import axios from "axios";
import { apiUrl } from "../../config";
import { useState } from "react";
import { CircularProgress } from "@mui/material";

const Timelapse = () => {
  const [isLoading, setIsLoading] = useState(false);
  const handleTimelapseClick = () => {
    setIsLoading(true);
    axios.get(apiUrl + "/image/timelapse", {
      responseType: "blob"
    }).then((res) => {
      const url = window.URL.createObjectURL(new Blob([res.data], { type: "image/gif"}));

      const link = document.createElement("a");
      link.href = url;
      const date = new Date();
      link.setAttribute("download", `timelapse_${date.toISOString().slice(0, 10)}.gif`);

      document.body.appendChild(link);
      link.click();
      setIsLoading(false);
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    }).catch((err) => {
      console.error(err);
      alert("타임랩스 가져오기 실패!");
    })
  };

  return (
    <button onClick={handleTimelapseClick} className="drop-shadow-md w-[45%] bg-mainColor rounded min-h-[40px] flex justify-center items-center" type="button">{isLoading ? <CircularProgress className="p-2" /> : "타임랩스"}</button>
  );
};

export default Timelapse;
