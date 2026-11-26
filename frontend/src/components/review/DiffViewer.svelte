<script lang="ts">
    import type {LineComment, ReviewFile} from "../../types/Review";
    import {type DiffLine, parsePatch} from "../../lib/parsePatch";
    import CommentBox from "./CommentBox.svelte";

    let {file}: { file: ReviewFile } = $props();

    let lines = $derived(file.patch ? parsePatch(file.patch) : []);

    const matches = (comment: LineComment, line: DiffLine): boolean =>
        comment.line === (comment.side === "OLD" ? line.oldNumber : line.newNumber);

    // Commentaires dont la ligne n'est pas dans le patch : à ne pas perdre
    let orphans = $derived(
        file.comments.filter(c => !lines.some(l => matches(c, l)))
    );
</script>

<div class="card">
    <div class="card-header font-monospace small text-break">{file.path}</div>

    {#if file.patch}
        <div class="overflow-auto">
            <table class="font-monospace small w-100">
                <tbody>
                {#each lines as line, i (i)}
                    {@const comments = file.comments.filter(c => matches(c, line))}
                    <tr
                            class:added={line.type === "ADDED"}
                            class:deleted={line.type === "DELETED"}
                            class:hunk={line.type === "HUNK"}
                            class:problem={comments.length > 0}
                    >
                        <td class="number">{line.oldNumber}</td>
                        <td class="number">{line.newNumber}</td>
                        <td class="sign">{line.type === "ADDED" ? "+" : line.type === "DELETED" ? "-" : ""}</td>
                        <td class="code">{line.content}</td>
                    </tr>
                    {#each comments as comment (comment.id)}
                        <tr>
                            <td colspan="4"><CommentBox {comment}/></td>
                        </tr>
                    {/each}
                {/each}
                </tbody>
            </table>
        </div>
    {:else}
        <p class="text-body-secondary m-3">Aperçu indisponible (fichier binaire ou diff trop gros).</p>
    {/if}

    {#each orphans as comment (comment.id)}
        <CommentBox {comment} showLine/>
    {/each}
</div>

<style>
    .number {
        width: 3.5rem;
        padding: 0 0.5rem;
        text-align: right;
        user-select: none;
        opacity: 0.5;
    }

    .sign {
        width: 1.5rem;
        text-align: center;
        user-select: none;
    }

    .code {
        white-space: pre;
        padding-right: 1rem;
    }

    .added { background: rgba(var(--bs-success-rgb), 0.18); }
    .deleted { background: rgba(var(--bs-danger-rgb), 0.18); }
    .hunk { background: rgba(var(--bs-primary-rgb), 0.15); }
    .problem { box-shadow: inset 4px 0 0 var(--bs-warning); }
</style>
