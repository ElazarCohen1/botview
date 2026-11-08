<script>
    import {availableRepos} from '../../lib/mock.js';

    let selected = '';
</script>

<div class="input-group" style="width: 22rem">
    <select bind:value={selected} class="form-select" disabled={!availableRepos.length}>
        <option value="">
            {availableRepos.length ? 'Choisir un repo…' : 'Aucun repo disponible'}
        </option>
        {#each availableRepos as r}
            <option value={r.id}>{r.name}</option>
        {/each}
    </select>
    <button class="btn btn-primary" disabled={!selected}>Connecter</button>
</div>