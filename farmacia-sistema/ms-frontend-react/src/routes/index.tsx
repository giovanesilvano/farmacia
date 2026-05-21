import { Routes, Route } from "react-router-dom";

import ProdutosPage from "../pages/ProdutosPage";
import VendasPage from "../pages/VendasPage";
import EstoquePage from "../pages/EstoquePage";
import DashboardPage from "../pages/DashboardPage";

function AppRoutes() {

  return (

    <Routes>

        <Route
            path="/"
            element={<DashboardPage />}
        />

        <Route
            path="/produtos"
            element={<ProdutosPage />}
        />

        <Route
            path="/vendas"
            element={<VendasPage />}
        />

        <Route
            path="/estoque"
            element={<EstoquePage />}
        />

    </Routes>
  );
}

export default AppRoutes;