<script lang="ts">

    let {
        bindValue = $bindable()
    }: {
        bindValue: any;
    } = $props();

    import {GitProvider} from "../../types/Git.ts";

    const providers = [
        GitProvider.GITHUB,
        GitProvider.GITLAB,
        GitProvider.BITBUCKET,
    ];

</script>

<select class="form-select flex-grow-0" style="width: 9rem"  aria-label="Provider Git" bind:value={bindValue}>
    {#each providers as p}
        <option value={p}>{GitProvider[p]}</option>
    {/each}
</select>