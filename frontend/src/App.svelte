<script lang="ts">
    import {onMount} from 'svelte';
    import Router, {push} from 'svelte-spa-router';
    import NavBar from './lib/NavBar.svelte';
    import SideBar from './lib/SideBar.svelte';
    import {routes} from './route';
    import {auth, gitProvider} from './lib/stores.svelte';
    import {getUser} from './api/login/gitApi';

    function onConditionsFailed() {
        push('/login');
    }

    onMount(async () => {
        const provider = gitProvider.selected;
        if (provider) {
            try {
                auth.setUser(await getUser(provider));
            } catch {
                gitProvider.reset();
            }
        }
        auth.setReady();
    });
</script>

{#if auth.ready}
    {#if auth.isConnected}
        <NavBar/>
        <SideBar/>
    {/if}

    <main class:content={auth.isConnected}>
        <Router {routes} {onConditionsFailed}/>
    </main>
{/if}

<style>
    .content {
        margin-left: 250px;
        padding: 1.5rem;
    }

    :global(body) {
        padding-top: 100px;
    }
</style>