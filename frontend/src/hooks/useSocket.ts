import { io, Socket } from "socket.io-client";
import { socketUrl} from "../app/config";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { RootState } from "@/store";
import useFetchUser from "./useFetchUser";

const useSocket = () => {
  const [socket, setSocket] = useState<Socket>();
  const userNickname = useSelector((state: RootState) => state.user.githubNickname);
  const accessToken = useSelector((state: RootState) => state.authorization.authorization);
  const setUser = useFetchUser();

  const connectToSocket = () => {
    if(accessToken && !userNickname) {
      setUser;
      console.log("어세스토큰이 있지만 닉네임이 없음 from useSocket");
    }
    if(true && socketUrl) {
      const socket = io(socketUrl, {
        transports: ["websocket"],
        reconnection: false,
        query: {
          "Authorization": accessToken || "",
          "githubNickname": userNickname || "Visitor",
        },
      });
      
      socket.on("connect", () => {
        setSocket(socket);
      });

      socket.on("isNotUser", () => {
        setSocket(undefined);
        alert("올바른 사용자 접속이 아닙니다!");
      });
      
      socket.on("disconnect", (error) => {
        setSocket(undefined);
        alert("소켓 연결 해제: " + error);
      });
      
      socket.on("connect_error", (error) => {
        setSocket(undefined);
        alert("소켓 연결 에러");
      });
      return socket;
    } else {
      alert("소켓 URL이 올바르지 않음");
    }
  }
    
  useEffect(() => {
    const socket = connectToSocket();
    return () => {
      socket?.removeAllListeners("connect");
      socket?.removeAllListeners("disconnect");
      socket?.removeAllListeners("connect_error");
    };
  }, []);
  return { socket, setSocket, connectToSocket};
};
  
export default useSocket;
