import { writable } from 'svelte/store';
import type {GitUserDto} from '../types/Gituser';

export const user = writable<GitUserDto | null>(null);
export const provider = writable('GitHub');