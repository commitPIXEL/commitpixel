"use client";
import React from "react";
import { SketchPicker } from "react-color";
import { connect } from "react-redux";
import { pick } from "../../../store/slices/colorSlice";

// Redux state와 actions의 타입을 정의합니다.
interface StateProps {
    colorFromRedux: string; // state.color의 타입을 가정합니다. 실제 타입으로 교체해야 합니다.
}

interface DispatchProps {
    pick: (color: object) => void;
}

// 전체 props의 타입을 결합합니다.
type Props = StateProps & DispatchProps;

class Picker extends React.Component<Props> {
    state = {
        background: "000",
    };

    handleChange = (color: any) => {
        this.setState({background: color});
        const colorData = {
            hex: color.hex,
            rgb: color.rgb
        }
        this.props.pick( colorData );
    };

    render() {
        return <SketchPicker
            color={this.state.background}
            onChange={this.handleChange}
        />;
    }
}

// Redux state를 컴포넌트의 props로 매핑
const mapStateToProps = (state: any): StateProps => ({
    colorFromRedux: state.color
});

export default connect<StateProps, DispatchProps>(mapStateToProps, { pick })(Picker);