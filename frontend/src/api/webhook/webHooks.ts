import type {WebHooks} from "../../types/WebHooks";
import {GitProviderEnum} from "../../types/Git";

export async function getWebHooks(
    repoId: number,
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<WebHooks[]> {

    const res = await fetch(`/api/hooks/${repoId}?provider=${provider}`);
    if (!res.ok) {
        throw new Error(`HTTP error: ${res.status}`);
    }
    return await res.json();

}