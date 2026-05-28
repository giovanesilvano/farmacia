import { useEffect, useState } from "react";
import { toast } from "sonner";
import MainLayout from "../layouts/MainLayout";

import { regulatorioApi } from "../api/api";

type RegistroSNGPC = {
  id: number;
  produto: string;
  lote: string;
  quantidade: number;
  tipoMovimentacao: string;
  dataRegistro: string;
};

function RegulatorioPage() {

  const [registros, setRegistros] = useState<RegistroSNGPC[]>([]);

  const [novoRegistro, setNovoRegistro] = useState({
    produto: "",
    lote: "",
    quantidade: 0,
    tipoMovimentacao: "ENTRADA"
  });

  useEffect(() => {
    buscarRegistros();
  }, []);

  async function buscarRegistros() {

    try {

      const response =
        await regulatorioApi.get("/api/sngpc");

      setRegistros(response.data);

    } catch (error) {

      console.error(error);
    }
  }

  async function registrarMovimentacao() {

    try {

      await regulatorioApi.post(
        "/api/sngpc/registrar",
        novoRegistro
      );

      await buscarRegistros();

      setNovoRegistro({
        produto: "",
        lote: "",
        quantidade: 0,
        tipoMovimentacao: "ENTRADA"
      });

      toast.success("Registro enviado com sucesso!");

    } catch (error) {

      console.error(error);

      toast.error("Erro ao registrar movimentação");
    }
  }

  return (

    <MainLayout>

      <div className="grid grid-cols-2 gap-8">

        <div>

          <h1 className="text-3xl font-bold mb-6">
            Registros SNGPC
          </h1>

          <div className="space-y-4">

            {registros.map((registro) => (

              <div
                key={registro.id}
                className="bg-white rounded-xl shadow p-5"
              >

                <div className="space-y-2">

                  <h2 className="text-xl font-semibold">
                    {registro.produto}
                  </h2>

                  <p className="text-gray-600">
                    Lote: {registro.lote}
                  </p>

                  <p className="text-gray-600">
                    Quantidade: {registro.quantidade}
                  </p>

                  <p className="text-gray-600">
                    Tipo: {registro.tipoMovimentacao}
                  </p>

                  <p className="text-gray-500 text-sm">
                    {registro.dataRegistro}
                  </p>

                </div>

              </div>

            ))}

          </div>

        </div>

        <div>

          <div className="bg-white rounded-xl shadow p-6 sticky top-8">

            <h2 className="text-2xl font-bold mb-6">
              Novo Registro
            </h2>

            <div className="space-y-4">

              <input
                type="text"
                placeholder="Produto"
                value={novoRegistro.produto}
                onChange={(e) =>
                  setNovoRegistro({
                    ...novoRegistro,
                    produto: e.target.value
                  })
                }
                className="w-full border p-3 rounded-lg"
              />

              <input
                type="text"
                placeholder="Lote"
                value={novoRegistro.lote}
                onChange={(e) =>
                  setNovoRegistro({
                    ...novoRegistro,
                    lote: e.target.value
                  })
                }
                className="w-full border p-3 rounded-lg"
              />

              <input
                type="number"
                placeholder="Quantidade"
                value={novoRegistro.quantidade}
                onChange={(e) =>
                  setNovoRegistro({
                    ...novoRegistro,
                    quantidade: Number(e.target.value)
                  })
                }
                className="w-full border p-3 rounded-lg"
              />

              <select
                value={novoRegistro.tipoMovimentacao}
                onChange={(e) =>
                  setNovoRegistro({
                    ...novoRegistro,
                    tipoMovimentacao: e.target.value
                  })
                }
                className="w-full border p-3 rounded-lg"
              >

                <option value="ENTRADA">
                  ENTRADA
                </option>

                <option value="SAIDA">
                  SAIDA
                </option>

              </select>

              <button
                onClick={registrarMovimentacao}
                className="
                  w-full
                  bg-blue-600
                  text-white
                  py-3
                  rounded-xl
                "
              >
                Registrar
              </button>

            </div>

          </div>

        </div>

      </div>

    </MainLayout>
  );
}

export default RegulatorioPage;