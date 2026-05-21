import { useEffect, useState } from "react";

import MainLayout from "../layouts/MainLayout";

import { estoqueApi } from "../api/api";

type Produto = {
  id: number;
  nome: string;
  preco: number;
  estoqueMinimo?: number;
};

type Movimentacao = {
  id: number;
  produtoId: number;
  tipo: string;
  quantidade: number;
  dataHora: string;
  motivo: string;
};

type ProdutoComSaldo = {
  id: number;
  nome: string;
  preco: number;
  estoqueMinimo?: number;
  saldo?: number;
};

function EstoquePage() {

    const [produtos, setProdutos] = useState<
        ProdutoComSaldo[]
    >([]);

  const [historico, setHistorico] = useState<Movimentacao[]>([]);

  const [alertas, setAlertas] = useState<Produto[]>([]);

  const [produtoId, setProdutoId] = useState("");

  const [quantidade, setQuantidade] = useState("");

  const [lote, setLote] = useState("");

  const [dataValidade, setDataValidade] = useState("");

  const [saidaProdutoId, setSaidaProdutoId] = useState("");

  const [saidaQuantidade, setSaidaQuantidade] = useState("");

  const [motivo, setMotivo] = useState("");

  useEffect(() => {

    buscarProdutos();

    buscarHistorico();

    buscarAlertas();

  }, []);

async function buscarProdutos() {

  try {

    const response = await estoqueApi.get(
      "/api/produtos"
    );

    const produtosComSaldo = await Promise.all(

      response.data.map(async (produto: ProdutoComSaldo) => {

        try {

          const saldoResponse = await estoqueApi.get(
            `/api/estoque/saldo/${produto.id}`
          );

          return {
            ...produto,
            saldo: saldoResponse.data,
          };

        } catch {

          return {
            ...produto,
            saldo: 0,
          };
        }
      })
    );

    setProdutos(produtosComSaldo);

  } catch (error) {

    console.error(error);
  }
}

  async function buscarHistorico() {

    try {

      const response = await estoqueApi.get(
        "/api/estoque/historico"
      );

      setHistorico(response.data);

    } catch (error) {

      console.error(error);
    }
  }

  async function buscarAlertas() {

    try {

      const response = await estoqueApi.get(
        "/api/estoque/alertas"
      );

      setAlertas(response.data);

    } catch (error) {

      console.error(error);
    }
  }

  async function entradaEstoque(
    e: React.FormEvent
  ) {

    e.preventDefault();

    try {

      await estoqueApi.post(

        "/api/estoque/entrada",

        {
          produtoId: Number(produtoId),
          quantidade: Number(quantidade),
          dataValidade,
          lote,
        }
      );

      alert("Entrada registrada!");

      setProdutoId("");
      setQuantidade("");
      setLote("");
      setDataValidade("");

      buscarHistorico();

      buscarAlertas();

    } catch (error) {

      console.error(error);

      alert("Erro ao registrar entrada");
    }
  }

  async function registrarSaida(
    e: React.FormEvent
  ) {

    e.preventDefault();

    try {

      await estoqueApi.post(

        "/api/estoque/saida",

        null,

        {
          params: {
            produtoId: Number(saidaProdutoId),
            quantidade: Number(saidaQuantidade),
            motivo,
          },
        }
      );

      alert("Saída registrada!");

      setSaidaProdutoId("");
      setSaidaQuantidade("");
      setMotivo("");

      buscarHistorico();

      buscarAlertas();

    } catch (error) {

      console.error(error);

      alert("Erro ao registrar saída");
    }
  }

  return (

    <MainLayout>

      <h1 className="text-3xl font-bold mb-8">
        Controle de Estoque
      </h1>

        <div className="bg-white p-8 rounded-2xl shadow mb-8">

        <h2 className="text-2xl font-bold mb-6">
            Produtos em Estoque
        </h2>

        <div className="overflow-x-auto">

            <table className="w-full">

            <thead>

                <tr className="border-b">

                <th className="text-left py-3">
                    Produto
                </th>

                <th className="text-left py-3">
                    Preço
                </th>

                <th className="text-left py-3">
                    Saldo
                </th>

                </tr>

            </thead>

            <tbody>

                {produtos.map((produto) => (

                <tr
                    key={produto.id}
                    className="border-b"
                >

                    <td className="py-3">
                    {produto.nome}
                    </td>

                    <td className="py-3">
                    R$ {produto.preco}
                    </td>

                    <td className="py-3 font-bold">
                    {produto.saldo ?? 0}
                    </td>

                </tr>

                ))}

            </tbody>

            </table>

        </div>

        </div>

      <div className="grid grid-cols-2 gap-8 mb-8">

        <form
          onSubmit={entradaEstoque}
          className="bg-white p-8 rounded-2xl shadow space-y-4"
        >

          <h2 className="text-2xl font-bold">
            Entrada de Estoque
          </h2>

          <select
            value={produtoId}
            onChange={(e) => setProdutoId(e.target.value)}
            className="w-full border p-3 rounded-lg"
          >

            <option value="">
              Selecione um produto
            </option>

            {produtos.map((produto) => (

              <option
                key={produto.id}
                value={produto.id}
              >
                {produto.nome}
              </option>

            ))}

          </select>

          <input
            type="number"
            placeholder="Quantidade"
            value={quantidade}
            onChange={(e) => setQuantidade(e.target.value)}
            className="w-full border p-3 rounded-lg"
          />

          <input
            type="text"
            placeholder="Lote"
            value={lote}
            onChange={(e) => setLote(e.target.value)}
            className="w-full border p-3 rounded-lg"
          />

          <input
            type="date"
            value={dataValidade}
            onChange={(e) => setDataValidade(e.target.value)}
            className="w-full border p-3 rounded-lg"
          />

          <button
            type="submit"
            className="bg-blue-600 text-white px-5 py-3 rounded-xl"
          >
            Registrar Entrada
          </button>

        </form>

        <form
          onSubmit={registrarSaida}
          className="bg-white p-8 rounded-2xl shadow space-y-4"
        >

          <h2 className="text-2xl font-bold">
            Saída de Estoque
          </h2>

          <select
            value={saidaProdutoId}
            onChange={(e) => setSaidaProdutoId(e.target.value)}
            className="w-full border p-3 rounded-lg"
          >

            <option value="">
              Selecione um produto
            </option>

            {produtos.map((produto) => (

              <option
                key={produto.id}
                value={produto.id}
              >
                {produto.nome}
              </option>

            ))}

          </select>

          <input
            type="number"
            placeholder="Quantidade"
            value={saidaQuantidade}
            onChange={(e) => setSaidaQuantidade(e.target.value)}
            className="w-full border p-3 rounded-lg"
          />

          <input
            type="text"
            placeholder="Motivo"
            value={motivo}
            onChange={(e) => setMotivo(e.target.value)}
            className="w-full border p-3 rounded-lg"
          />

          <button
            type="submit"
            className="bg-red-600 text-white px-5 py-3 rounded-xl"
          >
            Registrar Saída
          </button>

        </form>

      </div>

      <div className="bg-white p-8 rounded-2xl shadow mb-8">

        <h2 className="text-2xl font-bold mb-6">
          Alertas de Estoque Mínimo
        </h2>

        {alertas.length === 0 ? (

          <p>
            Nenhum alerta encontrado.
          </p>

        ) : (

          <div className="space-y-4">

            {alertas.map((produto) => (

              <div
                key={produto.id}
                className="border border-red-400 bg-red-50 p-4 rounded-xl"
              >

                <h3 className="font-bold text-red-700">
                  {produto.nome}
                </h3>

                <p className="text-red-600">
                  Estoque abaixo do mínimo
                </p>

              </div>

            ))}

          </div>

        )}

      </div>

      <div className="bg-white p-8 rounded-2xl shadow">

        <h2 className="text-2xl font-bold mb-6">
          Histórico de Movimentações
        </h2>

        <div className="overflow-x-auto">

          <table className="w-full">

            <thead>

              <tr className="border-b">

                <th className="text-left py-3">
                  Produto ID
                </th>

                <th className="text-left py-3">
                  Tipo
                </th>

                <th className="text-left py-3">
                  Quantidade
                </th>

                <th className="text-left py-3">
                  Motivo
                </th>

                <th className="text-left py-3">
                  Data
                </th>

              </tr>

            </thead>

            <tbody>

              {historico.map((mov) => (

                <tr
                  key={mov.id}
                  className="border-b"
                >

                  <td className="py-3">
                    {mov.produtoId}
                  </td>

                  <td className="py-3">
                    {mov.tipo}
                  </td>

                  <td className="py-3">
                    {mov.quantidade}
                  </td>

                  <td className="py-3">
                    {mov.motivo}
                  </td>

                  <td className="py-3">
                    {new Date(
                      mov.dataHora
                    ).toLocaleString()}
                  </td>

                </tr>

              ))}

            </tbody>

          </table>

        </div>

      </div>

    </MainLayout>
  );
}

export default EstoquePage;