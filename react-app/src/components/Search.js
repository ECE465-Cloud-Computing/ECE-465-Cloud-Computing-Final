import React, { Component } from "react";
import axios from "axios";

class Search extends Component {
    constructor(props) {
        super(props);

        this.state = {
            start: "",
            end: "",
            filter: "PRICE",
            results: [],
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
            // console.log(this.state);
            axios.get("", {
                    params: {
                        start: this.state.start,
                        end: this.state.end,
                        filter: this.state.filter,
                    },
                })
                .then((response) => {
                    // console.log(response.data);
                    this.setState({
                        results: response.data
                    })
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
            results,
            startError,
            endError,
            responseError,
        } = this.state;

        let resultsRender = null;

        if (results.length !== 0) {
            resultsRender = results.map(result => {
                let tripRender = '';
                for (let i = 0; i < result.path.length; i++) {
                    if (i === 0) {
                        tripRender = tripRender + result.path[i];
                    } else {
                        tripRender = tripRender + " -> " + result.path[i];
                    }
                }
                return (
                    <div>
                        <h3>
                            Airline: {result.airline}
                        </h3>
                        {tripRender}
                        <h3>
                            Cost: {result.cost}
                        </h3>
                        <button onClick={() => {
                            const user = JSON.parse(localStorage.getItem("user"));
                            if (user != null) {
                                // console.log({
                                //     Username: user.username,
                                //     trip: tripRender
                                // });
                                axios.post("/trips", {
                                        body: JSON.stringify({
                                            Username: user.username,
                                            trip: result.airline + "," + tripRender
                                        })
                                    })
                                .then((response) => {
                                        // console.log(response.data);
                                        window.alert("Trip saved")
                                    }
                                );
                            } else {
                                window.alert("Must be logged in to save a trip");
                            }
                        }}>Save trip</button>
                        <p>----------------------------------------------------------------------</p>
                    </div>
                )
            })
        }

        return (
            <div style={{ textAlign: "center" }}>
                <form onSubmit={this.handleSubmit}>
                    <div>
                        <h1>Search for Flights</h1>
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
                    <label>Sort by </label>
                    <select
                        name="filter"
                        value={filter}
                        onChange={this.handleChange}
                    >
                        <option value="PRICE">Price</option>
                        <option value="TIME">Time</option>
                    </select>
                    <div style={{ color: "red" }}>{responseError}</div>
                    <button type="submit">Search</button>
                </form>
                {resultsRender}
                <br/>
            </div>
        );
    }
}

export default Search;
