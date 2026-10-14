export interface Repository {
    id: number;
    name: string;
    url?: string;
    html_url?: string;
    gitProviderType?: string;
    branch?: string;
    default_branch?: string;
    description?: string;
    full_name?: string;
}