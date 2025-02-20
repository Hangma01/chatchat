import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from "react-redux";
import { useEffect, useRef, useState } from 'react';
import { login, logout } from "../../store/userSlice";
import { FaRegUser } from "react-icons/fa";
import { BsChatDots } from "react-icons/bs";
import { privateApi } from "../../api/token";
import { Stomp } from "@stomp/stompjs";
import { IoLogOutOutline } from "react-icons/io5";

import axios from "axios";
import Userlist from '../../componets/Userlist/Userlist';
import Chat from '../../componets/Chat/Chat';
import Chatlist from '../../componets/Chatlist/Chatlist';
import DecodingInfo from '../../api/DecodingInfo';

export default function Home() {

    // 메뉴 리스트
    const menuList = [
        {
            No: 1,
            Icon: FaRegUser,
            command: "userlist",
        },
        {
            No: 2,
            Icon: BsChatDots,
            command: "chat-part/chatlist",
        }
    ];

    const [selMenu, setSelMenu] = useState(1);
    const command = menuList[0].command;
    const [datalist, setDatalist] = useState([]);
    const [selSender, setSelSender] = useState('');


    // 리다이렉트
    const navigate = useNavigate();

    // 로그인 여부 확인
    const dispatch = useDispatch();
    const user = useSelector((state) => state.user.userState);
    const userNo = useSelector((state) => state.user.userNo);
    let accessToken = useSelector((state) => state.user.accessToken);
    const accessTokenRef = useRef(accessToken);

    useEffect(() => {
        accessTokenRef.current = accessToken
    }, [accessToken]); // accessToken 값이 변경될 때마다 실행

    useEffect(() => {
        if (!user) {
            navigate('/');
        } else {
            getList(command);
        }
    }, [])


    // 메뉴 선택
    const handleSelMenu = (no, command) => {
        if (!(selMenu === no)) {
            setSelMenu(no)
            getList(command)
        }
    }


    // 리스트 가져오기
    const getList = async (command) => {
        try {
            const response = await privateApi.get(`${import.meta.env.VITE_BASE_URL}/${command}`,
                {},
                {
                    withCredentials: true,
                });

            if (response.status === 200) {
                setDatalist(response.data)
                console.log(response)
            }
        } catch (error) {
            dispatch(logout({ accessToken: null }));
            navigate('/');
        }
    }



    // 로그아웃
    const logoutClick = async () => {
        try {
            const response = await axios.post(`${import.meta.env.VITE_BASE_URL}/logout`,
                {},
                {
                    withCredentials: true,
                });

            // 로그아웃 성공 시    
            if (response.status === 200) {

                dispatch(logout({ accessToken: null }));
                navigate('/');
            }
        } catch (error) {
            dispatch(logout({ accessToken: null }));
            navigate('/');
        }
    }


    const stompClient = useRef(null);
    // 채팅 내용들을 저장할 변수
    const [messages, setMessages] = new useState([]);

    // 채팅방 번호 변수
    const [chatroomNo, setChatroomNo] = useState(null);
    const rspChatroomNo = useRef(0)

    const SERVER_URL = 'ws://localhost:8080/ws';
    const SUB_ENDPOINT = '/sub/chatroom/';
    const PUB_ENDPOINT = '/pub/message';



    // 웹소켓 연결 설정
    const connect = (message, chatroomNo) => {

        disconnect()
        rspChatroomNo.current = chatroomNo
        const socket = new WebSocket(SERVER_URL);
        stompClient.current = Stomp.over(() => socket);

        stompClient.current.connect({
            Authorization: accessTokenRef.current
        }, () => {
            setChatroomNo(chatroomNo)

            // 채팅방 구독
            stompClient.current.subscribe(SUB_ENDPOINT + chatroomNo, (message) => {
                const newMessage = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, newMessage]);
            },
                {
                    Authorization: accessTokenRef.current
                });

            if (message !== "new") {
                fetchMessages(chatroomNo)
            }


        }, (error) => {
            if (error.body === "expired" || error.headers.message === "Failed to send message to ExecutorSubscribableChannel[clientInboundChannel]") {
                reissue()
            } else {
                dispatch(logout({ accessToken: null }));
                navigate('/');
            }
        });
    };

    const reissue = async () => {
        try {
            const response = await axios.post(`${import.meta.env.VITE_BASE_URL}/reissue`,
                {},
                {
                    withCredentials: true,
                });

            if (response.status === 200) {

                const newAccessToken = response.headers["authorization"];
                let decodingInfo = DecodingInfo(newAccessToken);
                const newUserNo = decodingInfo.userNo;
                dispatch(login({ accessToken: newAccessToken, userNo: newUserNo }));

                connect("defalut", rspChatroomNo.current);

            }
        } catch (error) {
            dispatch(logout({ accessToken: null }));
            navigate('/');
        }
    }

    // 웹소켓 연결 해제
    const disconnect = () => {
        if (stompClient.current) {
            stompClient.current.disconnect();
        }
    };

    // 기존 채팅 메시지를 서버로부터 가져오는 함수
    const fetchMessages = async (rspChatroomNo) => {

        try {
            const response = await privateApi.get(`${import.meta.env.VITE_BASE_URL}/chat`, {
                params: { chatroomNo: rspChatroomNo },  // 쿼리 파라미터로 전달
                withCredentials: true,
            });

            if (response.status === 200) {
                setMessages(response.data)
            }
        } catch (error) {
            alert("기존 채팅을 가져오는데 실패했습니다.");
        }
    };

    //메세지 전송
    const sendMessage = (inputValue) => {

        if (stompClient.current && inputValue) {
            const body = {
                chatroomNo: chatroomNo,
                message: inputValue,
                sender: userNo
            };
            stompClient.current.send(PUB_ENDPOINT, { Authorization: `Bearer ${accessTokenRef.current}` }, JSON.stringify(body));
        }
    };

    return (
        <div className='home-container'>
            <div className='header'>
                <div>로고</div>
            </div>

            <div className='content'>
                <ul className='menu'>
                    <div>
                        {menuList.map((menu) => (
                            <li className={`menu-item ${menu.No === selMenu ? "selected" : ""}`} key={menu.No} onClick={() => handleSelMenu(menu.No, menu.command)}>
                                <menu.Icon className='menu-icon' />
                            </li>
                        ))}
                    </div>

                    <div className='logout' onClick={logoutClick}>
                        <IoLogOutOutline />
                    </div>
                </ul>

                <div className='list'>
                    {selMenu === menuList[0].No
                        ? <Userlist userlist={datalist} connect={(rspMessage, rspChatroomNo) => connect(rspMessage, rspChatroomNo)} selSender={selSender} setSelSender={setSelSender} />
                        : <Chatlist chatlist={datalist} connect={(rspMessage, rspChatroomNo) => connect(rspMessage, rspChatroomNo)} selSender={selSender} setSelSender={setSelSender} />}
                </div>

                <div className='chat-wrap'>
                    <Chat chatroomNo={chatroomNo} messages={messages} selSender={selSender} sendMessage={sendMessage} />
                </div>
            </div>
        </div>
    )
}