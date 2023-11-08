import { RootState } from "@/store";
import { setSnackbarOff } from "@/store/slices/snackbarSlice";
import { Snackbar } from "@mui/material";
import { memo } from "react";
import { useDispatch, useSelector } from "react-redux";

const BrowserSnackBar = memo(({ urlData }: {
  urlData: {
    githubNickname: string,
    url: string,
  },
}) => {
  const dispatch = useDispatch();
  const isSnackbarOpen = useSelector((state: RootState) => state.snackbar.isOpen);
  if(!urlData?.url && !urlData?.githubNickname) {
    dispatch(setSnackbarOff());
    return;
  }

  return (
      <Snackbar open={isSnackbarOpen}>
        <div className="bg-mainColor rounded p-4 max-w-[1000px] h-fit break-words">
          <div className="cursor-pointer text-lg mb-4 text-bgColor">{urlData?.githubNickname}</div>
          <div
            className="cursor-pointer text-sm"
            onClick={() => {
              window.open(urlData.url, "_blank");
            }}
          >
            {urlData?.url}
          </div>
        </div>
      </Snackbar>
  );
});

export { BrowserSnackBar };
