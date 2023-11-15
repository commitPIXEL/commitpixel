import { RootState } from '@/store';
import { apiUrl } from '@/app/browser/config';
import { useDispatch } from "react-redux";
import useFetchAuth from './useFetchAuth';
import { getUserInfo, updateUserPixel } from '@/store/slices/userSlice';
import { IUserInfo, IUserPixel } from '@/interfaces/browser';


const useFetchUser = async () => {
    const dispatch = useDispatch();
    const customFetch = useFetchAuth();

    try {
        const resUser = await customFetch("/user/");
        const userData: IUserInfo = await resUser.json();
        dispatch(getUserInfo(userData));

        const resPixel = await customFetch("/user/refreshinfo");
        const pixelData: IUserPixel = await resPixel.json();
        dispatch(updateUserPixel(pixelData));

        return {success: true, userData, pixelData};
      } catch (err) {
        console.error("Error:", err);
      }
  
      try {
        const resPixel = await customFetch("/user/refreshinfo");
        const pixelData: IUserPixel = await resPixel.json();
  
        dispatch(updateUserPixel(pixelData));
      } catch (err) {
        return { success: false, error: err };
      }
};

export default useFetchUser;
