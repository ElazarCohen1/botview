<script>
  import Router from 'svelte-spa-router';
  import NavBar from './lib/NavBar.svelte';
  import SideBar from './lib/SideBar.svelte';
  import Home from './pages/Home.svelte';
  import PullRequests from './pages/PullRequests.svelte';
  import Repositories from './pages/Repositories.svelte';
  import Dashboard from './pages/Dashboard.svelte';
  const routes = {
    '/': Dashboard,
    '/repositories':Repositories,
    '/pull-requests': PullRequests,
    
  };
</script>

<NavBar />
<SideBar />

<main class="content">
  <Router {routes} />
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