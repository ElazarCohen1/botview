// Données de démo : à supprimer quand le backend répondra.
import type {Analysis, AnalysisStatus, Review} from "../../types/Review";

const TOOLS = ["Checkstyle", "PMD", "SpotBugs", "JUnit"];
const STEP = 4000; // durée simulée d'une analyse (ms)
const start = Date.now();

function simulate(
    id: number,
    number: number,
    title: string,
    delay: number,
    failing: string[] = []
): Review {
    const elapsed = Date.now() - start - delay;
    const analyses: Analysis[] = TOOLS.map((tool, i) => {
        let status: AnalysisStatus = "PENDING";
        if (elapsed >= (i + 1) * STEP) {
            status = failing.includes(tool) ? "FAILED" : "DONE";
        } else if (elapsed >= i * STEP) {
            status = "RUNNING";
        }
        const result =
            status === "DONE" ? "Aucun problème détecté." :
            status === "FAILED" ? "2 problèmes détectés (exemple)." : null;
        return {id: id * 10 + i, tool, status, result};
    });

    const finished = analyses.every(a => a.status === "DONE" || a.status === "FAILED");
    const failed = analyses.some(a => a.status === "FAILED");

    return {
        id,
        status: finished ? "DONE" : elapsed >= 0 ? "RUNNING" : "PENDING",
        isBlocking: finished && failed,
        globalComment: finished
            ? (failed ? "Des points sont à corriger avant le merge." : "La PR peut être mergée.")
            : null,
        createdAt: new Date(start + delay - id).toISOString(),
        repositoryName: "botview",
        pullRequestNumber: number,
        pullRequestTitle: title,
        analyses,
    };
}

export function mockReviews(): Review[] {
    return [
        simulate(4, 14, "Ajout du contrôleur des webhooks", 30000),
        simulate(3, 13, "Validation des entrées JSON", 16000),
        simulate(2, 12, "Connexion OAuth GitHub", 0, ["PMD"]),
        simulate(1, 11, "Schéma DuckDB", -60000),
    ];
}
