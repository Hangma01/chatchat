import { IoChatbubbles } from "react-icons/io5";
import { LiaSpellCheckSolid } from "react-icons/lia";
import { useSelector } from "react-redux";
import { LuUser } from "react-icons/lu";
import { useState } from "react";
import { privateApi } from "../../api/token";

export default function Chat({ chatroomNo, messages, selSender, sendMessage }) {

    const userNo = useSelector((state) => state.user.userNo);

    // 사용자 입력을 저장할 변수
    const [inputValue, setInputValue] = useState('');

    // 입력 필드에 변화가 있을 때마다 inputValue를 업데이트
    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    const handleSendMessage = () => {
        if (inputValue) {
            sendMessage(inputValue)
            setInputValue('')
        }
    }

    const handleKeyDown = (event) => {
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            handleSendMessage();
        }
    }

    const handleSpellingCheck = async () => {

        const request = "Hello, please return the text with any spelling or typo corrections, but do not delete any uncorrectable words. If it's already perfect, just return the text I sent : " + encodeURIComponent(inputValue)

        if (inputValue && inputValue.trim().length > 0) {
            try {
                const response = await privateApi.get(`${import.meta.env.VITE_BASE_URL}/chatgpt/spellingCheck?prompt=${request}`,
                    {},
                    {
                        withCredentials: true,
                    });

                if (response.status === 200) {
                    setInputValue(response.data)
                }
            } catch (error) {
                console.log(error)
                alert('실패했습니다.')
            }
        } else {
            alert('채팅을 입력해주세요.')
        }

    }

    return (
        <div className="chat-container">
            {chatroomNo ?
                <>
                    <div className="sender-info">
                        <div className="sender-profile">
                            <LuUser />
                        </div>
                        <span>{selSender}</span>
                    </div>

                    <div className="chat-box">
                        <div className="chat-content-box">
                            {messages && messages.map((item, index) =>
                            (
                                userNo === item.sender ?
                                    <div className="chat user" key={index} >
                                        {item.message}
                                    </div>
                                    :
                                    <div className="sender-box" key={index}>
                                        <div className="sender-profile">
                                            <LuUser />
                                        </div>
                                        <div className="chat sender">
                                            {item.message}
                                        </div>
                                    </div>

                            )
                            )}
                        </div>

                        <div className="chat-input-box">
                            <textarea className="chat-input" placeholder="메시지를 입력하세요." value={inputValue} onChange={handleInputChange} onKeyDown={handleKeyDown} />
                            <div className="chat-additional-services">
                                <div onClick={handleSpellingCheck}>
                                    <LiaSpellCheckSolid className="spelling-check-icon" />
                                </div>
                                <button className="send" onClick={handleSendMessage}>전송</button>
                            </div>
                        </div>
                    </div>
                </>
                :
                <div className="non-chat">
                    <IoChatbubbles className="chat-icon" />
                    <p>채팅할 상대를 선택해주세요</p>
                </div>
            }
        </div >
    );
}