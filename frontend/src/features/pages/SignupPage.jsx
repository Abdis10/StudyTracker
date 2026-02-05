import SignupForm from "../auth/components/SignupForm.jsx";
import "../auth/css/LoginSignup.css"
import Navbar from "../components/Navbar.jsx";


function SignupPage() {
    return (
        <>
            <Navbar/>
            <SignupForm />
        </>

    )
}

export default SignupPage;