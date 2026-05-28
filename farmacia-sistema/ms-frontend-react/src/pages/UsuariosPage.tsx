import { useEffect, useState } from "react";
import { toast } from "sonner";
import MainLayout from "../layouts/MainLayout";

import { usuariosApi } from "../api/api";

type Usuario = {
  id: number;
  login: string;
  perfil: string;
};

type NovoUsuario = {
  login: string;
  senha: string;
  perfil: string;
};

function UsuariosPage() {

  const [usuarios, setUsuarios] = useState<Usuario[]>([]);

  const [novoUsuario, setNovoUsuario] = useState<NovoUsuario>({
    login: "",
    senha: "",
    perfil: "ATENDENTE"
  });

  useEffect(() => {
    buscarUsuarios();
  }, []);

  async function buscarUsuarios() {

    try {

      const response =
        await usuariosApi.get("/api/usuarios");

      setUsuarios(response.data);

    } catch (error) {

      console.error(error);
    }
  }

  async function criarUsuario() {

    try {

      await usuariosApi.post(
        "/api/usuarios",
        novoUsuario
      );

      await buscarUsuarios();

      setNovoUsuario({
        login: "",
        senha: "",
        perfil: "ATENDENTE"
      });

      toast.success("Usuário criado com sucesso!");

    } catch (error) {

      console.error(error);

      toast.error("Erro ao criar usuário");
    }
  }

  return (

    <MainLayout>

      <div className="grid grid-cols-2 gap-8">

        <div>

          <h1 className="text-3xl font-bold mb-6">
            Usuários
          </h1>

          <div className="space-y-4">

            {usuarios.map((usuario) => (

              <div
                key={usuario.id}
                className="bg-white rounded-xl shadow p-5"
              >

                <div className="flex items-center justify-between">

                  <div>

                    <h2 className="text-xl font-semibold">
                      {usuario.login}
                    </h2>

                    <p className="text-gray-600">
                      Perfil: {usuario.perfil}
                    </p>

                  </div>

                </div>

              </div>

            ))}

          </div>

        </div>

        <div>

          <div className="bg-white rounded-xl shadow p-6 sticky top-8">

            <h2 className="text-2xl font-bold mb-6">
              Novo Usuário
            </h2>

            <div className="space-y-4">

              <input
                type="text"
                placeholder="Login"
                value={novoUsuario.login}
                onChange={(e) =>
                  setNovoUsuario({
                    ...novoUsuario,
                    login: e.target.value
                  })
                }
                className="w-full border p-3 rounded-lg"
              />

              <input
                type="password"
                placeholder="Senha"
                value={novoUsuario.senha}
                onChange={(e) =>
                  setNovoUsuario({
                    ...novoUsuario,
                    senha: e.target.value
                  })
                }
                className="w-full border p-3 rounded-lg"
              />

              <select
                value={novoUsuario.perfil}
                onChange={(e) =>
                  setNovoUsuario({
                    ...novoUsuario,
                    perfil: e.target.value
                  })
                }
                className="w-full border p-3 rounded-lg"
              >

                <option value="FARMACEUTICO">
                  FARMACEUTICO
                </option>

                <option value="GERENTE">
                  GERENTE
                </option>

                <option value="ATENDENTE">
                  ATENDENTE
                </option>

              </select>

              <button
                onClick={criarUsuario}
                className="
                  w-full
                  bg-blue-600
                  text-white
                  py-3
                  rounded-xl
                "
              >
                Criar Usuário
              </button>

            </div>

          </div>

        </div>

      </div>

    </MainLayout>
  );
}

export default UsuariosPage;