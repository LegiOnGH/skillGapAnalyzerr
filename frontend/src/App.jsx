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
          <MainLayout><Page name="Analysis History" /></MainLayout>
        </ProtectedRoute>
      } />
      <Route path="/analysis/:id" element={
        <ProtectedRoute>
          <MainLayout><Page name="Analysis Detail" /></MainLayout>
        </ProtectedRoute>
      } />
      <Route path="/repos" element={
        <ProtectedRoute>
          <MainLayout><Page name="Repo Recommender" /></MainLayout>
        </ProtectedRoute>
      } />

      {/* admin */}
      <Route path="/admin" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
            <MainLayout><Page name="Admin Dashboard" /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />
      <Route path="/admin/categories" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
            <MainLayout><Page name="Categories" /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />
      <Route path="/admin/roles" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
            <MainLayout><Page name="Roles" /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />
      <Route path="/admin/resources" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.ADMIN, ROLES.SUPER_ADMIN]}>
            <MainLayout><Page name="Resources" /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />

      {/* super admin */}
      <Route path="/super-admin" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.SUPER_ADMIN]}>
            <MainLayout><Page name="Super Admin Dashboard" /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />
      <Route path="/super-admin/users" element={
        <ProtectedRoute>
          <RoleRoute allowedRoles={[ROLES.SUPER_ADMIN]}>
            <MainLayout><Page name="Users" /></MainLayout>
          </RoleRoute>
        </ProtectedRoute>
      } />

      {/* fallback */}
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;