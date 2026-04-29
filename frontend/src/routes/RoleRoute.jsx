import { Navigate } from "react-router-dom";
import useAuthStore from "../store/authStore";

const RoleRoute = ({ children, allowedRoles }) => {
  const role = useAuthStore((state) => state.role);

  if (!allowedRoles.includes(role)) return <Navigate to="/dashboard" replace />;

  return children;
};

export default RoleRoute;