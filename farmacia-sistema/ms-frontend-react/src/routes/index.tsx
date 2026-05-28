import { Routes, Route } from "react-router-dom";

import ProdutosPage from "../pages/ProdutosPage";
import VendasPage from "../pages/VendasPage";
import EstoquePage from "../pages/EstoquePage";
import DashboardPage from "../pages/DashboardPage";
import LoginPage from "../pages/LoginPage";
import PrivateRoute from "./PrivateRoute";
import UsuariosPage from "../pages/UsuariosPage";
import RegulatorioPage from "../pages/RegulatorioPage";
import ReceitaPage from "../pages/ReceitaPage";

function AppRoutes() {

  return (

    <Routes>

        <Route
            path="/login"
            element={<LoginPage />}
        />

        <Route
            path="/"
            element={
                <PrivateRoute>
                    <DashboardPage />
                </PrivateRoute>
            }
        />

        <Route
            path="/produtos"
            element={
                <PrivateRoute>
                    <ProdutosPage />
                </PrivateRoute>
            }
        />

        <Route
            path="/vendas"
            element={
                <PrivateRoute>
                    <VendasPage />
                </PrivateRoute>
            }   
        />

        <Route
            path="/estoque"
            element={
                <PrivateRoute>
                    <EstoquePage />
                </PrivateRoute>
            }
        />

        <Route
            path="/usuarios"
            element={
                <PrivateRoute>
                    <UsuariosPage />
                </PrivateRoute>
            }
        />

        <Route
            path="/receitas"
            element={
                <PrivateRoute>
                    <ReceitaPage />
                </PrivateRoute>
            }
        />

        <Route
            path="/regulatorio"
            element={
                <PrivateRoute>
                    <RegulatorioPage />
                </PrivateRoute>
            }
        />

    </Routes>
  );
}

export default AppRoutes;