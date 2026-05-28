import { useEffect, useMemo, useState } from "react";

import MainLayout from "../layouts/MainLayout";
import { toast } from "sonner";
import { estoqueApi, vendasApi } from "../api/api";

type Produto = {
  id: number;
  nome: string;
  codigoBarras: string;
  controlado: boolean;
  estoqueMinimo: number;
  preco: number;
};

type Receita = {
  id: number;
  numeroCRM: string;
  nomeMedico: string;
  nomePaciente: string;
};

type ItemCarrinho = {
  produtoId: number;
  nomeProduto: string;
  controlado: boolean;
  quantidade: number;
  precoUnitario: number;
  receitaId: number;
  subtotal: number;
};

function VendasPage() {
  const [produtos, setProdutos] = useState<Produto[]>([]);
  const [receitas, setReceitas] = useState<Receita[]>([]);

  const [cliente, setCliente] = useState("");
  const [receitaSelecionada, setReceitaSelecionada] = useState<number | null>(null);

  const [carrinho, setCarrinho] = useState<ItemCarrinho[]>([]);

  useEffect(() => {
    buscarProdutos();
    buscarReceitas();
  }, []);

  async function buscarProdutos() {
    try {
      const response = await estoqueApi.get("/api/produtos");
      setProdutos(response.data);
    } catch (error) {
      console.error(error);
    }
  }

  async function buscarReceitas() {
    try {
      const response = await vendasApi.get("/api/receitas");
      setReceitas(response.data);
    } catch (error) {
      console.error(error);
    }
  }

  function adicionarProduto(produto: Produto) {
    const itemExistente = carrinho.find(
      (item) => item.produtoId === produto.id
    );

    if (itemExistente) {
      const novoCarrinho = carrinho.map((item) => {
        if (item.produtoId === produto.id) {
          const novaQuantidade = item.quantidade + 1;

          return {
            ...item,
            quantidade: novaQuantidade,
            subtotal: novaQuantidade * item.precoUnitario,
          };
        }

        return item;
      });

      setCarrinho(novoCarrinho);
      return;
    }

    const novoItem: ItemCarrinho = {
      produtoId: produto.id,
      nomeProduto: produto.nome,
      controlado: produto.controlado,
      quantidade: 1,
      precoUnitario: produto.preco,
      receitaId: 0,
      subtotal: produto.preco,
    };

    setCarrinho([...carrinho, novoItem]);
  }

  function removerItem(produtoId: number) {
    const novoCarrinho = carrinho.filter(
      (item) => item.produtoId !== produtoId
    );

    setCarrinho(novoCarrinho);
  }

  const total = useMemo(() => {
    return carrinho.reduce((acc, item) => acc + item.subtotal, 0);
  }, [carrinho]);

  async function finalizarVenda() {
    try {
      const temControlado = carrinho.some((i) => i.controlado);

      if (temControlado && !receitaSelecionada) {
        toast.error("Selecione uma receita para produtos controlados");
        return;
      }

      const payload = {
        cliente,
        status: "ABERTA",
        total,
        itens: carrinho.map((item) => ({
          ...item,
          receitaId: item.controlado ? receitaSelecionada : null,
        })),
      };

      await vendasApi.post("/api/vendas", payload);

      toast.success("Venda realizada com sucesso!");

      setCarrinho([]);
      setCliente("");
      setReceitaSelecionada(null);
    } catch (error) {
      console.error(error);
      toast.error("Erro ao finalizar venda");
    }
  }

  return (
    <MainLayout>
      <div className="grid grid-cols-2 gap-8">

        <div>
          <h1 className="text-3xl font-bold mb-6">Produtos</h1>

          <div className="space-y-4">
            {produtos.map((produto) => (
              <div
                key={produto.id}
                className="bg-white p-5 rounded-xl shadow"
              >
                <div className="flex items-center justify-between">
                  <div>
                    <h2 className="text-xl font-semibold">{produto.nome}</h2>
                    <p className="text-gray-600">R$ {produto.preco}</p>
                    <p className="text-gray-600">
                      Controlado: {produto.controlado ? "Sim" : "Não"}
                    </p>
                  </div>

                  <button
                    onClick={() => adicionarProduto(produto)}
                    className="bg-blue-600 text-white px-4 py-2 rounded-lg"
                  >
                    Adicionar
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div>
          <div className="bg-white rounded-xl shadow p-6 sticky top-8">
            <h2 className="text-2xl font-bold mb-6">Carrinho</h2>

            <input
              type="text"
              placeholder="Nome do cliente"
              value={cliente}
              onChange={(e) => setCliente(e.target.value)}
              className="w-full border p-3 rounded-lg mb-4"
            />

            {carrinho.some((i) => i.controlado) && (
              <select
                className="w-full border p-3 rounded-lg mb-6"
                value={receitaSelecionada ?? ""}
                onChange={(e) =>
                  setReceitaSelecionada(Number(e.target.value))
                }
              >
                <option value="">Selecione a receita</option>

                {receitas.map((r) => (
                  <option key={r.id} value={r.id}>
                    {r.nomePaciente} - {r.numeroCRM}
                  </option>
                ))}
              </select>
            )}

            <div className="space-y-4">
              {carrinho.map((item) => (
                <div
                  key={item.produtoId}
                  className="border rounded-lg p-4"
                >
                  <div className="flex items-start justify-between">
                    <div>
                      <h3 className="font-semibold">{item.nomeProduto}</h3>
                      <p className="text-gray-600">
                        Quantidade: {item.quantidade}
                      </p>
                      <p className="text-gray-600">
                        Subtotal: R$ {item.subtotal}
                      </p>
                    </div>

                    <button
                      onClick={() => removerItem(item.produtoId)}
                      className="bg-red-600 text-white px-3 py-1 rounded"
                    >
                      X
                    </button>
                  </div>
                </div>
              ))}
            </div>

            <div className="mt-8 border-t pt-6">
              <h3 className="text-2xl font-bold">
                Total: R$ {total.toFixed(2)}
              </h3>

              <button
                onClick={finalizarVenda}
                disabled={carrinho.length === 0}
                className="w-full mt-6 bg-green-600 text-white py-3 rounded-xl disabled:opacity-50"
              >
                Finalizar Venda
              </button>
            </div>
          </div>
        </div>

      </div>
    </MainLayout>
  );
}

export default VendasPage;