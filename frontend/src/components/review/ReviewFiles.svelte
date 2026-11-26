<script lang="ts">
    import {getReviewFiles} from "../../api/review/reviews";
    import type {ReviewFile} from "../../types/Review";
    import FileList from "./FileList.svelte";
    import DiffViewer from "./DiffViewer.svelte";

    let {reviewId, finished}: { reviewId: number; finished: boolean } = $props();

    let files: ReviewFile[] = $state([]);
    let selectedId: number | null = $state(null);
    let loading: boolean = $state(true);
    let error: string = $state("");

    let selected = $derived(files.find(f => f.id === selectedId) ?? files[0]);
    // $derived : le polling ne relance le chargement que si la valeur change
    let done = $derived(finished);

    // Chargé à l'ouverture, puis une fois quand la review se termine
    $effect(() => {
        void done;
        let cancelled = false;

        getReviewFiles(reviewId)
            .then((data) => {
                if (cancelled) return;
                files = data;
                error = "";
                loading = false;
            })
            .catch((e) => {
                if (cancelled) return;
                error = e instanceof Error ? e.message : "Erreur inattendue.";
                loading = false;
            });

        return () => {
            cancelled = true;
        };
    });
</script>

{#if error}
    <div class="alert alert-danger">{error}</div>
{/if}

{#if loading}
    <p>Chargement des fichiers...</p>
{:else if !selected}
    <p class="text-body-secondary">Aucun fichier pour cette review.</p>
{:else}
    <div class="row g-3">
        <div class="col-12 col-xl-3">
            <FileList {files} onSelect={(id) => selectedId = id} selectedId={selected.id}/>
        </div>
        <div class="col-12 col-xl-9">
            {#key selected.id}
                <DiffViewer file={selected}/>
            {/key}
        </div>
    </div>
{/if}
