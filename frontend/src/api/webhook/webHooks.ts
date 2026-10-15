import type { WebHooks } from "../../types/WebHooks";

export async function getWebHooks(repoName: string): Promise<WebHooks[] | undefined> {
    try {
        const res = await fetch(`/api/hooks/${repoName}`);

        if (!res.ok) {
            throw new Error(`HTTP error: ${res.status}`);
        }

        const repos: WebHooks[] = await res.json();
        return repos;

    } catch (e) {
        console.error("error :", e);
        return undefined;
    }
}