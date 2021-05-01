import React, { Component } from "react";
import axios from "axios";
import loggedIn from "./loggedIn";

class Register extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            confirm_password: "",

            errors: {},
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
        const {
            username,
            password,
            confirm_password
        } = this.state;
        let isValid = true;
        let err = {};

        if (username === "") {
            isValid = false;
            err.username = "Must not be empty";
        }
        if (password === "") {
            isValid = false;
            err.password = "Must not be empty";
        }
        if (password !== confirm_password) {
            isValid = false;
            err.confirm_password = "Passwords do not match";
        }

        this.setState({
            errors: {
                ...err,
            },
        });

        return isValid;
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.setState({
            errors: {},
        });
        const isValid = this.validate();

        if (isValid) {
            let newUser = { ...this.state };
            axios
                .post("http://localhost:5000/user/register", newUser)
                .then((response) => {
                    const data = response.data;
                    const user = {
                        username: data.username,
                        trips: []
                    };
                    localStorage.setItem("user", JSON.stringify(user));
                    window.location.href = "http://localhost:3000/";
                })
                .catch((error) => {
                    if (error.response) {
                        console.log(error);
                        this.setState({
                            errors: {
                                response: error.response.data.error,
                            },
                        });
                    }
                });
        }
    };

    render() {
        const {
            username,
            password,
            confirm_password,
            errors
        } = this.state;

        return (
            <div style={{ textAlign: "center" }}>
                <h2>Register</h2>
                <form onSubmit={this.handleSubmit}>
                    <div>
                        <input
                            type="text"
                            name="username"
                            placeholder="username"
                            value={username}
                            onChange={this.handleChange}
                        />
                    </div>
                    <div style={{ color: "red" }}>{errors.username}</div>
                    <div>
                        <input
                            type="password"
                            name="password"
                            placeholder="password"
                            value={password}
                            onChange={this.handleChange}
                        />
                    </div>
                    <div style={{ color: "red" }}>{errors.password}</div>
                    <div>
                        <input
                            type="password"
                            name="confirm_password"
                            placeholder="confirm password"
                            value={confirm_password}
                            onChange={this.handleChange}
                        />
                    </div>
                    <div style={{ color: "red" }}>{errors.confirm_password}</div>
                    <div style={{ color: "red" }}>{errors.response}</div>
                    <button type="submit">Register</button>
                    <p>
                        Already have an account? Login{" "}
                        <a href="http://localhost:3000/">here</a>
                    </p>
                </form>
            </div>
        );
    }
}

export default Register;
