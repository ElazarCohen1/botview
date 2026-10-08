import type { GitUserDto } from "../../types/Gituser.js";

export async function login(): Promise<GitUserDto | undefined> {
    try {
        const res = await fetch('/api/user');

        if (!res.ok) {
            throw new Error(`HTTP error: ${res.status}`);
        }

        const user: GitUserDto = await res.json();
        return user;

    } catch (e) {
        console.error("error :", e);
        return undefined;
    }
}