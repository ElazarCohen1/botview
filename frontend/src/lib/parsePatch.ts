export type DiffLineType = "HUNK" | "ADDED" | "DELETED" | "CONTEXT";

export interface DiffLine {
    type: DiffLineType;
    content: string;
    oldNumber: number | null;
    newNumber: number | null;
}

// En-tête d'un bloc : @@ -ancienDébut,n +nouveauDébut,n @@
const HUNK = /^@@ -(\d+)(?:,\d+)? \+(\d+)(?:,\d+)? @@/;

/** Transforme un patch (format unified diff) en lignes numérotées. */
export function parsePatch(patch: string): DiffLine[] {
    const lines: DiffLine[] = [];
    let oldNumber = 0;
    let newNumber = 0;

    for (const raw of patch.replace(/\n$/, "").split("\n")) {
        const hunk = HUNK.exec(raw);
        if (hunk) {
            oldNumber = Number(hunk[1]);
            newNumber = Number(hunk[2]);
            lines.push({type: "HUNK", content: raw, oldNumber: null, newNumber: null});
            continue;
        }
        if (raw.startsWith("\\")) continue; // "\ No newline at end of file"

        const content = raw.slice(1);
        if (raw.startsWith("+")) {
            lines.push({type: "ADDED", content, oldNumber: null, newNumber: newNumber++});
        } else if (raw.startsWith("-")) {
            lines.push({type: "DELETED", content, oldNumber: oldNumber++, newNumber: null});
        } else {
            lines.push({type: "CONTEXT", content, oldNumber: oldNumber++, newNumber: newNumber++});
        }
    }
    return lines;
}
