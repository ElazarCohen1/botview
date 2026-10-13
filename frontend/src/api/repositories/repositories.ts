import type { Repository } from "../../types/Repository";

export async function getRepositories(): Promise<Repository[] | undefined> {
    try {
        const res = await fetch('/api/repositories');

        if (!res.ok) {
            throw new Error(`HTTP error: ${res.status}`);
        }

        const repos: Repository[] = await res.json();
        return repos;

    } catch (e) {
        console.error("error :", e);
        return undefined;
    }
}