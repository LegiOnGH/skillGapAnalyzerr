import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import useAuthStore from "../store/authStore";
import { ROLES } from "../constants/roles";

const SidebarContent = ({ role, isActive, setSidebarOpen, handleLogout }) => {
  const navLinks = [
    { path: "/dashboard", label: "Dashboard", icon: "⊞" },
    { path: "/skills/analyze", label: "Skill Analyzer", icon: "◎" },
    { path: "/analysis", label: "History", icon: "☰" },
    { path: "/repos", label: "Repo Finder", icon: "⌥" },
  ];

  const adminLinks = [
    { path: "/admin/categories", label: "Categories", icon: "❏" },
    { path: "/admin/roles", label: "Roles", icon: "◈" },
    { path: "/admin/resources", label: "Resources", icon: "◉" },
  ];

  const superAdminLinks = [
    { path: "/super-admin/users", label: "Users", icon: "◎" },
  ];

  return (
    <>
      {/* brand */}
      <div className="px-6 py-6 border-b border-gray-700">
        <h1 className="text-xl font-bold text-white">SkillBridge</h1>
        <p className="text-gray-400 text-xs mt-1">Gap Analysis and Repo Discovery</p>
      </div>

      {/* nav */}
      <nav className="flex-1 px-4 py-6 space-y-1 overflow-y-auto">
        {navLinks.map((link) => (
          <Link
            key={link.path}
            to={link.path}
            onClick={() => setSidebarOpen(false)}
            className={`flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${
              isActive(link.path)
                ? "bg-indigo-600 text-white"
                : "text-gray-400 hover:bg-gray-800 hover:text-white"
            }`}
          >
            <span>{link.icon}</span>
            {link.label}
          </Link>
        ))}

        {(role === ROLES.ADMIN || role === ROLES.SUPER_ADMIN) && (
          <div className="pt-4">
            <p className="text-xs font-semibold text-gray-500 uppercase px-3 mb-2">
              Admin
            </p>
            {adminLinks.map((link) => (
              <Link
                key={link.path}
                to={link.path}
                onClick={() => setSidebarOpen(false)}
                className={`flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${
                  isActive(link.path)
                    ? "bg-indigo-600 text-white"
                    : "text-gray-400 hover:bg-gray-800 hover:text-white"
                }`}
              >
                <span>{link.icon}</span>
                {link.label}
              </Link>
            ))}
          </div>
        )}

        {role === ROLES.SUPER_ADMIN && (
          <div className="pt-4">
            <p className="text-xs font-semibold text-gray-500 uppercase px-3 mb-2">
              Super Admin
            </p>
            {superAdminLinks.map((link) => (
              <Link
                key={link.path}
                to={link.path}
                onClick={() => setSidebarOpen(false)}
                className={`flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium transition-colors ${
                  isActive(link.path)
                    ? "bg-indigo-600 text-white"
                    : "text-gray-400 hover:bg-gray-800 hover:text-white"
                }`}
              >
                <span>{link.icon}</span>
                {link.label}
              </Link>
            ))}
          </div>
        )}
      </nav>

      {/* logout */}
      <div className="px-4 py-4 border-t border-gray-700">
        <button
          onClick={handleLogout}
          className="w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm font-medium text-gray-400 hover:bg-gray-800 hover:text-white transition-colors"
        >
          <span>→</span>
          Logout
        </button>
      </div>
    </>
  );
};

const MainLayout = ({ children }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { role, logout } = useAuthStore();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const isActive = (path) => location.pathname === path;

  return (
    <div className="flex min-h-screen">

      {/* desktop sidebar */}
      <aside className="hidden md:flex w-64 bg-gray-900 flex-col fixed h-full z-30">
        <SidebarContent
          role={role}
          isActive={isActive}
          setSidebarOpen={setSidebarOpen}
          handleLogout={handleLogout}
        />
      </aside>

      {/* mobile overlay */}
      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-20 md:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* mobile sidebar */}
      <aside
        className={`fixed top-0 left-0 h-full w-64 bg-gray-900 flex flex-col z-30 transform transition-transform duration-300 md:hidden ${
          sidebarOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <SidebarContent
          role={role}
          isActive={isActive}
          setSidebarOpen={setSidebarOpen}
          handleLogout={handleLogout}
        />
      </aside>

      {/* main content */}
      <main className="flex-1 md:ml-64 bg-gray-50 min-h-screen">

        {/* mobile topbar */}
        <div className="md:hidden flex items-center justify-between px-4 py-4 bg-white border-b border-gray-200">
          <button
            onClick={() => setSidebarOpen(true)}
            className="text-gray-600 text-xl font-bold"
          >
            ☰
          </button>
          <span className="text-gray-800 font-bold">SkillBridge</span>
          <div className="w-6" />
        </div>

        <div className="p-4 md:p-8">
          {children}
        </div>
      </main>
    </div>
  );
};

export default MainLayout;