import { Snackbar } from "@mui/material";

const BrowserSnackBar = ({ open, handleClose, urlData }: {
  open: boolean,
  handleClose: any,
  urlData: {
    githubNickname: string,
    url: string,
  },
}) => {
  if(!urlData?.githubNickname && !urlData.url) {
    return;
  }
  return (
      <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
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
}

export { BrowserSnackBar };
