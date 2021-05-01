import React, { Component } from "react";
import loggedIn from "./loggedIn";
import Login from "./Login";
import Search from "./Search";

class Home extends Component {

    render() {
        return (
            <div style={{ textAlign: "center" }}>
                <h1>Welcome</h1>
                <Search></Search>
            </div>
        );
    }
}

export default Home;