import type {GitUserDto} from "../../types/Git";
import {GitProvider} from "../../types/Git";

export async function saveToken(
    provider :GitProvider,
    token : string
):void {
    try{
        const res = await fetch("api/token",{
            method:'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials:'same-origin',
            body:JSON.stringify({provider,token})
        });
    }catch (e){
        console.error(e);
    }
}


export async function getUser(): Promise<GitUserDto | undefined> {
    try {
        const res = await fetch('/api/user');

        if (!res.ok) {
            throw new Error(`HTTP error: ${res.status}`);
        }

        const user: GitUserDto = await res.json();
        return user;

    } catch (e) {
        console.error("error :", e);
        return undefined;
    }
}