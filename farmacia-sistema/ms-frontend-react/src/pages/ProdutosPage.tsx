import { useEffect, useState } from "react";
import { estoqueApi } from "../api/api";
import MainLayout from "../layouts/MainLayout";

type Produto = {
  id: number;
  nome: string;
  codigoBarras: string;
  controlado: boolean;
  estoqueMinimo: number;
  preco: number;
};

function ProdutosPage() {

  const [produtos, setProdutos] = useState<Produto[]>([]);

  const [modalAberto, setModalAberto] = useState(false);

  const [produtoEditando, setProdutoEditando] = useState<number | null>(null);

  const [nome, setNome] = useState("");
  const [codigoBarras, setCodigoBarras] = useState("");
  const [preco, setPreco] = useState("");
  const [estoqueMinimo, setEstoqueMinimo] = useState("");
  const [controlado, setControlado] = useState(false);

  useEffect(() => {
    buscarProdutos();
  }, []);

  async function buscarProdutos() {

    try {

      const response = await estoqueApi.get("/api/produtos");

      setProdutos(response.data);

    } catch (error) {

      console.error(error);
    }
  }

  function limparFormulario() {

    setNome("");
    setCodigoBarras("");
    setPreco("");
    setEstoqueMinimo("");
    setControlado(false);

    setProdutoEditando(null);
  }

  function abrirModalCadastro() {

    limparFormulario();

    setModalAberto(true);
  }

  function abrirModalEdicao(produto: Produto) {

    setProdutoEditando(produto.id);

    setNome(produto.nome);
    setCodigoBarras(produto.codigoBarras);
    setPreco(produto.preco.toString());
    setEstoqueMinimo(produto.estoqueMinimo.toString());
    setControlado(produto.controlado);

    setModalAberto(true);
  }

  function fecharModal() {

    setModalAberto(false);

    limparFormulario();
  }

  async function salvarProduto(e: React.FormEvent) {

    e.preventDefault();

    try {

      const payload = {
        nome,
        codigoBarras,
        preco: Number(preco),
        estoqueMinimo: Number(estoqueMinimo),
        controlado,
      };

      if (produtoEditando !== null) {

        await estoqueApi.put(
          `/api/produtos/${produtoEditando}`,
          payload
        );

      } else {

        await estoqueApi.post(
          "/api/produtos",
          payload
        );
      }

      fecharModal();

      buscarProdutos();

    } catch (error) {

      console.error(error);
    }
  }

  async function excluirProduto(id: number) {

    try {

      await estoqueApi.delete(`/api/produtos/${id}`);

      buscarProdutos();

    } catch (error) {

      console.error(error);
    }
  }

  return (

    <MainLayout>

      <div className="flex items-center justify-between mb-6">

        <h1 className="text-3xl font-bold">
          Produtos
        </h1>

        <button
          onClick={abrirModalCadastro}
          className="bg-blue-600 text-white px-5 py-3 rounded-xl"
        >
          Novo Produto
        </button>

      </div>

      <div className="space-y-4">

        {produtos.map((produto) => (

          <div
            key={produto.id}
            className="bg-white p-5 rounded-xl shadow"
          >

            <div className="flex items-start justify-between">

              <div>

                <h2 className="text-xl font-semibold">
                  {produto.nome}
                </h2>

                <p className="text-gray-600">
                  Código: {produto.codigoBarras}
                </p>

                <p className="text-gray-600">
                  Preço: R$ {produto.preco}
                </p>

                <p className="text-gray-600">
                  Estoque mínimo: {produto.estoqueMinimo}
                </p>

                <p className="text-gray-600">
                  Controlado: {produto.controlado ? "Sim" : "Não"}
                </p>

              </div>

              <div className="flex gap-2">

                <button
                  onClick={() => abrirModalEdicao(produto)}
                  className="bg-yellow-500 text-white px-4 py-2 rounded-lg"
                >
                  Editar
                </button>

                <button
                  onClick={() => excluirProduto(produto.id)}
                  className="bg-red-600 text-white px-4 py-2 rounded-lg"
                >
                  Excluir
                </button>

              </div>

            </div>

          </div>

        ))}

      </div>

      {modalAberto && (

        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">

          <div className="bg-white w-full max-w-lg rounded-2xl p-8">

            <div className="flex items-center justify-between mb-6">

              <h2 className="text-2xl font-bold">

                {produtoEditando !== null
                  ? "Editar Produto"
                  : "Novo Produto"}

              </h2>

              <button
                onClick={fecharModal}
                className="text-gray-500 text-xl"
              >
                ×
              </button>

            </div>

            <form
              onSubmit={salvarProduto}
              className="space-y-4"
            >

              <input
                type="text"
                placeholder="Nome"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                className="w-full border p-3 rounded-lg"
              />

              <input
                  type="text"
                  placeholder="Código de barras"
                  value={codigoBarras}
                  onChange={(e) => setCodigoBarras(e.target.value)}
                  maxLength={13}
                  className="w-full border p-3 rounded-lg"
              />

              <input
                type="number"
                placeholder="Preço"
                value={preco}
                onChange={(e) => setPreco(e.target.value)}
                className="w-full border p-3 rounded-lg"
              />

              <input
                type="number"
                placeholder="Estoque mínimo"
                value={estoqueMinimo}
                onChange={(e) => setEstoqueMinimo(e.target.value)}
                className="w-full border p-3 rounded-lg"
              />

              <label className="flex items-center gap-2">

                <input
                  type="checkbox"
                  checked={controlado}
                  onChange={(e) => setControlado(e.target.checked)}
                />

                Produto controlado

              </label>

              <div className="flex justify-end gap-3 pt-4">

                <button
                  type="button"
                  onClick={fecharModal}
                  className="bg-gray-200 px-4 py-2 rounded-lg"
                >
                  Cancelar
                </button>

                <button
                  type="submit"
                  className="bg-blue-600 text-white px-5 py-2 rounded-lg"
                >
                  {produtoEditando !== null
                    ? "Salvar"
                    : "Cadastrar"}
                </button>

              </div>

            </form>

          </div>

        </div>

      )}

    </MainLayout>
  );
}

export default ProdutosPage;