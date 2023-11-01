import { useSelector } from 'react-redux';
import { RootState } from '@/store';

const useFetchWithAuth = () => {
  const authorization = useSelector((state: RootState) => state.authorization.authorization);
  // TODO: 수정하기
  const baseURL = 'http://localhost:8082';

  const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
    const headers = {
      ...options.headers,
      'Content-Type': 'application/json',
      ...(authorization !== "" ? { "accesstoken": authorization } : {}),
    };

    console.log(headers);
    const reqURL = baseURL + url;

    try {
      const response = await fetch(reqURL, {...options, headers});
      return response;
    } catch (error) {
      console.error("Fetch error:", error);
      throw error;
    }
  };

  return fetchWithAuth;
};

export default useFetchWithAuth;