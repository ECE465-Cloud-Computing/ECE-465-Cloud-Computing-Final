import React, {Component} from "react";
import {Link} from "react-router-dom";

class Nav extends Component {
    constructor(props) {
        super(props);
    }

    handleLogout = () => {
        localStorage.removeItem("user");
        window.location = "/";
    };

    render() {
        const user = JSON.parse(localStorage.getItem("user"));
        if (user != null) {
            return (
                <div style={{textAlign: 'right', padding: '10px'}}>
                    <Link to="/">
                        <button style={{marginLeft: '20px'}}>Home</button>
                    </Link>
                    <Link to="/mytrips" >
                        <button style={{marginLeft: '20px'}}>My Trips</button>
                    </Link>
                    <button onClick={this.handleLogout} style={{marginLeft: '20px'}}>Logout</button>
                </div>
            );
        } else {
            return (
                <div style={{textAlign: 'right', padding: '10px'}}>
                    <Link to="/">
                        <button>Home</button>
                    </Link>
                    <Link to="/login">
                        <button style={{marginLeft: '20px'}}>Login</button>
                    </Link>
                </div>
            );
        }
    }
}

export default Nav;
