const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function getDashboardData(token) {
    const response = await fetch(`${BASE_URL}/session/dashboard`, {
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