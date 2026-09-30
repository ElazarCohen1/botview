<script>
    import { router  } from 'svelte-spa-router';

    const links = [
        { href: '/', label: 'Dashboard' },
        { href: '/repositories', label: 'Repositories' },
        { href: '/settings', label: 'Paramètres' },
    ];
</script>

<aside class="sidebar">
    <div class="p-3">
        <h5>BotView</h5>
        <hr />
        <ul class="nav nav-pills flex-column">
            {#each links as { href, label }}
                <li class="nav-item">
                    <a class="nav-link" class:active={router.location === href} href="#{href}">
                        {label}
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
        background-color: #f8f9fa;
        border-right: 1px solid #dee2e6;
        overflow-y: auto;
    }
</style>