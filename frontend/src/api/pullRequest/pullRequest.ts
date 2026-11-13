import type {PullRequest} from "../../types/PullRequest";
import {GitProviderEnum} from "../../types/Git";

export async function getPullRequest(
    repoName: string,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<PullRequest[]> {
    const res = await fetch(`/api/pullrequest/${encodeURIComponent(repoName)}?provider=${encodeURIComponent(provider)}`);

    if (!res.ok) {
        throw new Error(`HTTP error: ${res.status}`);
    }
    return await res.json();
}