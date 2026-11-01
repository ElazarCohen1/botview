import type { WebHooks } from "../../types/WebHooks";
import { GitProviderEnum } from "../../types/Git";

export async function getWebHooks(
    repoName: string,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<WebHooks[] | undefined> {
    try {
        const res = await fetch(`/api/hooks/${encodeURIComponent(repoName)}?provider=${encodeURIComponent(provider)}`);

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
export async function createWebHook(
    repoName: string,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<boolean> {
    try {
        const res = await fetch(`/api/hooks/${encodeURIComponent(repoName)}?provider=${encodeURIComponent(provider)}`, {
            method: "POST"
        });
        return res.ok;
    } catch (e) {
        console.error("error :", e);
        return false;
    }
}