const toHex = (rgbData: number) => {
  let hex = rgbData.toString(16);
  return hex.length == 1 ? "0" + hex : hex;
};

const rgbtoHex = (r: number, g: number, b: number) => {
  return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
};

export default rgbtoHex;