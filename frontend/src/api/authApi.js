
async function signup(formData) {
    const response = await fetch("http://localhost:7000/auth/signup", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(formData)
    });

    if (!response.ok) {
        return await response.json();
    }

    else {
        return await response.json();
    }
}


async function login(loginformData) {
    const response = await fetch("http://localhost:7000/auth/login", {
       method: "POST",
       headers: {"Content-Type": "application/json"},
       body: JSON.stringify(loginformData)
    });

    if (!response.ok) {
        return await response.json();
    }

    else {
        return await response.json();
    }
}



