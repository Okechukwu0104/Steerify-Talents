import {useState} from 'react'
import style from "../assets/assetsStyles.module.css"
import {useNavigate} from "react-router-dom"
import Switch from './Burger';
function SideBar() {
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const navigate = useNavigate();
    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };



    return (
        <div className={`${style.SideBar} ${isSidebarOpen ? style.open : ''}`}>
            <Switch  onClick={toggleSidebar} />
            <li onClick ={()=>navigate("/posts")} className={style.list}>Now Playing</li>
            <li onClick ={()=>navigate("/job-offers")} className={style.list}>Up Coming</li>
            <li onClick ={()=>navigate("/profile")} className={style.list}>Top Rated</li>

        </div>
    )
}

export default SideBar
