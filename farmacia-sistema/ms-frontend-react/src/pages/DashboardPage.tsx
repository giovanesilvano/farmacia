import { useEffect, useState } from "react";

import MainLayout from "../layouts/MainLayout";

import {
  estoqueApi,
  vendasApi,
} from "../api/api";

function DashboardPage() {

  const [produtos, setProdutos] = useState([]);

  const [vendas, setVendas] = useState([]);

  const [alertas, setAlertas] = useState([]);

  const [historico, setHistorico] = useState([]);

  useEffect(() => {

    carregarDashboard();

  }, []);

  async function carregarDashboard() {

    try {

      const [
        produtosRes,
        vendasRes,
        alertasRes,
        historicoRes,
      ] = await Promise.all([

        estoqueApi.get("/api/produtos"),

        vendasApi.get("/api/vendas"),

        estoqueApi.get("/api/estoque/alertas"),

        estoqueApi.get("/api/estoque/historico"),
      ]);

      setProdutos(produtosRes.data);

      setVendas(vendasRes.data);

      setAlertas(alertasRes.data);

      setHistorico(historicoRes.data);

    } catch (error) {

      console.error(error);
    }
  }

  const totalVendas = vendas.reduce(
    (acc: number, venda: any) =>
      acc + venda.total,
    0
  );

  return (

    <MainLayout>

      <h1 className="text-4xl font-bold mb-8">
        Dashboard
      </h1>

      <div className="grid grid-cols-4 gap-6 mb-8">

        <div className="bg-white p-6 rounded-2xl shadow">

          <h2 className="text-gray-500 mb-2">
            Produtos
          </h2>

          <p className="text-4xl font-bold">
            {produtos.length}
          </p>

        </div>

        <div className="bg-white p-6 rounded-2xl shadow">

          <h2 className="text-gray-500 mb-2">
            Vendas
          </h2>

          <p className="text-4xl font-bold">
            {vendas.length}
          </p>

        </div>

        <div className="bg-white p-6 rounded-2xl shadow">

          <h2 className="text-gray-500 mb-2">
            Faturamento
          </h2>

          <p className="text-4xl font-bold">
            R$ {totalVendas.toFixed(2)}
          </p>

        </div>

        <div className="bg-white p-6 rounded-2xl shadow">

          <h2 className="text-gray-500 mb-2">
            Alertas
          </h2>

          <p className="text-4xl font-bold text-red-600">
            {alertas.length}
          </p>

        </div>

      </div>

      <div className="grid grid-cols-2 gap-8">

        <div className="bg-white p-8 rounded-2xl shadow">

          <h2 className="text-2xl font-bold mb-6">
            Estoque Baixo
          </h2>

          <div className="space-y-4">

            {alertas.length === 0 ? (

              <p>
                Nenhum alerta.
              </p>

            ) : (

              alertas.map((produto: any) => (

                <div
                  key={produto.id}
                  className="border border-red-300 bg-red-50 p-4 rounded-xl"
                >

                  <h3 className="font-bold">
                    {produto.nome}
                  </h3>

                </div>

              ))

            )}

          </div>

        </div>

        <div className="bg-white p-8 rounded-2xl shadow">

          <h2 className="text-2xl font-bold mb-6">
            Últimas Movimentações
          </h2>

          <div className="space-y-4">

            {historico
              .slice(0, 5)
              .map((mov: any) => (

                <div
                  key={mov.id}
                  className="border p-4 rounded-xl"
                >

                  <p className="font-semibold">
                    Produto ID: {mov.produtoId}
                  </p>

                  <p>
                    Tipo: {mov.tipo}
                  </p>

                  <p>
                    Quantidade: {mov.quantidade}
                  </p>

                </div>

              ))}

          </div>

        </div>

      </div>

    </MainLayout>
  );
}

export default DashboardPage;