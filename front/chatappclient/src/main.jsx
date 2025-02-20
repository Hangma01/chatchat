import "./index.css";

// Router
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

// Redux
import store from "./store/index.js";
import { PersistGate } from "redux-persist/integration/react";
import { persistStore } from "redux-persist";
import { Provider } from "react-redux";

import App from './App.jsx'
import Login from './pages/Login/Login.jsx';
import Join from './pages/Join/Join.jsx'
import Home from './pages/Home/Home.jsx';

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      // 효준
      { index: true, element: <Login /> },
      { path: "/join", element: <Join /> },
      { path: "/home", element: <Home /> },
    ],
  },
]);

const persistor = persistStore(store);

ReactDOM.createRoot(document.getElementById('root')).render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <RouterProvider router={router}>
      </RouterProvider>
    </PersistGate>
  </Provider>
)
