import type { Repository } from "../../types/Repository";
import { GitProviderEnum } from "../../types/Git";
import { gitProvider } from '../../lib/stores.svelte';

export async function getRepositories(
    provider: GitProviderEnum = gitProvider.selected ?? GitProviderEnum.GITHUB
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