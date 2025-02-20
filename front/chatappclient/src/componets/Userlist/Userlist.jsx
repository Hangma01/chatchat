import { LuUser } from "react-icons/lu";
import { privateApi } from "../../api/token";
import { useState } from "react";

export default function Userlist({ userlist, connect, selSender, setSelSender }) {

    const handleSelUser = async (userNo, nickname) => {
        try {
            const response = await privateApi.post(`${import.meta.env.VITE_BASE_URL}/chat-room/create`, {
                userNo: userNo
            },
                {
                    headers: {
                        'Content-Type': 'application/json'  // 요청 헤더에서 Content-Type을 JSON으로 설정
                    },
                    withCredentials: true,
                });

            // 유저 선택 시  
            if (response.status === 200) {
                console.log(response)
                const message = response.data.message;
                const chatroomNo = response.data.chatroomNo;

                const validMessage = message !== undefined ? message : "default message";

                connect(validMessage, chatroomNo)
                setSelSender(nickname)
            }
        } catch (error) {
            alert("채팅방 연결 실패")
        }
    }

    return (
        <ul>
            {userlist && userlist.map((user, index) => (
                <li className={`user-box ${selSender === user.nickname ? 'selected' : ''}`} key={index} onClick={() => handleSelUser(user.userNo, user.nickname)}>
                    <div className="user-profile">
                        <LuUser />
                    </div>
                    <p>{user.nickname}</p>
                </li>
            ))}
        </ul>
    );
}