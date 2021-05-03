import React, { Component } from "react";
import axios from "axios";
import loggedIn from "./loggedIn";
import {Link} from "react-router-dom";

class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            username: "",
            password: "",
            usernameError: "",
            passwordError: "",
            responseError: "",
        };
    }

    componentDidMount() {
        if (loggedIn()) {
            this.props.history.push("/");
        }
    }

    handleChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value,
        });
    };

    validate = () => {
        const { username, password } = this.state;
        let usernameError = "";
        let passwordError = "";

        if (username === "") {
            usernameError = "Must not be empty";
        }
        if (password === "") {
            passwordError = "Must not be empty";
        }
        if (usernameError || passwordError) {
            this.setState({
                usernameError: usernameError,
                passwordError: passwordError,
            });
            return false;
        }
        return true;
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.setState({
            usernameError: "",
            passwordError: "",
            responseError: "",
        });
        const isValid = this.validate();
        if (isValid) {
            // console.log(this.state);
            axios
                .get("/user", {
                    params: {
                        Username: this.state.username,
                        password: this.state.password,
                    }
                })
                .then((response) => {
                    // console.log(response.data);
                    localStorage.setItem("user", JSON.stringify(response.data.body));
                    window.location = "/";
                })
                .catch((error) => {
                    this.setState({
                        responseError: "Incorrect credentials",
                    });
                });
        }
    };

    render() {
        const {
            username,
            password,
            usernameError,
            passwordError,
            responseError,
        } = this.state;
        return (
            <div style={{ textAlign: "center" }}>
                <form onSubmit={this.handleSubmit}>
                    <div>
                        <h1>Login</h1>
                    </div>
                    <div>
                        <input
                            type="text"
                            name="username"
                            placeholder="username"
                            value={username}
                            onChange={this.handleChange}
                        />
                    </div>
                    <div style={{ color: "red" }}>{usernameError}</div>
                    <div>
                        <input
                            type="password"
                            name="password"
                            placeholder="password"
                            value={password}
                            onChange={this.handleChange}
                        />
                    </div>
                    <div style={{ color: "red" }}>{passwordError}</div>
                    <div style={{ color: "red" }}>{responseError}</div>
                    <button type="submit">Login</button>
                    <p>
                        Don't have an account? Sign up{" "}
                        <Link to="/register">Here</Link>
                    </p>
                </form>
            </div>
        );
    }
}

export default Login;
