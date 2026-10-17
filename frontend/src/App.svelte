<script lang="ts">
  import Router, { push } from 'svelte-spa-router';
  import NavBar from './lib/NavBar.svelte';
  import SideBar from './lib/SideBar.svelte';
  import { routes } from './route';
  import { auth } from './lib/stores..svelte';

  function onConditionsFailed() {
    push('/login');
  }
</script>

{#if auth.isConnected}
  <NavBar />
  <SideBar />
{/if}

<main class:content={auth.isConnected}>
  <Router {routes} {onConditionsFailed} />
</main>

<style>
  .content {
    margin-left: 250px;
    padding: 1.5rem;
  }
  :global(body) {
    padding-top: 56px;
  }
</style>