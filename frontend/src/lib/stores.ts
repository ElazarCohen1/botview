import { writable } from 'svelte/store';
import type {GitUserDto} from '../types/Gituser';
import {GitProvider} from "../types/Git";

export const user = writable<GitUserDto | null>(null);
