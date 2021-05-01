import React, { Component } from "react";
import axios from "axios";

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
                .post("http://localhost:5000/user/login", {
                    body: {
                        username: this.state.username,
                        password: this.state.password,
                    }
                })
                .then((response) => {
                    const user = JSON.stringify(response.data);
                    localStorage.setItem("user", user);
                    window.location.href = "http://localhost:3000/";
                })
                .catch((error) => {
                    if (error.response) {
                        this.setState({
                            responseError: error.response.data.error,
                        });
                    }
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
                        <a href="http://localhost:3000/register">here</a>
                    </p>
                </form>
            </div>
        );
    }
}

export default Login;
