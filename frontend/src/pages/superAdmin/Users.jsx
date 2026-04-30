import { useState } from "react";
import { useAllUsers, useUpdateUserRole, useDeleteUser } from "../../features/superAdmin/hooks";
import useAuthStore from "../../store/authStore";
import { unwrapPage } from "../../utils/pagination";
import { getErrorMessage } from "../../utils/errorHandler";

const ROLE_OPTIONS = [
  { value: "USER", label: "User" },
  { value: "ADMIN", label: "Admin" },
  { value: "SUPER_ADMIN", label: "Super Admin" },
];

const getRoleBadge = (role) => {
  const map = {
    SUPER_ADMIN: "bg-purple-100 text-purple-700",
    ADMIN: "bg-indigo-100 text-indigo-700",
    USER: "bg-gray-100 text-gray-600",
  };
  return map[role] ?? "bg-gray-100 text-gray-600";
};

const getRoleLabel = (role) => {
  const map = {
    SUPER_ADMIN: "Super Admin",
    ADMIN: "Admin",
    USER: "User",
  };
  return map[role] ?? role;
};

const Users = () => {
  const [page, setPage] = useState(0);
  const size = 10;
  const [error, setError] = useState("");

  const currentUserId = useAuthStore((state) => state.userId);
  const { data, isLoading } = useAllUsers(page, size);
  const { mutate: updateRole } = useUpdateUserRole();
  const { mutate: deleteUser } = useDeleteUser();

  const { content: users, totalPages } = unwrapPage(data);

  const handleRoleChange = (userId, newRole) => {
    updateRole(
      { userId, role: newRole },
      {
        onError: (err) => {
          setError(getErrorMessage(err));
          setTimeout(() => setError(""), 3000);
        },
      }
    );
  };

  const handleDelete = (userId, userName) => {
    if (confirm(`Delete user "${userName}"? This cannot be undone.`)) {
      deleteUser(userId, {
        onError: (err) => {
          setError(getErrorMessage(err));
          setTimeout(() => setError(""), 3000);
        },
      });
    }
  };

  return (
    <div className="max-w-5xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Users</h1>
        <p className="text-gray-500 mt-1">Manage all registered users.</p>
      </div>

      {error && (
        <div className="bg-red-50 text-red-600 text-sm rounded-lg p-3 mb-4">
          {error}
        </div>
      )}

      <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
        {isLoading ? (
          <div className="p-6 space-y-3">
            {[1, 2, 3, 4, 5].map((i) => (
              <div key={i} className="h-12 bg-gray-100 rounded animate-pulse" />
            ))}
          </div>
        ) : users.length === 0 ? (
          <div className="p-12 text-center text-gray-400 text-sm">
            No users found.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead className="bg-gray-50 border-b border-gray-200">
                <tr>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                    Username
                  </th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                    Role
                  </th>
                  <th className="text-left px-6 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {users.map((user) => (
                  <tr key={user.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 font-medium text-gray-800 whitespace-nowrap">
                      {user.userName}
                      {user.id === currentUserId && (
                        <span className="ml-2 text-xs text-indigo-500">(you)</span>
                      )}
                    </td>
                    <td className="px-6 py-4 text-gray-500 whitespace-nowrap">
                      {user.email}
                    </td>
                    <td className="px-6 py-4">
                      <span className={`text-xs font-medium px-2.5 py-1 rounded-full whitespace-nowrap ${getRoleBadge(user.role)}`}>
                        {getRoleLabel(user.role)}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      {user.id === currentUserId ? (
                        <span className="text-gray-300 text-xs">—</span>
                      ) : (
                        <div className="flex items-center gap-3">
                          <select
                            value={user.role}
                            onChange={(e) => handleRoleChange(user.id, e.target.value)}
                            className="border border-gray-300 rounded-lg px-2 py-1 text-xs focus:outline-none focus:ring-2 focus:ring-indigo-500"
                          >
                            {ROLE_OPTIONS.map((opt) => (
                              <option key={opt.value} value={opt.value}>
                                {opt.label}
                              </option>
                            ))}
                          </select>
                          <button
                            onClick={() => handleDelete(user.id, user.userName)}
                            className="text-red-400 hover:text-red-600 text-xs transition-colors whitespace-nowrap"
                          >
                            Delete
                          </button>
                        </div>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {totalPages > 1 && (
        <div className="flex items-center justify-center gap-2 mt-6">
          <button
            onClick={() => setPage((p) => p - 1)}
            disabled={page === 0}
            className="px-4 py-2 text-sm border border-gray-300 rounded-lg disabled:opacity-40 hover:bg-gray-50 transition-colors"
          >
            ← Prev
          </button>
          <span className="text-sm text-gray-500">
            Page {page + 1} of {totalPages}
          </span>
          <button
            onClick={() => setPage((p) => p + 1)}
            disabled={page === totalPages - 1}
            className="px-4 py-2 text-sm border border-gray-300 rounded-lg disabled:opacity-40 hover:bg-gray-50 transition-colors"
          >
            Next →
          </button>
        </div>
      )}
    </div>
  );
};

export default Users;