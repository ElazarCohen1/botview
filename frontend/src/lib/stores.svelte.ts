import { GitProviderEnum, type GitUserDto } from '../types/Git';

const STORAGE_KEY = 'botview.provider';

function loadStoredProvider(): GitProviderEnum | null {
    try {
        const value = localStorage.getItem(STORAGE_KEY);
        return value && value in GitProviderEnum ? (value as GitProviderEnum) : null;
    } catch {
        return null;
    }
}

let selectedProvider = $state<GitProviderEnum | null>(loadStoredProvider());
let currentUser = $state<GitUserDto | null>(null);
let ready = $state(false);

export const gitProvider = {
    get selected() {
        return selectedProvider;
    },

    select(provider: GitProviderEnum) {
        selectedProvider = provider;
        try {
            localStorage.setItem(STORAGE_KEY, provider);
        } catch { /* ignore */ }
    },

    reset() {
        selectedProvider = null;
        try {
            localStorage.removeItem(STORAGE_KEY);
        } catch { /* ignore */ }
    }
};

export const auth = {
    get user() {
        return currentUser;
    },

    get isConnected() {
        return currentUser !== null;
    },

    get ready() {
        return ready;
    },

    setReady() {
        ready = true;
    },

    setUser(user: GitUserDto | null) {
        currentUser = user;
    },

    logout() {
        currentUser = null;
        gitProvider.reset();
    }
};