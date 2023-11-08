"use client";
import React from "react";
import { connect } from "react-redux";
import { pick } from "@/store/slices/colorSlice";
import { setTool } from "@/store/slices/toolSlice";
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
    setTool: (tool: string) => void;
};

const SketchPicker = dynamic(() => import("react-color").then((mod) => mod.SketchPicker), { ssr: false});

class Picker extends React.Component<Iprops> {
    state = {
        background: "#000000"
    };

    handleColorChange = (color: any) => {
        this.setState({background: color.hex});
        const colorData = {
            hex: color.hex,
            rgb: color.rgb
        };
        this.props.pick( colorData );
        this.props.setTool("painting");
    };

    componentDidUpdate(prevProps: Iprops): void {
        if(this.props.color && prevProps.color && (this.props.color.hex !== prevProps.color.hex)) {
            this.setState({background: this.props.color.hex});
        }
    };

    render() {
        return <SketchPicker
            width="100%"
            color={this.state.background}
            onChange={this.handleColorChange}
        />;
    }
}

// Redux state를 컴포넌트의 props로 매핑
const mapStateToProps = (state: StoreState): Icolor => state.color;

export default connect(mapStateToProps, { pick, setTool })(Picker);