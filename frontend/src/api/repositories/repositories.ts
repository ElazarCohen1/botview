import type {Repository} from "../../types/Repository";
import {GitProviderEnum} from "../../types/Git";

export async function getGitRepositories(
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<Repository[] | undefined> {
    try {
        const res = await fetch(`/api/repositories/git?provider=${encodeURIComponent(provider)}`);

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

export async function getRepositories(
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<Repository[]> {
    const res = await fetch(`/api/repositories?provider=${provider}`);
    console.warn(res)
    if (!res.ok) {
        throw new Error(`Failed to fetch repositories: ${res.status}`);
    }

    return await res.json();
}

export async function addRepository(
    repoName: string,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<void> {
    const res = await fetch(`/api/repositories/${repoName}?provider=${provider}`, {
        method: "POST"
    });
    if (!res.ok) {
        const error = await res.json();
        throw new Error(error.message || error.error || "Une erreur est survenue.");
    }
}

export async function removeWebhook(repoId: string, provider: GitProviderEnum = GitProviderEnum.GITHUB) {
    const res = await fetch(`/api/repositories/${repoId}?provider=${provider}`, {
        method: "PATCH"
    });
    if (!res.ok) {
        throw new Error(`Failed to remove webhook: ${res.status}`);
    }
}

/**
 * delete the repo in the db use just
 * @param repoId
 * @param provider
 */
export async function deleteRepositoryFromDb(
    repoId: number,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<boolean> {

    const res = await fetch(
        `/api/repositories/${encodeURIComponent(repoId)}?provider=${provider}`,
        {method: "DELETE"}
    );
    if (!res.ok) {
        throw new Error(`Failed to delete repos: ${res.status}`);
    }
    return true;
}
