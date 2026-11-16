export interface PullRequest {
    pullId: number;
    number: number;
    name: string;
    url: string;
    createdAt: string;
    updatedAt: string;
    repositoryId: number;
    action: string;
}