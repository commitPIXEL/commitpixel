import Image from "next/image";

const Nav = () => {
  return (
    <div className="bg-mainColor w-full h-[8%] flex items-center">
      <div className="relative w-[5%] h-full">
        <Image src="/icon.png" fill style={{
          objectFit: 'contain',
        }} sizes="80px" priority alt="commit Pixel logo" />
      </div>
      <div className="flex flex-col justify-center">
        <div className="text-xl font-bold">commit</div>
        <div className="text-xl font-bold">Pixel</div>
      </div>
    </div>
  );
}

export default Nav;