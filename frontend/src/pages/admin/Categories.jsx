import { useState } from "react";
import { useCategories } from "../../features/skills/hooks";
import { useCreateCategory } from "../../features/admin/hooks";
import { getErrorMessage } from "../../utils/errorHandler";

const Categories = () => {
  const [name, setName] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const { data: categories, isLoading } = useCategories();
  const { mutate: createCategory, isPending: creating } = useCreateCategory();

  const handleCreate = () => {
    if (!name.trim()) {
      setError("Category name is required.");
      return;
    }
    createCategory(
      { name: name.trim() },
      {
        onSuccess: () => {
          setName("");
          setError("");
          setSuccess("Category created successfully.");
          setTimeout(() => setSuccess(""), 3000);
        },
        onError: (err) => {
          setError(getErrorMessage(err));
          setSuccess("");
        },
      }
    );
  };

  return (
    <div className="max-w-3xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Categories</h1>
        <p className="text-gray-500 mt-1">Manage skill categories.</p>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-6">
        <h2 className="text-sm font-semibold text-gray-700 mb-3">
          Add New Category
        </h2>
        <div className="flex gap-3">
          <input
            type="text"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
              setError("");
            }}
            placeholder="e.g. Frontend, Backend, DevOps"
            className="flex-1 border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
          <button
            onClick={handleCreate}
            disabled={creating}
            className="bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white font-semibold px-5 py-2.5 rounded-lg text-sm transition-colors whitespace-nowrap"
          >
            {creating ? "Adding..." : "Add"}
          </button>
        </div>
        {error && <p className="text-red-500 text-xs mt-2">{error}</p>}
        {success && <p className="text-green-600 text-xs mt-2">{success}</p>}
      </div>

      <div className="bg-white rounded-xl border border-gray-200">
        {isLoading ? (
          <div className="p-6 space-y-3">
            {[1, 2, 3].map((i) => (
              <div key={i} className="h-10 bg-gray-100 rounded animate-pulse" />
            ))}
          </div>
        ) : categories?.length === 0 ? (
          <div className="p-12 text-center text-gray-400 text-sm">
            No categories yet.
          </div>
        ) : (
          <ul className="divide-y divide-gray-100">
            {categories?.map((cat) => (
              <li
                key={cat.id}
                className="flex items-center justify-between px-6 py-4"
              >
                <span className="text-sm font-medium text-gray-800">
                  {cat.name}
                </span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Categories;