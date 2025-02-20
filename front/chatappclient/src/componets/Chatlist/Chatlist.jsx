import { LuUser } from "react-icons/lu";
import { privateApi } from "../../api/token";
import { useState } from "react";

export default function Chatlist({ chatlist, connect, selSender, setSelSender }) {

    const validMessage = ""

    const handleSelChatlist = async (chatroomNo, nickname) => {

        connect(validMessage, chatroomNo)
        setSelSender(nickname)
    }
    console.log(chatlist)
    return (
        <ul>
            {chatlist && chatlist.map((chat, index) => (
                <li className={`chatlist-box ${selSender === chat.nickname ? 'selected' : ''}`} key={index} onClick={() => handleSelChatlist(chat.chatroomNo, chat.nickname)}>
                    <div className="chatlist-profile">
                        <LuUser />
                    </div>

                    <div className="chatlist-content">
                        <div className="chatlist-username">{chat.nickname}</div>
                        <div className="chatlist-lastmessage">{chat.lastMessage}</div>
                    </div>

                </li>
            ))}
        </ul>
    );

}