<script>
    import { auth } from '../../lib/stores..svelte';
    import UserBadge from '../auth/UserBadge.svelte';
    import RepoSelector from '../repos/RepoSelector.svelte';
</script>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
    <h3 class="m-0">Dashboard</h3>
    <div class="d-flex flex-wrap align-items-center gap-3">
        {#if auth.isConnected}
            <RepoSelector />
            <UserBadge />
        {:else}
<!--            <ProviderLoginButton />-->
        {/if}
    </div>
</div>