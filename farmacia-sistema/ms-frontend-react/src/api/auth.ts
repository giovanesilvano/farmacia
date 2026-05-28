import { jwtDecode } from "jwt-decode";

interface TokenPayload {
    sub: string;
    perfil: string;
    id: number;
    exp: number;
}

export function getUserFromToken(): TokenPayload | null {
    const token = localStorage.getItem("token");
    if (!token) return null;

    try {
        return jwtDecode<TokenPayload>(token);
    } catch {
        return null;
    }
}

export function getPerfil(): string | null {
    return getUserFromToken()?.perfil || null;
}