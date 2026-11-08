<script lang="ts">

    let {
        bindValue = $bindable()
    }: {
        bindValue: any;
    } = $props();

    import {GitProviderEnum} from "../../types/Git";
</script>

<select aria-label="Provider Git" bind:value={bindValue} class="form-select flex-grow-0" style="width: 9rem">
    {#each Object.values(GitProviderEnum) as p}
        <option value={p}>{GitProviderEnum[p]}</option>
    {/each}
</select>