import axios from "axios";

export const estoqueApi = axios.create({
  baseURL: "http://localhost:8081",
});

export const vendasApi = axios.create({
  baseURL: "http://localhost:8082",
});

export const regulatorioApi = axios.create({
  baseURL: "http://localhost:8083",
});

export const usuariosApi = axios.create({
  baseURL: "http://localhost:8084",
});