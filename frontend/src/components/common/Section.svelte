<script lang="ts">
    import type { Snippet } from 'svelte';

    let {
        title,
        icon = '',
        children
    } = $props<{
        title: string;
        icon?: string;
        children?: Snippet;
    }>();
</script>

<section class="mb-4">
    <h5 class="mb-3"><i class="bi {icon} me-2"></i>{title}</h5>
    {@render children?.()}
</section>