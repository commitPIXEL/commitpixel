import axios from "axios";
import { useCallback, useRef } from "react";
import { apiUrl } from "../../config";

const ImageToPixel = () => {
  const inputRef = useRef<HTMLInputElement | null>(null);

  const handleUploadImage = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) {
      return;
    }
    const image = e.target.files[0];
    let formData = new FormData();
    formData.append('image', e?.target.files[0]);
  
    axios.post(apiUrl + "/image/convert?type=1", {
      file: formData
    }, {
      responseType: "arraybuffer",
      headers: {
        "Content-Type": "multipart/form-data"
      }
    }).then((res) => {
      console.log(res);
      const blob = new Blob([res.data], {type: "image/jpeg"});
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      const date = new Date();
      link.setAttribute("download", `pixeled_image_${date.toISOString().slice(0, 10)}.jpeg`);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    }).catch((err) => {
      console.error(err);
      alert("이미지 픽셀화 실패!");
    });
  }, []);

  return (
    <>
      <label htmlFor="image_to_pixel" className="cursor-pointer drop-shadow-md w-[45%] bg-mainColor rounded min-h-[40px] flex justify-center items-center">
        <div>
          이미지 픽셀화
        </div>
      </label>
      <input className="hidden" ref={inputRef} onChange={handleUploadImage} id="image_to_pixel" type="file" accept="image/jpg, image/png, image/jpeg"></input>
    </>
  );
}

export default ImageToPixel;
