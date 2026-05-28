import { Link, useLocation, useNavigate } from "react-router-dom";
import { getPerfil } from "../api/auth";

type Props = {
    children: React.ReactNode;
};

function MainLayout({ children }: Props) {

    const location = useLocation();
    const navigate = useNavigate();

    const perfil = getPerfil();

    function logout() {
        localStorage.removeItem("token");
        navigate("/login");
    }

    const menuItems = [
        {
            label: "Dashboard",
            path: "/"
        },
        {
            label: "Estoque",
            path: "/estoque"
        },
        {
            label: "Produtos",
            path: "/produtos"
        },
        {
            label: "Vendas",
            path: "/vendas"
        },
        {
            label: "Usuários",
            path: "/usuarios",
            only: ["GERENTE"]
        },
        {
            label: "Receitas",
            path: "/receitas",
            only: ["FARMACEUTICO"]
        },
        {
            label: "Regulatório",
            path: "/regulatorio",
            only: ["FARMACEUTICO"]
        }
    ];

    const filteredMenu = menuItems.filter(item => {
        if (!item.only) return true;
        return perfil && item.only.includes(perfil);
    });

    return (

        <div className="min-h-screen bg-slate-100 flex">

            <aside className="w-72 bg-slate-950 text-white flex flex-col border-r border-slate-800 shadow-2xl">

                <div className="p-8 border-b border-slate-800">

                    <div className="flex items-center gap-4">

                        <div className="w-12 h-12 rounded-2xl bg-blue-600 flex items-center justify-center text-xl font-bold shadow-lg">
                            F
                        </div>

                        <div>

                            <h1 className="text-xl font-bold">
                                FarmaSys
                            </h1>

                            <p className="text-sm text-slate-400">
                                Sistema de Gestão
                            </p>

                        </div>

                    </div>

                </div>

                <nav className="flex-1 p-5 space-y-2">

                    {filteredMenu.map((item) => {

                        const active = location.pathname === item.path;

                        return (

                            <Link
                                key={item.path}
                                to={item.path}
                                className={`
                                    flex items-center px-4 py-3 rounded-xl transition font-medium
                                    ${active
                                        ? "bg-blue-600 text-white shadow-lg"
                                        : "text-slate-300 hover:bg-slate-800 hover:text-white"
                                    }
                                `}
                            >
                                {item.label}
                            </Link>
                        );
                    })}

                </nav>

                <div className="p-5 border-t border-slate-800">

                    <button
                        onClick={logout}
                        className="w-full bg-red-500 hover:bg-red-600 text-white font-semibold py-3 rounded-xl transition cursor-pointer active:scale-[0.98]"
                    >
                        Sair
                    </button>

                </div>

            </aside>

            <main className="flex-1 p-8 overflow-auto">
                {children}
            </main>

        </div>
    );
}

export default MainLayout;