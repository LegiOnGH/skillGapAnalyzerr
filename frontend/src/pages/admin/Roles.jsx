import { useState } from "react";
import { useCategories, useRoles } from "../../features/skills/hooks";
import { useCreateRole, useDeleteRole, useUpdateRole } from "../../features/admin/hooks";
import { getErrorMessage } from "../../utils/errorHandler";

const Roles = () => {
  const [selectedCategory, setSelectedCategory] = useState("");
  const [roleName, setRoleName] = useState("");
  const [skills, setSkills] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [editingRole, setEditingRole] = useState(null);
  const [editSkills, setEditSkills] = useState("");

  const { data: categories } = useCategories();
  const { data: roles, isLoading } = useRoles(selectedCategory);
  const { mutate: createRole, isPending: creating } = useCreateRole();
  const { mutate: deleteRole } = useDeleteRole();
  const { mutate: updateRole, isPending: updating } = useUpdateRole();

  const handleCreate = () => {
    if (!roleName.trim() || !selectedCategory || !skills.trim()) {
      setError("All fields are required.");
      return;
    }

    const skillList = skills
      .split(",")
      .map((s) => s.trim())
      .filter((s) => s.length > 0);

    if (skillList.length === 0) {
      setError("Enter at least one skill.");
      return;
    }

    createRole(
      { roleName: roleName.trim(), category: selectedCategory, skills: skillList },
      {
        onSuccess: () => {
          setRoleName("");
          setSkills("");
          setError("");
          setSuccess("Role created successfully.");
          setTimeout(() => setSuccess(""), 3000);
        },
        onError: (err) => {
          setError(getErrorMessage(err));
          setSuccess("");
        },
      }
    );
  };

  const handleDelete = (id, name) => {
    if (confirm(`Delete role "${name}"?`)) {
      deleteRole(id, {
        onError: (err) => setError(getErrorMessage(err)),
      });
    }
  };

  const handleEditStart = (role) => {
    setEditingRole(role);
    setEditSkills(role.skills?.join(", ") ?? "");
  };

  const handleEditSave = () => {
    const skillList = editSkills
      .split(",")
      .map((s) => s.trim())
      .filter((s) => s.length > 0);

    if (skillList.length === 0) {
      setError("Enter at least one skill.");
      return;
    }

    updateRole(
      {
        id: editingRole.id,
        data: {
          roleName: editingRole.roleName,
          category: selectedCategory,
          skills: skillList,
        },
      },
      {
        onSuccess: () => {
          setEditingRole(null);
          setEditSkills("");
          setSuccess("Role updated successfully.");
          setTimeout(() => setSuccess(""), 3000);
        },
        onError: (err) => setError(getErrorMessage(err)),
      }
    );
  };

  return (
    <div className="max-w-3xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Roles</h1>
        <p className="text-gray-500 mt-1">Manage roles and their required skills.</p>
      </div>

      {/* create form */}
      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-6">
        <h2 className="text-sm font-semibold text-gray-700 mb-4">Add New Role</h2>
        <div className="space-y-3">
          <select
            value={selectedCategory}
            onChange={(e) => setSelectedCategory(e.target.value)}
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">-- Select Category --</option>
            {categories?.map((cat) => (
              <option key={cat.id} value={cat.name}>{cat.name}</option>
            ))}
          </select>
          <input
            type="text"
            value={roleName}
            onChange={(e) => { setRoleName(e.target.value); setError(""); }}
            placeholder="Role name e.g. Backend Developer"
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
          <input
            type="text"
            value={skills}
            onChange={(e) => { setSkills(e.target.value); setError(""); }}
            placeholder="Skills (comma separated) e.g. Java, Spring Boot, MongoDB"
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
          <button
            onClick={handleCreate}
            disabled={creating}
            className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white font-semibold py-2.5 rounded-lg text-sm transition-colors"
          >
            {creating ? "Creating..." : "Create Role"}
          </button>
        </div>
        {error && <p className="text-red-500 text-xs mt-2">{error}</p>}
        {success && <p className="text-green-600 text-xs mt-2">{success}</p>}
      </div>

      {/* filter */}
      <div className="mb-4">
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
          className="border border-gray-300 rounded-lg px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option value="">-- Filter by Category --</option>
          {categories?.map((cat) => (
            <option key={cat.id} value={cat.name}>{cat.name}</option>
          ))}
        </select>
      </div>

      {/* roles list */}
      <div className="bg-white rounded-xl border border-gray-200">
        {!selectedCategory ? (
          <div className="p-12 text-center text-gray-400 text-sm">
            Select a category to view roles.
          </div>
        ) : isLoading ? (
          <div className="p-6 space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-16 bg-gray-100 rounded animate-pulse" />
            ))}
          </div>
        ) : roles?.length === 0 ? (
          <div className="p-12 text-center text-gray-400 text-sm">
            No roles in this category yet.
          </div>
        ) : (
          <ul className="divide-y divide-gray-100">
            {roles?.map((role) => (
              <li key={role.id} className="px-6 py-4">
                {editingRole?.id === role.id ? (
                  <div className="space-y-2">
                    <p className="text-sm font-semibold text-gray-800">
                      {role.roleName}
                    </p>
                    <input
                      type="text"
                      value={editSkills}
                      onChange={(e) => setEditSkills(e.target.value)}
                      placeholder="Skills (comma separated)"
                      className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                    <div className="flex gap-2">
                      <button
                        onClick={handleEditSave}
                        disabled={updating}
                        className="bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white text-xs font-medium px-4 py-1.5 rounded-lg transition-colors"
                      >
                        {updating ? "Saving..." : "Save"}
                      </button>
                      <button
                        onClick={() => setEditingRole(null)}
                        className="text-xs text-gray-400 hover:text-gray-600 px-2"
                      >
                        Cancel
                      </button>
                    </div>
                  </div>
                ) : (
                  <div className="flex items-start justify-between">
                    <div>
                      <p className="text-sm font-semibold text-gray-800">
                        {role.roleName}
                      </p>
                      <div className="flex flex-wrap gap-1 mt-2">
                        {role.skills?.map((skill) => (
                          <span
                            key={skill}
                            className="bg-indigo-50 text-indigo-600 text-xs px-2 py-0.5 rounded-full"
                          >
                            {skill}
                          </span>
                        ))}
                      </div>
                    </div>
                    <div className="flex gap-3 ml-4 shrink-0">
                      <button
                        onClick={() => handleEditStart(role)}
                        className="text-gray-400 hover:text-indigo-600 text-sm transition-colors"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(role.id, role.roleName)}
                        className="text-red-400 hover:text-red-600 text-sm transition-colors"
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                )}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Roles;