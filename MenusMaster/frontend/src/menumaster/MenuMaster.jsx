import React, { useState } from 'react';
import { sendMessage } from './sendMessage';
import MainLayout from '../components/MainLayout';
import CTA from '../pages/home/container/CTA'

function MenuMaster() {
  // 新状态来存储消息历史
  const [messageHistory, setMessageHistory] = useState([]);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [isLogin, setIsLogin] = useState(false);

  const handleInputChange = (event) => {
    setMessage(event.target.value);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      // 发送消息并接收回复
      const reply = await sendMessage(message);
      const newMessageHistory = [
        ...messageHistory,
        { text: message, author: 'user' },
        { text: reply, author: 'chatgpt' },
      ];
      setMessageHistory(newMessageHistory);
      setMessage(''); // 清空输入框
    } catch (error) {
      console.error('Failed to send message:', error);
    }
    setLoading(false);
  };

  return (
    <MainLayout>
      {isLogin ? (
        <div className='my-container'>
          <h1>Menu Master</h1>
          <form onSubmit={handleSubmit} className='message-form'>
            <input
              type="text"
              value={message}
              onChange={handleInputChange}
              placeholder="Type your message..."
              className='message-input'
              required
            />
            <button type="submit" disabled={loading} className='send-button'>
              Send
            </button>
          </form>
          <div className="messages">
            {messageHistory.map((msg, index) => (
              <div key={index} className={`message-bubble ${msg.author === 'user' ? 'user-message' : 'chatgpt-message'}`}>
                <p>{msg.text}</p>
              </div>
            ))}
            {loading && <p>Sending...</p>}
          </div>
        </div>
      ) : (
        <div className='my-container'>
      <h1>Menu Master</h1>
      <form onSubmit={handleSubmit} className='message-form'>
        <input
          type="text"
          value={message}
          onChange={handleInputChange}
          placeholder="Type your message..."
          className='message-input'
          required
        />
        <button type="submit" disabled={loading} className='send-button'>
          Send
        </button>
      </form>
      <div className="messages">
        {messageHistory.map((msg, index) => (
          <div key={index} className={`message-bubble ${msg.author === 'user' ? 'user-message' : 'chatgpt-message'}`}>
            <p>{msg.text}</p>
          </div>
        ))}
        {loading && <p>Sending...</p>}
      </div>
      </div>
      )}
    <CTA />
    </MainLayout>
  );
}

export default MenuMaster;