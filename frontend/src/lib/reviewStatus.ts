import type {ReviewStatus} from "../types/Review";

export const STATUS: Record<ReviewStatus, { label: string; color: string }> = {
    PENDING: {label: "En attente", color: "secondary"},
    RUNNING: {label: "En cours", color: "primary"},
    DONE: {label: "Terminée", color: "success"},
    FAILED: {label: "Échec", color: "danger"},
};

export const isFinished = (status: ReviewStatus): boolean =>
    status === "DONE" || status === "FAILED";
