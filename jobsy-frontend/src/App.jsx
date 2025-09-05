import { ToastContainer } from 'react-toastify';
import './App.css'
import {Route,Routes} from 'react-router-dom';
import Home from '../src/pages/Home';
import Login from '../src/pages/Login';
import EmailVerify from '../src/pages/EmailVerify';
import ResetPassword from '../src/pages/ResetPassword';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return(
    <div>
        <ToastContainer />
        <Routes>
            <Route path='/' element={<Home />} />
            <Route path='/login' element={<Login />} />
            <Route path= '/email-verify' element={<EmailVerify />} />
            <Route path= '/reset-password' element={<ResetPassword />} />
        </Routes>
    </div>
  )
}

export default App
