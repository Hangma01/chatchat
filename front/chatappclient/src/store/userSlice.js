import { configureStore, createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
    name: "user",
    initialState: {
        userState: false, // 초기값 설정
        accessToken: null,
        userNo: 0
    },
    reducers: {
        login(state, action) {
            state.userState = true;
            state.accessToken = action.payload.accessToken;
            state.userNo = action.payload.userNo;
        },
        logout(state) {
            // 로그아웃 시 상태 초기화
            state.userState = false;
            state.accessToken = null;
            state.userNo = 0;
        },
    },
});

export let { login, logout } = userSlice.actions;

export default userSlice;
