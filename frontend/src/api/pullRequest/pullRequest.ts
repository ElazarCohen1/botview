import type { PullRequest } from "../../types/PullRequest";

export async function getPullRequest(repoName : string): Promise<PullRequest[] | undefined> {
    try {
        const res = await fetch(`/api/pullRequest/${repoName}`);

        if (!res.ok) {
            throw new Error(`HTTP error: ${res.status}`);
        }

        const repos: PullRequest[] = await res.json();
        return repos;

    } catch (e) {
        console.error("error :", e);
        return undefined;
    }
}