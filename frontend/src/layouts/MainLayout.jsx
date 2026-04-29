import { Link, useLocation, useNavigate } from "react-router-dom";
import useAuthStore from "../store/authStore";
import { ROLES } from "../constants/roles";

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

const MainLayout = ({ children }) => {
  const location = useLocation();
  const navigate = useNavigate();
  const { role, logout } = useAuthStore();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const isActive = (path) => location.pathname === path;

  return (
    <div className="flex min-h-screen">
      {/* sidebar */}
      <aside className="w-64 bg-gray-900 flex flex-col fixed h-full">

        {/* brand */}
        <div className="px-6 py-6 border-b border-gray-700">
          <h1 className="text-xl font-bold text-white">SkillBridge</h1>
          <p className="text-gray-400 text-xs mt-1">Gap Analysis and Repo Discovery</p>
        </div>

        {/* nav */}
        <nav className="flex-1 px-4 py-6 space-y-1 overflow-y-auto">

          {/* main links */}
          {navLinks.map((link) => (
            <Link
              key={link.path}
              to={link.path}
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

          {/* admin section */}
          {(role === ROLES.ADMIN || role === ROLES.SUPER_ADMIN) && (
            <div className="pt-4">
              <p className="text-xs font-semibold text-gray-500 uppercase px-3 mb-2">
                Admin
              </p>
              {adminLinks.map((link) => (
                <Link
                  key={link.path}
                  to={link.path}
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

          {/* super admin section */}
          {role === ROLES.SUPER_ADMIN && (
            <div className="pt-4">
              <p className="text-xs font-semibold text-gray-500 uppercase px-3 mb-2">
                Super Admin
              </p>
              {superAdminLinks.map((link) => (
                <Link
                  key={link.path}
                  to={link.path}
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
      </aside>

      {/* main content — offset by sidebar width */}
      <main className="flex-1 ml-64 bg-gray-50 min-h-screen p-8">
        {children}
      </main>
    </div>
  );
};

export default MainLayout;