import type { GitUserDto } from "../../types/Git";
import { GitProviderEnum } from "../../types/Git";

export async function getUser(
    provider: GitProviderEnum
): Promise<GitUserDto> {
    const res = await fetch(
        `/api/user?provider=${encodeURIComponent(provider)}`,
        { credentials: "same-origin" }
    );

    if (!res.ok) {
        throw new Error(`Impossible de récupérer l'utilisateur : HTTP ${res.status}`);
    }

    return await res.json() as GitUserDto;
}