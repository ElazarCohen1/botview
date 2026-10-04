<script>
    import { user, provider } from '../../lib/stores.js';
</script>

<div class="d-flex align-items-center gap-2">
    <img src={$user.avatar} alt="" width="32" height="32" class="rounded-circle" />
    <div class="lh-1">
        <div>{$user.name}</div>
        <span class="badge text-bg-secondary">{$provider}</span>
    </div>
    <button class="btn btn-outline-secondary btn-sm ms-2" on:click={() => user.set(null)}>
        <i class="bi bi-box-arrow-right"></i>
    </button>
</div>