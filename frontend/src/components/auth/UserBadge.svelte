<script lang="ts">
    import {push} from "svelte-spa-router";
    import {auth} from "../../lib/stores.svelte";

    function logout() {
        auth.logout();
        push("/login");
    }
</script>

<div class="d-flex align-items-center gap-2">
    <img
            alt="avatar"
            class="rounded-circle"
            height="64"
            src={auth.user?.avatar_url}
            width="64"
    />
    <div>{auth.user?.login}</div>
    <button
            aria-label="Se déconnecter"
            class="btn btn-outline-secondary btn-sm ms-2"
            onclick={logout}
    >
        <i class="bi bi-box-arrow-right"></i>
    </button>
</div>
