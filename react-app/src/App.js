import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import "./App.css";
import Home from "./components/Home";
import Login from './components/Login.js'
import Register from "./components/Register";
import Nav from "./components/Nav";
import MyTrips from "./components/MyTrips";

function App() {
    return (
        <Router>
            <Nav/>
            <Switch>
                <Route path="/" exact component={Home}/>
                <Route path="/register" exact component={Register}/>
                <Route path="/login" exact component={Login}/>
                <Route path="/mytrips" exact component={MyTrips}/>
            </Switch>
        </Router>
    );
}

export default App;