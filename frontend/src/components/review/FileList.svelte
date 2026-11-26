<script lang="ts">
    import type {FileStatus, ReviewFile} from "../../types/Review";

    let {files, selectedId, onSelect}: {
        files: ReviewFile[];
        selectedId: number;
        onSelect: (id: number) => void;
    } = $props();

    const LETTERS: Record<FileStatus, { letter: string; color: string }> = {
        ADDED: {letter: "A", color: "success"},
        MODIFIED: {letter: "M", color: "warning"},
        DELETED: {letter: "D", color: "danger"},
        RENAMED: {letter: "R", color: "info"},
    };

    const fileName = (path: string): string => path.slice(path.lastIndexOf("/") + 1);
    const folder = (path: string): string => path.slice(0, Math.max(path.lastIndexOf("/"), 0));
</script>

<div class="list-group">
    {#each files as file (file.id)}
        <button
                class="list-group-item list-group-item-action d-flex align-items-center gap-2"
                class:active={file.id === selectedId}
                onclick={() => onSelect(file.id)}
                title={file.path}
                type="button"
        >
            <span class="fw-bold text-{LETTERS[file.status].color}">{LETTERS[file.status].letter}</span>
            <span class="flex-grow-1 text-truncate">
                <span class="d-block text-truncate">{fileName(file.path)}</span>
                <span class="d-block small opacity-75 text-truncate">{folder(file.path)}</span>
            </span>
            {#if file.comments.length > 0}
                <span class="badge text-bg-danger" title="Problèmes">{file.comments.length}</span>
            {/if}
        </button>
    {/each}
</div>
