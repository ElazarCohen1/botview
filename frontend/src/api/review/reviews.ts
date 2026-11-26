import type {Review, ReviewFile} from "../../types/Review";
import {GitProviderEnum} from "../../types/Git";
import {mockReviews} from "./mockReviews";
import {mockReviewFiles} from "./mockReviewFiles";

// Passe à false quand les endpoints /api/reviews existent côté backend
const USE_MOCK = true;

export async function getReviews(
    provider: GitProviderEnum = GitProviderEnum.GITHUB
): Promise<Review[]> {
    if (USE_MOCK) return mockReviews();

    const res = await fetch(`/api/reviews?provider=${encodeURIComponent(provider)}`);
    if (!res.ok) {
        throw new Error(`Impossible de récupérer les reviews : HTTP ${res.status}`);
    }
    return await res.json() as Review[];
}

export async function getReview(id: number): Promise<Review> {
    if (USE_MOCK) {
        const review = mockReviews().find(r => r.id === id);
        if (!review) throw new Error("Review introuvable.");
        return review;
    }

    const res = await fetch(`/api/reviews/${encodeURIComponent(id)}`);
    if (!res.ok) {
        throw new Error(`Impossible de récupérer la review : HTTP ${res.status}`);
    }
    return await res.json() as Review;
}

export async function getReviewFiles(id: number): Promise<ReviewFile[]> {
    if (USE_MOCK) return mockReviewFiles;

    const res = await fetch(`/api/reviews/${encodeURIComponent(id)}/files`);
    if (!res.ok) {
        throw new Error(`Impossible de récupérer les fichiers : HTTP ${res.status}`);
    }
    return await res.json() as ReviewFile[];
}
