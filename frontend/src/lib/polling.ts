/**
 * Exécute `task` tout de suite, puis `delay` ms après la fin de chaque appel
 * (deux requêtes ne se chevauchent donc jamais).
 * `task` renvoie false pour arrêter. La fonction retournée arrête le polling.
 */
export function startPolling<T>(
    task: () => Promise<T>,
    onData: (data: T) => boolean,
    onError: (message: string) => void,
    delay = 3000
): () => void {
    let stopped = false;
    let timer: ReturnType<typeof setTimeout> | undefined;

    async function tick() {
        let again = true;
        try {
            const data = await task();
            if (stopped) return; // la page a été quittée entre-temps
            again = onData(data);
        } catch (e) {
            if (stopped) return;
            onError(e instanceof Error ? e.message : "Erreur inattendue.");
        }
        if (again) timer = setTimeout(tick, delay);
    }

    void tick();

    return () => {
        stopped = true;
        clearTimeout(timer);
    };
}
