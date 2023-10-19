import { useSelector } from 'react-redux';
import { RootState } from '@/store';

export default function FetchWithAuth(url: string, options: RequestInit = {}) {
    const authorization = useSelector((state: RootState) => state.authorization.authorization);

    const baseURL =  'http://localhost:8080'
    const headers = {...options.headers, ...(authorization !== "") ? {"Authorization": {authorization}} : {}};
    const reqURL = baseURL + url;

    return fetch(reqURL, {...options, ...headers});
}