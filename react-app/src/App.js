import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import "./App.css";
import Home from "./components/Home";
import Login from './components/Login.js'
import Register from "./components/Register";

function App() {
    return (
        <Router>
            <Switch>
                <Route path="/" exact component={Home}/>
                <Route path="/register" exact component={Register}/>
                <Route path="/login" exact component={Login}/>
            </Switch>
        </Router>
    );
}

export default App;