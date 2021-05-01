import React, { Component } from "react";
import axios from "axios";

class Search extends Component {
    constructor(props) {
        super(props);

        this.state = {
            start: "",
            end: "",
            filter: "PRICE",
            startError: "",
            endError: "",
            responseError: "",
        };
    }

    handleChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value,
        });
    };

    validate = () => {
        const { start, end } = this.state;
        let startError = "";
        let endError = "";

        if (start === "") {
            startError = "Must not be empty";
        }
        if (end === "") {
            endError = "Must not be empty";
        }
        if (startError || endError) {
            this.setState({
                startError: startError,
                endError: endError,
            });
            return false;
        }
        return true;
    };

    handleSubmit = (e) => {
        e.preventDefault();
        this.setState({
            startError: "",
            endError: "",
            responseError: "",
        });
        const isValid = this.validate();
        if (isValid) {
            console.log(this.state);
            axios
                .post("http://3.83.211.184:5000/", null, {
                    params: {
                        start: 0,
                        end: 3,
                        filter: this.state.filter,
                    },
                })
                .then((response) => {
                    console.log(response.data);
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
            start,
            end,
            filter,
            startError,
            endError,
            responseError,
        } = this.state;
        return (
            <form onSubmit={this.handleSubmit}>
                <div>
                    <h1>Search for Flights</h1>
                    <label>Filter </label>
                    <select
                        name="filter"
                        value={filter}
                        onChange={this.handleChange}
                    >
                        <option value="PRICE">Price</option>
                        <option value="TIME">Time</option>
                    </select>
                </div>
                <div>
                    <input
                        type="text"
                        name="start"
                        placeholder="From"
                        value={start}
                        onChange={this.handleChange}
                    />
                </div>
                <div style={{ color: "red" }}>{startError}</div>
                <div>
                    <input
                        type="text"
                        name="end"
                        placeholder="To"
                        value={end}
                        onChange={this.handleChange}
                    />
                </div>
                <div style={{ color: "red" }}>{endError}</div>
                <div style={{ color: "red" }}>{responseError}</div>
                <button type="submit">Search</button>
            </form>
        );
    }
}

export default Search;
