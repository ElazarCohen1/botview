<script lang="ts">

    let {
        bindValue = $bindable()
    }: {
        bindValue: any;
    } = $props();

    import {GitProviderEnum} from "../../types/Git.ts";

    const providers = [
        GitProviderEnum.GITHUB,
        GitProviderEnum.GITLAB,
        GitProviderEnum.BITBUCKET,
    ];

</script>

<select class="form-select flex-grow-0" style="width: 9rem"  aria-label="Provider Git" bind:value={bindValue}>
    {#each providers as p}
        <option value={p}>{GitProviderEnum[p]}</option>
    {/each}
</select>