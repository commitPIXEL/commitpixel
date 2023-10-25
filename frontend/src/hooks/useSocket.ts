import { io, Socket } from "socket.io-client";
import { socketUrl} from "../app/config";
import { useEffect, useState } from "react";

const useSocket = () => {
  const [socket, setSocket] = useState<Socket>();

  const connectToSocket = () => {
    if(true && socketUrl) {
      const socket = io("socketUrl", {
        reconnection: false,
      });
      
      socket.on("connect", () => {
        console.log("Socket connected", socket);
        setSocket(socket);
      });
      
      socket.on("disconnect", (error) => {
        console.error("Socket disconnected: ", error);
        setSocket(undefined);
        alert("Socket disconnected: " + error);
      });
      
      socket.on("connect_error", (error) => {
        console.log("Error connecting to socket:", error);
        setSocket(undefined);
        alert("Error connecting to socket");
      });
      return socket;
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