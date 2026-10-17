import type { GitUserDto } from "../../types/Git";
import { GitProviderEnum } from "../../types/Git";

export async function saveToken(
    provider: GitProviderEnum,
    token: string
): Promise<void> {
    const res = await fetch("/api/token", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "same-origin",
        body: JSON.stringify({ provider, token })
    });

    if (!res.ok) {
        throw new Error(`Erreur lors de l'enregistrement du token : HTTP ${res.status}`);
    }
}

export async function getUser(
    provider: GitProvider
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