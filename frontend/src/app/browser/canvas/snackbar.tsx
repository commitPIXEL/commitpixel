import { IurlInfo } from "@/interfaces/pixel";
import { RootState } from "@/store";
import { Snackbar } from "@mui/material";
import { memo } from "react";
import { useSelector } from "react-redux";

const BrowserSnackBar = memo(({ urlData }: {urlData: IurlInfo}) => {
  const isSnackbarOpen = useSelector((state: RootState) => state.snackbar.isOpen);

  return (
      <Snackbar open={isSnackbarOpen}>
        <div className="bg-mainColor rounded p-4 max-w-[1000px] h-fit break-words">
          { !urlData?.githubNickname ? <div>No Pixel Data</div> : <div className="cursor-pointer text-lg mb-4 text-bgColor">{urlData?.githubNickname}</div>}
          {!urlData?.url ? null : <div
            className="cursor-pointer text-sm"
            onClick={() => {
              window.open(urlData.url, "_blank");
            }}
          >
            {urlData?.url}
          </div>}
        </div>
      </Snackbar>
  );
});


BrowserSnackBar.displayName = 'BrowserSnackBar';

export { BrowserSnackBar };
