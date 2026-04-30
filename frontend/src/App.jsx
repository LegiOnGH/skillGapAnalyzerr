import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./routes/ProtectedRoute";
import RoleRoute from "./routes/RoleRoute";
import { ROLES } from "./constants/roles";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import MainLayout from "./layouts/MainLayout";
import Landing from "./pages/Landing";
import Dashboard from "./pages/Dashboard";
import SkillAnalyzer from "./pages/SkillAnalyzer";
import AnalysisHistory from "./pages/AnalysisHistory";
import AnalysisDetail from "./pages/AnalysisDetail";  
import RepoRecommender from "./pages/RepoRecommender";
import Categories from "./pages/admin/Categories";
import Roles from "./pages/admin/Roles";
import Resources from "./pages/admin/Resources";
import Users from "./pages/superAdmin/Users";

const Page = ({ name }) => (
  <div className="p-8 text-xl font-semibold text-gray-700">{name}</div>
);

function App() {
  return (
    <Routes>
      {/* public */}
      <Route path="/" element={<Landing />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />

      {/* protected - any logged in user */}
      <Route path="/dashboard" element={
        <ProtectedRoute>
          <MainLayout><Dashboard /></MainLayout>
        </ProtectedRoute>
      } />
      <Route path="/skills/analyze" element={
        <ProtectedRoute>
          <MainLayout><SkillAnalyzer/></MainLayout>
        </ProtectedRoute>
      } />
      <Route path="/analysis" element={
        <ProtectedRoute>
          <MainLayout><AnalysisHistory /></MainLayout>
        </ProtectedRoute>
      } />
      <Route path="/analysis/:id" element={
        <ProtectedRoute>
          <MainLayout><AnalysisDetail /></MainLayout>
        </ProtectedRoute>
      } />
      <Route path="/repos" element={
        <ProtectedRoute>
          <MainLayout><RepoRecommender /></MainLayout>
        </ProtectedRoute>
      } />

      {/* admin */}
      <Route path="/admin/categories" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
            <MainLayout><Categories /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />
      <Route path="/admin/roles" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
            <MainLayout><Roles /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />
      <Route path="/admin/resources" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
            <MainLayout><Resources /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />

      {/* super admin */}
      <Route path="/super-admin/users" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.SUPER_ADMIN]}>
            <MainLayout><Users /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />

      {/* fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;