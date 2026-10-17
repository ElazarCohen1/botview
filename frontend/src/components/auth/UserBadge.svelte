<script lang="ts">
    import { push } from 'svelte-spa-router';
    import { auth } from '../../lib/stores..svelte';

    function logout() {
        auth.logout();
        push('/login');
    }
</script>

<div class="d-flex align-items-center gap-2">
    <img src={auth.user?.avatar_url} alt="avatar" width="64" height="64" class="rounded-circle" />
    <div>{auth.user?.login}</div>
    <button class="btn btn-outline-secondary btn-sm ms-2" onclick={logout}>
        <i class="bi bi-box-arrow-right"></i>
    </button>
</div>