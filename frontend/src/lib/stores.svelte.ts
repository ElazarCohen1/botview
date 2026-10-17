import { GitProviderEnum } from '../types/Git';

let selectedProvider = $state<GitProvider | null>(null);
let currentUser = $state<GitUserDto | null>(null);

export const gitProvider = {
    get selected() {
        return selectedProvider;
    },

    select(provider: GitProviderEnum) {
        selectedProvider = provider;
    },

    reset() {
        selectedProvider = null;
    }
};


export const auth = {
    get user() {
        return currentUser;
    },

    get isConnected() {
        return currentUser !== null;
    },

    setUser(user: GitUserDto | null) {
        currentUser = user;
    },

    logout() {
        currentUser = null;
        selectedProvider = null;
    }
};