
const BASE_URL = import.meta.env.BASE_URL;

async function registerSession(sessionData, token) {
    const response = await fetch(BASE_URL + "/session/session-registration", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(sessionData),

    });

    if (!response.ok) {
        return await response.json();
    }

    else {
        return await response.json();
    }
}

async function getSessions(token) {
    const response = await fetch(BASE_URL + "/session/sessions", {
        method: "GET",
        headers: { "Authorization": `Bearer ${token}` }
    });
    if (!response.ok) {
        return await response.json();
    }

    else {
        return await response.json();
    }
}

async function updateSession(updateData, sessionId, token) {
    const response = await fetch( BASE_URL + `/session/${sessionId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(updateData)
    });
    if (!response.ok) {
        return await response.json();
    }

    else {
        return await response.json();
    }
}


async function deleteSession(sessionId, token) {
    const response = await fetch(BASE_URL + `/session/${sessionId}`, {
        method: "DELETE",
        headers: {"Authorization": `Bearer ${token}`}
    });
    if (!response.ok) {
        return await response.json();
    }

    else {
        return await response.json();
    }
}