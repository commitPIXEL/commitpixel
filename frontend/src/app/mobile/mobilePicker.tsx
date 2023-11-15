import React from "react";
import { connect } from "react-redux";
import { pick } from "@/store/slices/colorSlice";
import dynamic from "next/dynamic";

interface StoreState {
  color: Icolor;
};

interface Icolor {
  hex: string;
  rgb: {
      r: number;
      g: number;
      b: number;
      a: number;
  };
};

interface Iprops {
  color?: Icolor;
  pick: (color: Icolor) => void;
};

const CirclePicker = dynamic(() => import("react-color").then((mod) => mod.CirclePicker), { ssr: false });
const SliderPicker = dynamic(() => import("react-color").then((mod) => mod.SliderPicker), { ssr: false });
const colors = ["#f44336", "#e91e63", "#9c27b0", "#673ab7", "#3f51b5", "#2196f3", "#03a9f4", "#00bcd4", "#009688", "#4caf50", "#8bc34a", "#cddc39", "#ffeb3b", "#ffc107", "#ff9800", "#ff5722", "#795548", "#607d8b", "#FFFFFF", "#969696", "#000000"]

class MobilePicker extends React.Component<Iprops> {
  state = {
    background: "#000000"
  };


  handleColorChange = (color: any) => {
    this.setState({background: color.hex});
    const colorData = {
      hex: color.hex,
      rgb: color.rgb
    };
    this.props.pick( colorData);
  };

  render() {
    return (
      <div className="w-full h-fit flex justify-center">
        <div className="mt-8 w-[80%] flex flex-col p-2 bg-slate-200 rounded-lg">
          <div className="mb-4 pt-4 w-full flex justify-center items-center">
            <CirclePicker
              colors={colors}
              width="100%"
              color={this.state.background}
              onChange={this.handleColorChange}
            />
          </div>
          <SliderPicker
            color={this.state.background}
            onChange={this.handleColorChange}
          />
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state: StoreState): Icolor => state.color;

export default connect(mapStateToProps, { pick })(MobilePicker);