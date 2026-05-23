import { Link } from "react-router-dom";

type Props = {
  children: React.ReactNode;
};

function MainLayout({ children }: Props) {

  return (

    <div className="min-h-screen flex bg-gray-100">

      <aside className="w-64 bg-gray-900 text-white p-6">

        <h1 className="text-2xl font-bold mb-10">
          Farmácia
        </h1>

        <nav className="flex flex-col gap-4">

          <Link to="/">
            Dashboard
          </Link>

          <Link to="/estoque">
            Estoque
          </Link>

          <Link to="/produtos">
            Produtos
          </Link>

          <Link to="/vendas">
            Vendas
          </Link>

          <Link to="/usuarios">
            Usuários
          </Link>

          <Link to="/regulatorio">
            Regulatório
          </Link>

        </nav>

      </aside>

      <main className="flex-1 p-8">

        {children}

      </main>

    </div>
  );
}

export default MainLayout;