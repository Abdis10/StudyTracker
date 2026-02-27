
export async function registerSession(sessionData, token) {
    const response = await fetch("/session/session-registration", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(sessionData),

    });
    const data = await response.json();
    return {
        success: response.ok,
        data,
        status: response.status
    }
}

export async function getSessions(token) {
    const response = await fetch("session/sessions", {
        method: "GET",
        headers: { "Authorization": `Bearer ${token}` }
    });
    const data = await response.json();
    return {
        success: response.ok,
        data,
        status: response.status
    }
}

export async function updateSession(updateData, sessionId, token) {
    const response = await fetch(`/session/${sessionId}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(updateData)
    });
    const data = await response.json();
    return {
        success: response.ok,
        data,
        status: response.status
    }
}


export async function deleteSession(sessionId, token) {
    const response = await fetch(`/session/${sessionId}`, {
        method: "DELETE",
        headers: {"Authorization": `Bearer ${token}`}
    });
    return {
        success: response.ok,
        status: response.status
    }
}