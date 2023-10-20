"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
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
const initialColor = { r: 255, g: 255, b: 255, alpha: 1 };
let lastChange = new Date();
const getInitialArray = (width, height) => __awaiter(void 0, void 0, void 0, function* () {
    if (fs.existsSync(imagePath)) {
        const image = yield sharp(imagePath).ensureAlpha().raw().toBuffer();
        return new Uint8ClampedArray(image);
    }
    else {
        console.log("Error reading data, creating new blank image");
        const blankImage = yield sharp({
            create: {
                width,
                height,
                channels: 4,
                background: initialColor,
            },
        }).png().raw().toBuffer();
        return blankImage;
    }
});
const downloadImage = (canvasArray) => __awaiter(void 0, void 0, void 0, function* () {
    const image = sharp(canvasArray, {
        raw: {
            width,
            height,
            channels: 4,
        },
    });
    yield image.toFile(imagePath);
});
const main = () => __awaiter(void 0, void 0, void 0, function* () {
    const canvasArray = yield getInitialArray(width, height);
    yield downloadImage(canvasArray);
    setInterval(() => __awaiter(void 0, void 0, void 0, function* () {
        const now = new Date();
        const needsToUpdate = now.getTime() - lastChange.getTime() < 1000;
        if (needsToUpdate) {
            console.log("Updating local image");
            yield downloadImage(canvasArray);
        }
    }), 1000);
    app.use(cors());
    app.use("/public", express.static(path.join(__dirname, "public")));
    app.get("/", (req, res) => {
        res.send("Up since " + start_date);
    });
    app.get("/connected-clients", (req, res) => {
        const sockets = [];
        io.sockets.sockets.forEach((socket) => {
            sockets.push({
                socket_id: socket.id,
                client_ip_address: socket.handshake.headers["x-real-ip"],
                connected_on: socket.handshake.time,
            });
        });
        const count = sockets.length;
        res.send({ count, sockets });
    });
    io.on("connection", (socket) => {
        console.log("New User connected to Socket: ", socket.id);
        socket.on("pixel", (pixel) => __awaiter(void 0, void 0, void 0, function* () {
            try {
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
            }
            catch (error) {
                console.log(error);
                socket.disconnect(true);
            }
        }));
        socket.on("disconnect", () => {
            console.log("User disconnected from Socket: ", socket.id);
        });
    });
    const port = Number(process.env.PORT) || 3001;
    server.listen(port, () => {
        console.log("Canvas socket server listening to PORT " + port);
    });
});
main();
