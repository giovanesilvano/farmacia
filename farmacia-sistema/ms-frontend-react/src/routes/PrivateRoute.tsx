import { Navigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

interface Props {
    children: React.ReactNode;
}

interface TokenPayload {
    exp: number;
}

function PrivateRoute({ children }: Props) {

    const token = localStorage.getItem("token");

    if (!token) {
        return <Navigate to="/login" />;
    }

    try {

        const decoded = jwtDecode<TokenPayload>(token);

        const agora = Date.now() / 1000;

        if (decoded.exp < agora) {

            localStorage.removeItem("token");

            return <Navigate to="/login" />;
        }

    } catch {

        localStorage.removeItem("token");

        return <Navigate to="/login" />;
    }

    return children;
}

export default PrivateRoute;