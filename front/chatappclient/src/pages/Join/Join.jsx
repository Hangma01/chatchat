import React, { useEffect, useState } from 'react';
import axios from "axios";
import { useNavigate } from 'react-router-dom';



export default function Join() {

    // 아이디 비밀번호 닉네임입력
    const [id, setId] = useState('');
    const [pw, setPw] = useState('');
    const [pwConfirm, setPwConfirm] = useState('')
    const [nickname, setNickname] = useState('');

    //오류메시지 상태저장
    const [idMessage, setIdMessage] = useState('')
    const [pwMessage, setPwMessage] = useState('')
    const [pwConfirmMessage, setPwConfirmMessage] = useState('')
    const [nicknameMessage, setNicknameMessage] = useState('')

    // 유효성 검사
    const [isId, setIsId] = useState(false)
    const [isPw, setIsPw] = useState(false)
    const [isPwConfirm, setIsPwConfirm] = useState(false)
    const [isNickname, setIsNickname] = useState(false)

    // 아이디 비밀번호 입력 핸들러
    const handlerIdChange = (e) => {
        const idCurrent = e.target.value.replace(/\s/g, '')
        setId(idCurrent)
        if (idCurrent.length < 2 || idCurrent.length > 12) {
            setIdMessage('2글자 이상 12글자 미만으로 입력해주세요.')
            setIsId(false)
        } else {
            DuplicateIdCheck(idCurrent)

        }
    }

    const handlerPwChange = (e) => {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/
        const pwCurrent = e.target.value.replace(/\s/g, '')
        setPw(pwCurrent)

        if (!passwordRegex.test(pwCurrent)) {
            setPwMessage('숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요!')
            setIsPw(false)
        } else {
            setPwMessage('')
            setIsPw(true)
        }
    }

    const handlerPwConfirmChange = (e) => {
        const pwConfirmCurrent = e.target.value.replace(/\s/g, '')
        setPwConfirm(pwConfirmCurrent)

        if (pw === pwConfirmCurrent) {
            setPwConfirmMessage('')
            setIsPwConfirm(true)
        } else {
            setPwConfirmMessage('비밀번호가 일치하지 않습니다.')
            setIsPwConfirm(false)
        }
    }

    const handlerNicknameChange = (e) => {
        const nicknameCurrent = e.target.value.replace(/\s/g, '')
        setNickname(nicknameCurrent)

        if (nicknameCurrent.length < 2 || nicknameCurrent.length > 5) {
            setNicknameMessage('2글자 이상 5글자 미만으로 입력해주세요.')
            setIsNickname(false)
        } else {
            console.log('타니')
            setNicknameMessage('')
            setIsNickname(true)
        }
    }


    const DuplicateIdCheck = async (idCurrent) => {
        try {
            const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/duplicationId?username=${idCurrent}`,
                {},
                {
                    withCredentials: true,
                });

            if (response.status === 200) {
                console.log('dd?')
                setIdMessage('')
                setIsId(true)
            }
        } catch (error) {
            console.log(error)
            if (error.status === 400) {
                setIdMessage('중복되는 아이디입니다.')
                setIsId(false)
            } else {
                alert('잘못된 요청입니다.')
                setIsId(false)
            }

        }
    }

    // 리다이렉트
    const navigate = useNavigate();


    // 로그인 버튼 클릭
    const joinClick = async () => {
        try {
            const response = await axios.post(`${import.meta.env.VITE_BASE_URL}/join`, {
                username: id,
                password: pw,
                nickname: nickname
            },
                {
                    headers: {
                        'Content-Type': 'application/json'  // 요청 헤더에서 Content-Type을 JSON으로 설정
                    },
                    withCredentials: true,
                });

            // 로그인 성공 시    
            if (response.status === 200) {

                navigate('/');
            }
        } catch (error) {
            alert("잘못된 요청입니다.")
        }
    }

    return (
        <div className='join-container'>
            <div className='join-wrap'>
                <div className='logo'>
                    로고
                </div>

                {/* 아이디 비밀번호 닉네임 입력 */}
                <div className='input-box'>
                    <input className="input" type='text' id='id' value={id} onChange={handlerIdChange} placeholder='' />
                    <label for="id" class="label">아이디</label>
                    {id.length > 0 && <div className={`message ${isId ? 'success' : 'error'}`}>{idMessage}</div>}
                </div>

                <div className='input-box'>
                    <input className="input" type='password' id='pw' value={pw} onChange={handlerPwChange} placeholder='' />
                    <label for="pw" class="label">비밀번호</label>
                    {pw.length > 0 && <div className={`message ${isPw ? 'success' : 'error'}`}>{pwMessage}</div>}
                </div>

                <div className='input-box'>
                    <input className="input" type='password' id='pwConfirm' value={pwConfirm} onChange={handlerPwConfirmChange} placeholder='' />
                    <label for="pwConfirm" class="label">비밀번호 확인</label>
                    {pwConfirm.length > 0 && <div className={`message ${isPwConfirm ? 'success' : 'error'}`}>{pwConfirmMessage}</div>}
                </div>

                <div className='input-box nickname-box'>
                    <input className="input" type='text' id='nickname' value={nickname} onChange={handlerNicknameChange} placeholder='' />
                    <label for="nickname" class="label">닉네임</label>
                    {nickname.length > 0 && <div className={`message ${isNickname ? 'success' : 'error'}`}>{nicknameMessage}</div>}
                </div>

                <button className='join-btn' onClick={joinClick}
                    disabled={!(isId && isPw && isPwConfirm && isNickname)}
                >회원 가입</button>

            </div>
        </div>
    );
};

