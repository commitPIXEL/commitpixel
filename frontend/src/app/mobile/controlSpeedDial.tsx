import Box from "@mui/material/Box";
import Backdrop from "@mui/material/Backdrop";
import SpeedDial from "@mui/material/SpeedDial";
import SpeedDialIcon from "@mui/material/SpeedDialIcon";
import SpeedDialAction from "@mui/material/SpeedDialAction";
import TimelapseModal from "../browser/sidebar/timelapse";
import KakaoShare from "@/components/kakaoShare";
import BoardBtn from "@/components/boardBtn";
import { useState } from "react";
import { ISpeedDialProps } from "@/interfaces/browser";
import { InstallPWA } from "../browser/nav/navBtn";
import ToPixelBtn from "@/components/toPixelBtn";

const actions = [
  {
    icon: <ToPixelBtn key="ToPixelBtn" />,
    name: "이미지 픽셀화",
  },
  { icon: <TimelapseModal key="TimelapseModal" />, name: "타입랩스" },
  { icon: <KakaoShare key="KakaoShare" />, name: "카카오 공유" },
  { icon: <BoardBtn key="BoardBtn" />, name: "건의하기" },
  { icon: <InstallPWA key="InstallPWA" />, name: "App 다운" },
];

const ControlSpeedDial = ({ hidden }: ISpeedDialProps) => {
  const [open, setOpen] = useState(false);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <Box
      sx={{
        position: "relative",
        zIndex: 1,
      }}
    >
      <Backdrop open={open} />
      <SpeedDial
        ariaLabel="SpeedDial tooltip example"
        hidden={hidden}
        sx={{
          position: "fixed",
          bottom: 16,
          right: 16,
          "& button": {
            background: "black",
            color: "white",
            "&:hover": {
              background: "black",
            },
          },
        }}
        icon={<SpeedDialIcon />}
        onClose={handleClose}
        onOpen={handleOpen}
        open={open}
      >
        {actions.map((action) => (
          <SpeedDialAction
            key={action.name}
            icon={action.icon}
            tooltipTitle={action.name}
            tooltipOpen
            className="!bg-white !text-black"
          />
        ))}
      </SpeedDial>
    </Box>
  );
};

export default ControlSpeedDial;
