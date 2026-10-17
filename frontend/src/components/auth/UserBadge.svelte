<script>
    import { auth } from "../../lib/stores..svelte";

    const user = auth.user;
</script>

<div class="d-flex align-items-center gap-2">
    <img src={user.avatar_url} alt="avatar picture" width="64" height="64" class="rounded-circle" />
    <div class="lh-1">
        <div>{user.login}</div>
    </div>
    <button class="btn btn-outline-secondary btn-sm ms-2" on:click={() => auth.logout()}>
        <i class="bi bi-box-arrow-right"></i>
    </button>
</div>