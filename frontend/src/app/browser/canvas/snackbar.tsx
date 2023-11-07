import { Snackbar } from "@mui/material";

const BrowserSnackBar = ({ open, handleClose, urlData }: {
  open: boolean,
  handleClose: any,
  urlData: {
    userId: string,
    url: string,
  },
}) => {
  return (
      <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
        <div className="bg-mainColor rounded p-4">
          <div className="cursor-pointer">{urlData?.userId}</div>
          <div
            className="cursor-pointer"
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
