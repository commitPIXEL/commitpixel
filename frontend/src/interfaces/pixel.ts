export interface ImyPaintPixel {
  x: number, 
  y: number, 
  color: {
    r: number,
    g: number,
    b: number,
  }, 
  user: IurlInfo,
};

export type IothersPaintPixel = [number, number, number, number, number];

export interface IurlInfo {
  url: string, 
  userId: string
}