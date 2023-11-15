import { useDispatch } from "react-redux";
import useFetchAuth from './useFetchAuth';
import { getUserInfo, updateUserPixel } from '@/store/slices/userSlice';


const useFetchUser = async () => {
    const dispatch = useDispatch();
    const customFetch = useFetchAuth();

    try {
      const [resUser, resPixel] = await Promise.all([
          customFetch("/user/"),
          customFetch("/user/refreshinfo")
      ]);

      const [userData, pixelData] = await Promise.all([
          resUser.json(),
          resPixel.json()
      ]);

      dispatch(getUserInfo(userData));
      dispatch(updateUserPixel(pixelData));

      return { success: true, userData, pixelData };
  } catch (err) {
      console.error("Error:", err);
      return { success: false, error: err };
  }
};

export default useFetchUser;
