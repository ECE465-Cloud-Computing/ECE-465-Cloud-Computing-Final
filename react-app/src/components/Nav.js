import React from "react";
import {Link} from "react-router-dom";

const Nav = (props) => {
    function handleLogout() {
        localStorage.removeItem("user");
        window.location.href = "http://localhost:3000/";
    }

    const user = JSON.parse(localStorage.getItem("user"));
    if (user != null) {
        return (
            <div style={{textAlign: 'right', padding: '10px'}}>
                <Link to="/">Home</Link>
                <Link to="/trips">My trips</Link>
                <button onClick={handleLogout} style={{marginLeft: '20px'}}>Logout</button>
            </div>
        );
    } else {
        return (
            <div style={{textAlign: 'right', padding: '10px'}}>
                <Link to="/">Home</Link>
                <Link to="/login" style={{marginLeft: '20px'}}>Login</Link>
            </div>
        );
    }
};

export default Nav;
