
export async function signup(signupformata) {
    const response = await fetch( "auth/signup", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(signupformata)
    });

    const data = await response.json();
    return {
        success: response.ok,
        data,
        status: response.status
    }
}


export async function login(loginformData) {
    const response = await fetch("/auth/login", {
       method: "POST",
       headers: {"Content-Type": "application/json"},
       body: JSON.stringify(loginformData)
    });

    const data = await response.json();
    return {
        success: response.ok,
        data,
        status: response.status
    }
}



