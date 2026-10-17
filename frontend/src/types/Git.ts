export interface GitUserDto {
    login: string;
    id: number;
    avatar_url: string;
}

export enum GitProvider {
    GITHUB = "GITHUB",
    GITLAB= "GITLAB",
    BITBUCKET= "BITBUCKET",
}