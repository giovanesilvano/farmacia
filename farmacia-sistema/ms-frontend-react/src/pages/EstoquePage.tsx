import { useEffect, useState } from "react";

import MainLayout from "../layouts/MainLayout";
import { toast } from "sonner";
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

            if (!produtoId) {
                toast.error("Selecione um produto");
                return;
            }

            if (!quantidade) {
                toast.error("Informe a quantidade");
                return;
            }

            if (!lote) {
                toast.error("Informe o lote");
                return;
            }

            if (!dataValidade) {
                toast.error("Informe a data de validade");
                return;
            }

            await estoqueApi.post(

                "/api/estoque/entrada",

                {
                    produtoId: Number(produtoId),
                    quantidade: Number(quantidade),
                    dataValidade,
                    lote,
                }
            );

            toast.success("Entrada registrada!");

            setProdutoId("");
            setQuantidade("");
            setLote("");
            setDataValidade("");

            buscarHistorico();
            buscarProdutos();
            buscarAlertas();

        } catch (error) {

            console.error(error);

            toast.error("Erro ao registrar entrada");
        }
    }

    async function registrarSaida(e: React.FormEvent) {
        e.preventDefault();

        try {

            if (!saidaProdutoId) {
                toast.error("Selecione um produto");
                return;
            }

            if (!saidaQuantidade) {
                toast.error("Informe a quantidade");
                return;
            }

            if (!motivo.trim()) {
                toast.error("Informe o motivo");
                return;
            }

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

            toast.success("Saída registrada!");

            setSaidaProdutoId("");
            setSaidaQuantidade("");
            setMotivo("");

            buscarHistorico();

            buscarAlertas();

        } catch (error) {

            console.error(error);

            toast.error("Erro ao registrar saída");
        }
    }

    return (

        <MainLayout>

            <div className="space-y-8">

                {/* Header */}

                <div className="flex items-center justify-between">

                    <div>

                        <h1 className="text-4xl font-bold text-slate-900">
                            Controle de Estoque
                        </h1>

                        <p className="text-slate-500 mt-2">
                            Gerencie entradas, saídas e movimentações do estoque
                        </p>

                    </div>

                </div>

                {/* Cards resumo */}

                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

                    <div className="bg-white rounded-3xl p-6 shadow-sm border border-slate-200">

                        <p className="text-slate-500 text-sm">
                            Produtos cadastrados
                        </p>

                        <h2 className="text-4xl font-bold mt-2 text-slate-900">
                            {produtos.length}
                        </h2>

                    </div>

                    <div className="bg-white rounded-3xl p-6 shadow-sm border border-slate-200">

                        <p className="text-slate-500 text-sm">
                            Alertas de estoque
                        </p>

                        <h2 className="text-4xl font-bold mt-2 text-red-500">
                            {alertas.length}
                        </h2>

                    </div>

                    <div className="bg-white rounded-3xl p-6 shadow-sm border border-slate-200">

                        <p className="text-slate-500 text-sm">
                            Movimentações
                        </p>

                        <h2 className="text-4xl font-bold mt-2 text-slate-900">
                            {historico.length}
                        </h2>

                    </div>

                </div>

                {/* Produtos */}

                <div className="bg-white rounded-3xl shadow-sm border border-slate-200 overflow-hidden">

                    <div className="p-6 border-b border-slate-200 flex items-center justify-between">

                        <div>

                            <h2 className="text-2xl font-bold text-slate-900">
                                Produtos em Estoque
                            </h2>

                            <p className="text-slate-500 mt-1">
                                Visualize os produtos e seus saldos atuais
                            </p>

                        </div>

                    </div>

                    <div className="overflow-x-auto">

                        <table className="w-full">

                            <thead className="bg-slate-50">

                                <tr>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Produto
                                    </th>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Preço
                                    </th>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Saldo
                                    </th>

                                </tr>

                            </thead>

                            <tbody>

                                {produtos.map((produto) => (

                                    <tr
                                        key={produto.id}
                                        className="border-t border-slate-100 hover:bg-slate-50 transition"
                                    >

                                        <td className="px-6 py-4 font-medium text-slate-800">
                                            {produto.nome}
                                        </td>

                                        <td className="px-6 py-4 text-slate-600">
                                            R$ {produto.preco}
                                        </td>

                                        <td className="px-6 py-4">

                                            <span
                                                className="
                                                bg-blue-100
                                                text-blue-700
                                                px-3
                                                py-1
                                                rounded-full
                                                text-sm
                                                font-semibold
                                            "
                                            >
                                                {produto.saldo ?? 0}
                                            </span>

                                        </td>

                                    </tr>

                                ))}

                            </tbody>

                        </table>

                    </div>

                </div>

                {/* Forms */}

                <div className="grid grid-cols-1 xl:grid-cols-2 gap-8">

                    {/* Entrada */}

                    <form
                        onSubmit={entradaEstoque}
                        className="
                        bg-white
                        rounded-3xl
                        shadow-sm
                        border
                        border-slate-200
                        p-8
                        space-y-5
                    "
                    >

                        <div>

                            <h2 className="text-2xl font-bold text-slate-900">
                                Entrada de Estoque
                            </h2>

                            <p className="text-slate-500 mt-1">
                                Registre novas entradas de produtos
                            </p>

                        </div>

                        <select
                            value={produtoId}
                            onChange={(e) => setProdutoId(e.target.value)}
                            className="
                            w-full
                            border
                            border-slate-200
                            rounded-2xl
                            px-4
                            py-3
                            outline-none
                            focus:ring-2
                            focus:ring-blue-500
                        "
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
                            className="
                            w-full
                            border
                            border-slate-200
                            rounded-2xl
                            px-4
                            py-3
                            outline-none
                            focus:ring-2
                            focus:ring-blue-500
                        "
                        />

                        <input
                            type="text"
                            placeholder="Lote"
                            value={lote}
                            onChange={(e) => setLote(e.target.value)}
                            className="
                            w-full
                            border
                            border-slate-200
                            rounded-2xl
                            px-4
                            py-3
                            outline-none
                            focus:ring-2
                            focus:ring-blue-500
                        "
                        />

                        <input
                            type="date"
                            value={dataValidade}
                            onChange={(e) => setDataValidade(e.target.value)}
                            className="
                            w-full
                            border
                            border-slate-200
                            rounded-2xl
                            px-4
                            py-3
                            outline-none
                            focus:ring-2
                            focus:ring-blue-500
                        "
                        />

                        <button
                            type="submit"
                            className="
                            w-full
                            bg-blue-600
                            hover:bg-blue-700
                            text-white
                            font-semibold
                            py-3
                            rounded-2xl
                            transition
                            cursor-pointer
                        "
                        >
                            Registrar Entrada
                        </button>

                    </form>

                    {/* Saída */}

                    <form
                        onSubmit={registrarSaida}
                        className="
                        bg-white
                        rounded-3xl
                        shadow-sm
                        border
                        border-slate-200
                        p-8
                        space-y-5
                    "
                    >

                        <div>

                            <h2 className="text-2xl font-bold text-slate-900">
                                Saída de Estoque
                            </h2>

                            <p className="text-slate-500 mt-1">
                                Registre retiradas de produtos
                            </p>

                        </div>

                        <select
                            value={saidaProdutoId}
                            onChange={(e) => setSaidaProdutoId(e.target.value)}
                            className="
                            w-full
                            border
                            border-slate-200
                            rounded-2xl
                            px-4
                            py-3
                            outline-none
                            focus:ring-2
                            focus:ring-red-500
                        "
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
                            className="
                            w-full
                            border
                            border-slate-200
                            rounded-2xl
                            px-4
                            py-3
                            outline-none
                            focus:ring-2
                            focus:ring-red-500
                        "
                        />

                        <input
                            type="text"
                            placeholder="Motivo"
                            value={motivo}
                            onChange={(e) => setMotivo(e.target.value)}
                            className="
                            w-full
                            border
                            border-slate-200
                            rounded-2xl
                            px-4
                            py-3
                            outline-none
                            focus:ring-2
                            focus:ring-red-500
                        "
                        />

                        <button
                            type="submit"
                            className="
                            w-full
                            bg-red-500
                            hover:bg-red-600
                            text-white
                            font-semibold
                            py-3
                            rounded-2xl
                            transition
                            cursor-pointer
                        "
                        >
                            Registrar Saída
                        </button>

                    </form>

                </div>

                {/* Alertas */}

                {alertas.length > 0 && (

                    <div className="bg-white rounded-3xl shadow-sm border border-slate-200 p-8">

                        <h2 className="text-2xl font-bold text-slate-900 mb-6">
                            Alertas de Estoque
                        </h2>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

                            {alertas.map((produto) => (

                                <div
                                    key={produto.id}
                                    className="
                                    border
                                    border-red-200
                                    bg-red-50
                                    rounded-2xl
                                    p-5
                                "
                                >

                                    <h3 className="font-bold text-red-700">
                                        {produto.nome}
                                    </h3>

                                    <p className="text-red-600 mt-1 text-sm">
                                        Estoque abaixo do mínimo permitido
                                    </p>

                                </div>

                            ))}

                        </div>

                    </div>

                )}

                {/* Histórico */}

                <div className="bg-white rounded-3xl shadow-sm border border-slate-200 overflow-hidden">

                    <div className="p-6 border-b border-slate-200">

                        <h2 className="text-2xl font-bold text-slate-900">
                            Histórico de Movimentações
                        </h2>

                    </div>

                    <div className="overflow-x-auto">

                        <table className="w-full">

                            <thead className="bg-slate-50">

                                <tr>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Produto ID
                                    </th>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Tipo
                                    </th>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Quantidade
                                    </th>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Motivo
                                    </th>

                                    <th className="text-left px-6 py-4 text-sm font-semibold text-slate-600">
                                        Data
                                    </th>

                                </tr>

                            </thead>

                            <tbody>

                                {historico.map((mov) => (

                                    <tr
                                        key={mov.id}
                                        className="border-t border-slate-100 hover:bg-slate-50 transition"
                                    >

                                        <td className="px-6 py-4">
                                            {mov.produtoId}
                                        </td>

                                        <td className="px-6 py-4">

                                            <span
                                                className={`
                                                px-3
                                                py-1
                                                rounded-full
                                                text-xs
                                                font-bold
                                                ${mov.tipo === "ENTRADA"
                                                        ? "bg-green-100 text-green-700"
                                                        : "bg-red-100 text-red-700"
                                                    }
                                            `}
                                            >
                                                {mov.tipo}
                                            </span>

                                        </td>

                                        <td className="px-6 py-4">
                                            {mov.quantidade}
                                        </td>

                                        <td className="px-6 py-4">
                                            {mov.motivo}
                                        </td>

                                        <td className="px-6 py-4 text-slate-500">
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

            </div>

        </MainLayout>
    );
}

export default EstoquePage;