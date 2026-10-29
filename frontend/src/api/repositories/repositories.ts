import type { Repository } from "../../types/Repository";
import { GitProviderEnum } from "../../types/Git";

export async function getRepositories(
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<Repository[] | undefined> {
    try {
        const res = await fetch(`/api/repositories?provider=${encodeURIComponent(provider)}`);

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

export async function getRepositoriesFromDb(
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<Repository[] | undefined> {
    try {
        const res = await fetch(`/api/repositories/database?provider=${encodeURIComponent(provider)}`);

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

export async function addRepositoryToDb(
    repoName: string,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<boolean> {
    try {
        const res = await fetch(`/api/repositories/database/addrepo/${encodeURIComponent(repoName)}?provider=${encodeURIComponent(provider)}`, {
            method: "POST"
        });
        return res.ok;
    } catch (e) {
        console.error("error :", e);
        return false;
    }
}

export async function deleteRepositoryFromDb(
    repoName: string,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<boolean> {
    try {
        const res = await fetch(
            `/api/repositories/database/delete/${encodeURIComponent(repoName)}?provider=${encodeURIComponent(provider)}`,
            { method: "DELETE" }
        );
        return res.ok;
    } catch (e) {
        console.error("error :", e);
        return false;
    }
}
