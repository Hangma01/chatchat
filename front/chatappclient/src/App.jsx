import React from 'react';
import { Outlet } from 'react-router-dom';

import "./App.css";

// pages
import "./pages/Login/Login.css";
import "./pages/Home/Home.css";
import "./pages/Join/Join.css"

// componets
import "./componets/Userlist/Userlist.css"
import "./componets/Chatlist/Chatlist.css"
import "./componets/Chat/Chat.css"

export default function App() {
  return (
    <div className="App">
      <Outlet />
    </div>
  );
}