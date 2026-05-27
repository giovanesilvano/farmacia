import axios from "axios";

function criarApi(baseURL: string) {

    const api = axios.create({
        baseURL
    });

    api.interceptors.request.use((config) => {

        const token = localStorage.getItem("token");

        if (token) {

            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    });

    return api;
}

export const estoqueApi = criarApi("http://localhost:8081");

export const vendasApi = criarApi("http://localhost:8082");

export const regulatorioApi = criarApi("http://localhost:8083");

export const usuariosApi = criarApi("http://localhost:8084");