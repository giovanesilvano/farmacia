import { Routes, Route } from "react-router-dom";

import ProdutosPage from "../pages/ProdutosPage";
import VendasPage from "../pages/VendasPage";
import EstoquePage from "../pages/EstoquePage";
import DashboardPage from "../pages/DashboardPage";
import LoginPage from "../pages/LoginPage";
import PrivateRoute from "./PrivateRoute";

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

    </Routes>
  );
}

export default AppRoutes;