const PatchNote = () => {
  return (
    <div className="h-[55%]">
      <div className="text-4xl">패치노트</div>
      <div className="mt-2 h-[90%] overflow-y-scroll no-scrollbar p-2">
        <div>
          <div className="text-mainColor text-xl">ver 1.0.0</div>
          <div className="mt-2 flex flex-col">
            <div>- 공유 캔버스 기능</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PatchNote;