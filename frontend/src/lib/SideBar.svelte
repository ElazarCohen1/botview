<script>
    import { router } from 'svelte-spa-router';

    const links = [
        { href: '/', label: 'Dashboard', icon: 'bi-speedometer2' },
        { href: '/repositories', label: 'Repositories', icon: 'bi-folder2-open' },
        { href: '/settings', label: 'Paramètres', icon: 'bi-gear' },
    ];
</script>

<aside class="sidebar bg-body-tertiary border-end">
    <div class="p-3">
        <h5>BotView</h5>
        <hr />
        <ul class="nav nav-pills flex-column">
            {#each links as { href, label, icon }}
                <li class="nav-item">
                    <a class="nav-link" class:active={router.location === href} href="#{href}">
                        <i class="bi {icon} me-2"></i>{label}
                    </a>
                </li>
            {/each}
        </ul>
    </div>
</aside>

<style>
    .sidebar {
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        width: 250px;
        overflow-y: auto;
    }
</style>