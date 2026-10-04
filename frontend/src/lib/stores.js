import { writable } from 'svelte/store';
import { user as mockUser } from './mock.js';

export const user = writable(mockUser);
export const provider = writable('GitHub');