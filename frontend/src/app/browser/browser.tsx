import CanvasContainer from "./canvas/canvasContainer";
import Nav from "./nav";
import Sidebar from "./sidebar/sidebar";

const Browser = () => {
  return (
    <main className="w-screen h-screen bg-bgColor">
          <Nav />
          <div className="grid grid-cols-4 w-full h-[92%] place-items-center">
            <CanvasContainer canvasHeight={1024} canvasWidth={1024} />
            <Sidebar />
          </div>
    </main>
  );
}

export default Browser;