
const BASE_URL = import.meta.env.BASE_URL;

async function signup(formData) {
    const response = await fetch(BASE_URL+ "/auth/signup", {
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
    const response = await fetch(BASE_URL + "/auth/login", {
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



