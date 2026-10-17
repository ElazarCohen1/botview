import { wrap } from 'svelte-spa-router/wrap';
import type { SvelteComponent } from 'svelte';
import {auth} from './lib/stores..svelte'

import Login from './pages/Login.svelte';
import Dashboard from './pages/Dashboard.svelte';
import Repositories from './pages/Repositories.svelte';
import PullRequests from './pages/PullRequests.svelte';

// Condition : on n'accède à la page que si on est connecté
const requireAuth = () => auth.isConnected;

const guarded = (component: any) =>
    wrap({ component, conditions: [requireAuth] });

export const routes = {
    '/login': Login,
    '/': guarded(Dashboard),
    '/repositories': guarded(Repositories),
    '/pull-requests': guarded(PullRequests),
    '*': guarded(Dashboard),
};