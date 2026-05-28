import { useEffect, useState } from "react";
import MainLayout from "../layouts/MainLayout";
import { vendasApi } from "../api/api";
import { toast } from "sonner";

type Receita = {
  id: number;
  numeroCRM: string;
  nomeMedico: string;
  nomePaciente: string;
  dataEmissao: string;
  valida: boolean;
};

function ReceitaPage() {

  const [numeroCRM, setNumeroCRM] = useState("");
  const [nomeMedico, setNomeMedico] = useState("");
  const [nomePaciente, setNomePaciente] = useState("");
  const [dataEmissao, setDataEmissao] = useState("");

  const [receitas, setReceitas] = useState<Receita[]>([]);

  useEffect(() => {
    listarReceitas();
  }, []);

  async function listarReceitas() {
    try {
      const response = await vendasApi.get("/api/receitas");
      setReceitas(response.data);
    } catch (error) {
      console.error(error);
      toast.error("Erro ao listar receitas");
    }
  }

  async function criarReceita() {
    try {

      const payload = {
        numeroCRM,
        nomeMedico,
        nomePaciente,
        dataEmissao,
        valida: true
      };

      await vendasApi.post("/api/receitas", payload);

      toast.success("Receita criada com sucesso!");

      setNumeroCRM("");
      setNomeMedico("");
      setNomePaciente("");
      setDataEmissao("");

      await listarReceitas();

    } catch (error) {
      console.error(error);
      toast.error("Erro ao criar receita");
    }
  }

  return (
    <MainLayout>

      <div className="max-w-xl mx-auto bg-white p-6 rounded-xl shadow">

        <h1 className="text-2xl font-bold mb-6">
          Criar Receita
        </h1>

        <div className="space-y-4">

          <input
            className="w-full border p-3 rounded"
            placeholder="Número CRM"
            value={numeroCRM}
            onChange={(e) => setNumeroCRM(e.target.value)}
          />

          <input
            className="w-full border p-3 rounded"
            placeholder="Nome do Médico"
            value={nomeMedico}
            onChange={(e) => setNomeMedico(e.target.value)}
          />

          <input
            className="w-full border p-3 rounded"
            placeholder="Nome do Paciente"
            value={nomePaciente}
            onChange={(e) => setNomePaciente(e.target.value)}
          />

          <input
            type="date"
            className="w-full border p-3 rounded"
            value={dataEmissao}
            onChange={(e) => setDataEmissao(e.target.value)}
          />

          <button
            onClick={criarReceita}
            className="w-full bg-green-600 text-white py-3 rounded-xl"
          >
            Criar Receita
          </button>

        </div>
      </div>

      {/* LISTA */}
      <div className="max-w-xl mx-auto mt-8 bg-white p-6 rounded-xl shadow">

        <h2 className="text-xl font-bold mb-4">
          Receitas cadastradas
        </h2>

        <div className="space-y-3">

          {receitas.map((r) => (
            <div key={r.id} className="border p-3 rounded">

              <p><b>ID:</b> {r.id}</p>
              <p><b>CRM:</b> {r.numeroCRM}</p>
              <p><b>Médico:</b> {r.nomeMedico}</p>
              <p><b>Paciente:</b> {r.nomePaciente}</p>
              <p><b>Data:</b> {r.dataEmissao}</p>

            </div>
          ))}

        </div>

      </div>

    </MainLayout>
  );
}

export default ReceitaPage;