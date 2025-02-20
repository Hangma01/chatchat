import React, { useEffect, useState } from 'react';
import { Link } from "react-router-dom";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { login } from "../../store/userSlice";
import { useNavigate } from 'react-router-dom';
import DecodingInfo from '../../api/DecodingInfo';


export default function Login() {

    // 아이디 비밀번호 입력
    const [idValue, setIdValue] = useState('');
    const [pwValue, setPwValue] = useState('');

    // 아이디 비밀번호 입력 핸들러
    const handlerIdChange = (e) => {
        setIdValue(e.target.value)
    }

    const handlerPwChange = (e) => {
        setPwValue(e.target.value)
    }

    // dispatch
    const dispatch = useDispatch();
    const user = useSelector((state) => state.user.userState);

    // 리다이렉트
    const navigate = useNavigate();

    // 로그인 여부 확인
    useEffect(() => {
        if (user) {
            navigate('/home');
        }
    })


    // 로그인 버튼 클릭
    const loginClick = async () => {
        try {
            const response = await axios.post(`${import.meta.env.VITE_BASE_URL}/login`, {
                username: idValue,
                password: pwValue
            },
                {
                    headers: {
                        'Content-Type': 'application/json'  // 요청 헤더에서 Content-Type을 JSON으로 설정
                    },
                    withCredentials: true,
                });

            // 로그인 성공 시    
            if (response.status === 200) {
                // 액세스 토큰 가져오기
                const accessToken = response.headers["authorization"];
                let decodingInfo = DecodingInfo(accessToken);
                const userNo = decodingInfo.userNo;

                dispatch(login({ accessToken: accessToken, userNo: userNo }));

                navigate('/home');
            }
        } catch (error) {
            alert("사용자 계정을 찾을수 없습니다. 다시 시도해주세요.")
        }
    }

    return (
        <div className='login-container'>
            <div className='login-wrap'>
                <div className='logo'>
                    로고
                </div>

                {/* 아이디 비밀번호 입력 */}
                <div className='input-box'>
                    <input type='text' value={idValue} onChange={handlerIdChange} placeholder='아이디' />
                </div>

                <div className='input-box'>
                    <input type='text' value={pwValue} onChange={handlerPwChange} placeholder='비밀번호' />
                </div>


                <button className='login-btn' onClick={loginClick}>로그인</button>

                <div className='line'></div>

                {/* 회원가입 화면 이동*/}
                <div className='join--wrap'>
                    <span>계정이 없으신가요?</span>
                    <Link to="/join" className='link'>회원가입</Link>
                </div>
            </div>
        </div>
    );
};

