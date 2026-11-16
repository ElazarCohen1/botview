import type {PullRequest} from "../../types/PullRequest";

export async function getPullRequest(
    repoId: number,
): Promise<PullRequest[]> {
    const res = await fetch(`/api/pullrequest/${encodeURIComponent(repoId)}`);

    if (!res.ok) {
        throw new Error(`HTTP error: ${res.status}`);
    }
    return await res.json();
}

export async function getNbPullRequest(): Promise<number> {
    const res = await fetch(`/api/pullrequest/count`);

    if (!res.ok) {
        throw new Error(`HTTP error: ${res.status}`);
    }
    return await res.json();
}