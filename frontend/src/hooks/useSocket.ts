import { io, Socket } from "socket.io-client";
import { socketUrl} from "../app/config";
import { useEffect, useState } from "react";

const useSocket = () => {
  const [socket, setSocket] = useState<Socket>();

  const connectToSocket = () => {
    if(true && socketUrl) {
      const socket = io(socketUrl, {
        transports: ["websocket"],
        reconnection: false,
        query: {
          "Authorization": "",
          "githubNickname": "Visitor"
        },
      });
      
      socket.on("connect", () => {
        setSocket(socket);
      });
      
      socket.on("disconnect", (error) => {
        setSocket(undefined);
        alert("Socket disconnected: " + error);
      });
      
      socket.on("connect_error", (error) => {
        setSocket(undefined);
        alert("Error connecting to socket");
      });
      return socket;
    } else {
      alert("socketUrl is empty");
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
