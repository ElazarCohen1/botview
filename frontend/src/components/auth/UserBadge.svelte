<script>
    import { user, provider } from '../../lib/stores';
</script>

<div class="d-flex align-items-center gap-2">
    <img src={$user.avatar_url} alt="avatar picture" width="64" height="64" class="rounded-circle" />
    <div class="lh-1">
        <div>{$user.login}</div>
        <span class="badge text-bg-secondary">{$provider}</span>
    </div>
    <button class="btn btn-outline-secondary btn-sm ms-2" on:click={() => user.set(null)}>
        <i class="bi bi-box-arrow-right"></i>
    </button>
</div>