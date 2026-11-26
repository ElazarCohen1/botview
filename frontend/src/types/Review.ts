// Mêmes valeurs que les ENUM review_status et analysis_status de DatabaseInitializer
export type ReviewStatus = "PENDING" | "RUNNING" | "DONE" | "FAILED";
export type AnalysisStatus = ReviewStatus;

export interface Analysis {
    id: number;
    tool: string;
    status: AnalysisStatus;
    result: string | null;
}

export interface Review {
    id: number;
    status: ReviewStatus;
    isBlocking: boolean;
    globalComment: string | null;
    createdAt: string; // ISO 8601 (Instant côté Java)
    repositoryName: string; // même valeur que dans #/pull-requests?repo=...
    pullRequestNumber: number;
    pullRequestTitle: string;
    analyses: Analysis[];
}


export type FileStatus = "ADDED" | "MODIFIED" | "DELETED" | "RENAMED";

export interface LineComment {
    id: number;
    line: number; // numéro de ligne dans le fichier
    side: "OLD" | "NEW"; // OLD : ligne supprimée, NEW : tout le reste
    comment: string;
}

export interface ReviewFile {
    id: number;
    path: string;
    status: FileStatus;
    patch: string | null; // champ "patch" de l'API GitHub
    comments: LineComment[];
}
