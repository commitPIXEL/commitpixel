/**
 * Canvas 상태를 저장하고 갱신하는 Socket Server
 * 
 * 반드시 npm run build 한 후 npm run start 할 것!
 */

const express = require("express");
const https = require("https");
const http = require("http");
const fs = require("fs");
const path = require("path");
const sharp = require("sharp");
const cors = require("cors");
const app = express();
const server = http.createServer(app);

const io = require("socket.io")(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
    credentials: true,
  },
});

const width = 1024;
const height = 1024;
const start_date = new Date().toLocaleString();
const imagePath = path.join(__dirname, "public/place.png");
const initialColor = {r: 255, g: 255, b: 255, alpha: 1};
let lastChange = new Date();

// 현재 그림 상태를 가져옴
// 만약 에러가 생기거나 그림 상태가 없다면 빈 그림 이미지를 가져옴
const getInitialArray = async (width: number, height: number) => {
  if (fs.existsSync(imagePath)) {
    const image = await sharp(imagePath).ensureAlpha().raw().toBuffer();
    return new Uint8ClampedArray(image);
  } else {
    console.log("Error reading data, creating new blank image");
    const blankImage = await sharp({
      create: {
        width,
        height,
        channels: 4,
        background: initialColor,
      },
    }).png().raw().toBuffer();
    return blankImage;
  }
};

// 이미지를 다운받음
const downloadImage = async (canvasArray: Buffer | Uint8ClampedArray) => {
  const image = sharp(canvasArray, {
    raw: {
      width,
      height,
      channels: 4,
    },
  });
  await image.toFile(imagePath);
};

// 메인 기능
const main = async () => {
  const canvasArray = await getInitialArray(width, height);
  await downloadImage(canvasArray);

  setInterval(async () => {
    const now = new Date();
    const needsToUpdate = now.getTime() - lastChange.getTime() < 1000;

    if(needsToUpdate) {
      console.log("Updating local image");
      await downloadImage(canvasArray);
    }
  }, 1000);

  app.use(cors());

  app.use("/public", express.static(path.join(__dirname, "public")));

  // 서버 시작 시간 리턴 api
  app.get("/", (req:any, res:any) => {
    res.send("Up since " + start_date);
  });

  // 접속된 유저를 구하는 api
  app.get("/connected-clients", (req: any, res: any) => {
    const sockets: {
      socket_id: string;
      client_ip_address: string | undefined;
      connected_on: string;
    }[] = [];
    io.sockets.sockets.forEach((socket: any) => {
      sockets.push({
        socket_id: socket.id,
        client_ip_address: socket.handshake.headers["x-real-ip"] as string,
        connected_on: socket.handshake.time,
      });
    });
    const count = sockets.length;
    res.send({count, sockets});
  });

  // 소켓 연결 시
  io.on("connection", (socket: any) => {
    console.log("New User connected to Socket: ", socket.id);

    // 캔버스에서 픽셀을 색칠했을 때 갱신
    socket.on("pixel", async (pixel: any) => {
      try {
        // TODO: URL과 UserID 추가하기
        const [x, y, r, g, b] = pixel;
        const index = (width * y + x) * 4;

        const original_r = canvasArray[index];
        const original_g = canvasArray[index + 1];
        const original_b = canvasArray[index + 2];

        if (original_r !== r || original_g !== g || original_b !== b) {
          canvasArray[index] = r;
          canvasArray[index + 1] = g;
          canvasArray[index + 2] = b;
          canvasArray[index + 3] = 255;
          lastChange = new Date();
          socket.broadcast.emit("pixel", pixel);
        }
      } catch (error) {
        console.log(error);
        socket.disconnect(true);
      }
    });

    // 접속 종료 시
    socket.on("disconnect", () => {
      console.log("User disconnected from Socket: ", socket.id);
    });
  });

  const port = Number(process.env.PORT) || 3001;

  server.listen(port, () => {
    console.log("Canvas socket server listening to PORT " + port);
  });
}

main();