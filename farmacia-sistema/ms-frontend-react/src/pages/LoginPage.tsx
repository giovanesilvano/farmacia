import { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage() {

    const navigate = useNavigate();

    const [login, setLogin] = useState("");
    const [senha, setSenha] = useState("");

    async function fazerLogin(e: React.FormEvent) {

        e.preventDefault();

        const response = await fetch("http://localhost:8084/api/usuarios/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                login,
                senha
            })
        });

        const data = await response.json();

        if (response.ok) {

            localStorage.setItem("token", data.token);

            navigate("/");

        } else {

            alert(data.erro);
        }
    }

    return (
        <div className="relative min-h-screen overflow-hidden bg-slate-950 flex items-center justify-center px-4">

            {/* Glow 1 */}
            <div
                className="
                    absolute
                    -top-40
                    -left-40
                    w-[500px]
                    h-[500px]
                    bg-blue-500/30
                    rounded-full
                    blur-3xl
                "
            />

            {/* Glow 2 */}
            <div
                className="
                    absolute
                    bottom-0
                    right-0
                    w-[450px]
                    h-[450px]
                    bg-cyan-400/20
                    rounded-full
                    blur-3xl
                "
            />

            {/* Grid effect */}
            <div
                className="
                    absolute
                    inset-0
                    bg-[linear-gradient(rgba(255,255,255,0.03)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.03)_1px,transparent_1px)]
                    bg-[size:40px_40px]
                "
            />

            {/* Blur overlay */}
            <div className="absolute inset-0 backdrop-blur-[2px]" />

            <div
                className="
                    relative
                    z-10
                    w-full
                    max-w-md
                    bg-white/10
                    backdrop-blur-xl
                    border
                    border-white/10
                    rounded-3xl
                    shadow-2xl
                    p-8
                "
            >

                <div className="mb-8 text-center">

                    <div
                        className="
                            mx-auto
                            mb-4
                            w-16
                            h-16
                            rounded-2xl
                            bg-blue-500
                            flex
                            items-center
                            justify-center
                            text-white
                            text-2xl
                            font-bold
                            shadow-lg
                        "
                    >
                        F
                    </div>

                    <h1 className="text-3xl font-bold text-white">
                        FarmaSys
                    </h1>

                    <p className="text-slate-300 mt-2">
                        Faça login para acessar o sistema
                    </p>

                </div>

                <form
                    onSubmit={fazerLogin}
                    className="space-y-5"
                >

                    <div>

                        <label className="block text-sm font-medium text-slate-200 mb-1">
                            Login
                        </label>

                        <input
                            type="text"
                            placeholder="Digite seu login"
                            value={login}
                            onChange={(e) => setLogin(e.target.value)}
                            className="
                                w-full
                                bg-white/10
                                border
                                border-white/10
                                text-white
                                placeholder:text-slate-400
                                rounded-xl
                                px-4
                                py-3
                                outline-none
                                focus:ring-2
                                focus:ring-blue-500
                                transition
                            "
                        />

                    </div>

                    <div>

                        <label className="block text-sm font-medium text-slate-200 mb-1">
                            Senha
                        </label>

                        <input
                            type="password"
                            placeholder="Digite sua senha"
                            value={senha}
                            onChange={(e) => setSenha(e.target.value)}
                            className="
                                w-full
                                bg-white/10
                                border
                                border-white/10
                                text-white
                                placeholder:text-slate-400
                                rounded-xl
                                px-4
                                py-3
                                outline-none
                                focus:ring-2
                                focus:ring-blue-500
                                transition
                            "
                        />

                    </div>

                    <button
                        type="submit"
                        className="
                            w-full
                            bg-blue-600
                            hover:bg-blue-700
                            text-white
                            font-semibold
                            py-3
                            rounded-xl
                            transition
                            cursor-pointer
                            active:scale-[0.98]
                        "
                    >
                        Entrar
                    </button>

                </form>

            </div>

        </div>
    );
}

export default LoginPage;