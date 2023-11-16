import { io, Socket } from "socket.io-client";
import { socketUrl} from "../app/config";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { RootState } from "@/store";
import { SOCKET_CONNECTION_ERROR, SOCKET_DISCONNECTED, SOCKET_NOT_USER, SOCKET_URL_ERROR, VISITOR } from "@/constants/message";

const useSocket = () => {
  const [socket, setSocket] = useState<Socket>();
  const userNickname = useSelector((state: RootState) => state.user.githubNickname);
  const accessToken = useSelector((state: RootState) => state.authorization.authorization);

  const connectToSocket = () => {
    if(socketUrl) {
      const socket = io(socketUrl, {
        transports: ["websocket"],
        reconnection: false,
        query: {
          "Authorization": accessToken || "",
          "githubNickname": userNickname || VISITOR,
        },
      });
      
      socket.on("connect", () => {
        setSocket(socket);
      });

      socket.on("isNotUser", () => {
        setSocket(undefined);
        alert(SOCKET_NOT_USER);
      });
      
      socket.on("disconnect", (error) => {
        setSocket(undefined);
        alert(SOCKET_DISCONNECTED + error);
      });
      
      socket.on("connect_error", (error) => {
        setSocket(undefined);
        alert(SOCKET_CONNECTION_ERROR + error);
      });
      return socket;
    } else {
      alert(SOCKET_URL_ERROR);
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
