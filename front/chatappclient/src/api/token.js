import axios from "axios";
import store from "../store"; 
import { login, logout } from "../store/userSlice";
import DecodingInfo from './DecodingInfo';

export const privateApi = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
});

// 요청 인터셉터 설정
privateApi.interceptors.request.use(
    (config) => {
        const state = store.getState();
        const accessToken = state.user.accessToken; // Redux에서 accessToken 가져오기

        if (accessToken) {
            config.headers["Authorization"] = `${accessToken}`;
        }

        return config;
    }, (error) => {
        return Promise.reject(error)
    }
);

// refresh token api
privateApi.interceptors.response.use(
    (response) => {
        return response;
    }, async (error) => {

        if(error.config && error.response.data.message === "expired" && error.status === 400){

            try {
                const res = await axios.post(
                    `${import.meta.env.VITE_BASE_URL}/reissue`,
                    {},
                    {
                        withCredentials: true,
                    }
                );

                if (res.status === 200) {
                    // Redux에 새로운 access token 저장
                    const newAccessToken = res.headers["authorization"];
               
                    if (newAccessToken === null) {
                        store.dispatch(logout());
                        alert('이용시간이 만료되었습니다. 로그인 후 이용해주세요.')
                        window.location.href ='/';
                    }
                    let decodingInfo = DecodingInfo(newAccessToken);
                    const userNo = decodingInfo.userNo;
                    console.log('새로운 토큰', userNo)
                    store.dispatch(login({ accessToken: newAccessToken, userNo: userNo }));

                    // 원래 요청을 새로운 access token으로 재시도
                    error.config.headers["Authorization"] = `${newAccessToken}`;
                    return privateApi(error.config);
                } else {
                    store.dispatch(logout());
                    alert('이용시간이 만료되었습니다. 로그인 후 이용해주세요.')
                    window.location.href ='/';
                }
            } catch (error) {
                // refresh token 요청 실패 시 로그아웃
                store.dispatch(logout());
                alert('이용시간이 만료되었습니다. 로그인 후 이용해주세요.')
                window.location.href ='/';
            }
        }

        return Promise.reject(error);
    }
)