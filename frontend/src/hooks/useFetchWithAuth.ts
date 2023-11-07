import { useSelector } from 'react-redux';
import { RootState } from '@/store';

const useFetchWithAuth = () => {
  const authorization = useSelector((state: RootState) => state.authorization.authorization);
  // const baseURL = 'http://localhost:8082'; // 로컬용
  const baseURL = 'https://dev.commitpixel.com/api'; // 개발용
  // const baseURL = 'https://commitpixel.com/api'; // 배포용

  const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
    const headers = {
      ...options.headers,
      'Content-Type': 'application/json',
      ...(authorization !== "" ? { "accesstoken": authorization } : {}),
    };

    console.log(headers);
    const reqURL = baseURL + url;

    const response = await fetch(reqURL, {...options, headers});
    if(!response.ok) {
      throw new Error(`${response.status}`);
    }
    return response;
  };

  return fetchWithAuth;
};

export default useFetchWithAuth;
