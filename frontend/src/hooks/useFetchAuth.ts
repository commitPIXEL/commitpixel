import { useSelector } from 'react-redux';
import { RootState } from '@/store';
import { apiUrl } from '@/app/browser/config';

const useFetchAuth = () => {
  const authorization = useSelector((state: RootState) => state.authorization.authorization);

  const fetchAuth = async (url: string, options: RequestInit = {}) => {
    const headers = {
      ...options.headers,
      "Content-Type": "application/json",
      ...(authorization !== "" ? { accesstoken: authorization } : {}),
    };

    const reqURL = apiUrl + url;

    const response = await fetch(reqURL, {...options, headers});
    if(!response.ok) {
      throw new Error(`${response.status}`);
    }
    return response;
  };

  return fetchAuth;
};

export default useFetchAuth;
