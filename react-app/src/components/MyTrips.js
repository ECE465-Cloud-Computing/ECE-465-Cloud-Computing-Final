import React, { Component } from "react";
import axios from "axios";

class MyTrips extends Component {
    constructor(props) {
        super(props);

        this.state = {
            trips: []
        };
    }

    async componentDidMount() {
        const user = JSON.parse(localStorage.getItem("user"));
        if (user != null) {
            console.log(user.username);
            axios.get("/trips", {
                params: {
                    Username: user.username
                }
            })
            .then((response) => {
                    console.log(response.data);
                    if(response.data.body) {
                        this.setState({
                            trips: response.data.body
                        })
                    }
                }
            );
        } else {
            this.props.history.push("/");
        }
    }

    render() {
        const {trips} = this.state;
        let tripsRender = null;

        if (trips.length !== 0) {
            tripsRender = trips.slice().reverse().map(trip => {
                let splitTrip = trip.split(',');
                const airline = splitTrip[0];
                const tripString = splitTrip[1];
                return (
                    <div>
                        <h3>
                            Airline: {airline}
                        </h3>
                        {tripString}
                        <p>----------------------------------------------------------------------</p>
                    </div>
                );
            });
            return (
                <div style={{ textAlign: "center" }}>
                    <h1>My Trips</h1>
                    <br/>
                    <button onClick={() => {
                        const user = JSON.parse(localStorage.getItem("user"));
                        if (user != null) {
                            console.log({
                                Username: user.username
                            });
                            axios.delete("/trips", {
                                params: {
                                    Username: user.username
                                }
                            })
                            .then((response) => {
                                    console.log(response.data);
                                    window.location.reload();
                                }
                            );
                        } else {
                            window.alert("Must be logged in to clear trips");
                        }
                    }}>Clear trips</button>
                    <br/>
                    {tripsRender}
                </div>
            );
        } else {
            return (
                <div style={{ textAlign: "center" }}>
                    <h1>My Trips</h1>
                    <br/>
                    <h3>No trips saved</h3>
                </div>
            );
        }
    }
}

export default MyTrips;
