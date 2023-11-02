"use client"
import { Button } from "@mui/material";

const MoveButton = () => {
  return (<Button onClick={() => {
    window.location.href = "/"
  }} style={{
    backgroundColor: "#FFBC00",
    color: "#222222",
    fontSize: "24px",
    textTransform: 'none',
  }}>
    commitPixel 하러 가기 {"=>"}
    </Button>
    );
}

export default MoveButton;
