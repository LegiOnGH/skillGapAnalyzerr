import { useState } from "react";
import { useAllResources, useCreateResource, useUpdateResource, useDeleteResource } from "../../features/admin/hooks";
import { useAllSkills } from "../../features/skills/hooks";
import { getErrorMessage } from "../../utils/errorHandler";

const Resources = () => {
  const [skill, setSkill] = useState("");
  const [resourceUrl, setResourceUrl] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [editingResource, setEditingResource] = useState(null);
  const [editUrl, setEditUrl] = useState("");

  const { data: resources, isLoading } = useAllResources();
  const { data: skillsByCategory } = useAllSkills();
  const { mutate: createResource, isPending: creating } = useCreateResource();
  const { mutate: updateResource, isPending: updating } = useUpdateResource();
  const { mutate: deleteResource } = useDeleteResource();

  const handleCreate = () => {
    if (!skill.trim() || !resourceUrl.trim()) {
      setError("Skill and resource URL are required.");
      return;
    }
    createResource(
      { skill: skill.trim(), resources: [resourceUrl.trim()] },
      {
        onSuccess: () => {
          setSkill("");
          setResourceUrl("");
          setError("");
          setSuccess("Resource added successfully.");
          setTimeout(() => setSuccess(""), 3000);
        },
        onError: (err) => {
          setError(getErrorMessage(err));
          setSuccess("");
        },
      }
    );
  };

  const handleUpdate = (id) => {
    if (!editUrl.trim()) return;
    updateResource(
      {
        id,
        data: {
          oldResource: editingResource.oldUrl,
          newResource: editUrl.trim(),
        },
      },
      {
        onSuccess: () => {
          setEditingResource(null);
          setEditUrl("");
          setSuccess("Resource updated.");
          setTimeout(() => setSuccess(""), 3000);
        },
        onError: (err) => setError(getErrorMessage(err)),
      }
    );
  };

  const handleDelete = (id, url) => {
    if (confirm(`Delete resource "${url}"?`)) {
      deleteResource(
        { id, resource: url },
        {
          onError: (err) => setError(getErrorMessage(err)),
        }
      );
    }
  };

  return (
    <div className="max-w-3xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Resources</h1>
        <p className="text-gray-500 mt-1">
          Manage learning resources for each skill.
        </p>
      </div>

      {/* create form */}
      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-6">
        <h2 className="text-sm font-semibold text-gray-700 mb-4">
          Add Resource
        </h2>
        <div className="space-y-3">
          <select
            value={skill}
            onChange={(e) => { setSkill(e.target.value); setError(""); }}
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">-- Select Skill --</option>
            {skillsByCategory &&
              Object.entries(skillsByCategory).map(([category, skills]) => (
                <optgroup key={category} label={category}>
                  {skills.map((s) => (
                    <option key={s} value={s}>{s}</option>
                  ))}
                </optgroup>
              ))}
          </select>
          <input
            type="url"
            value={resourceUrl}
            onChange={(e) => { setResourceUrl(e.target.value); setError(""); }}
            placeholder="Resource URL e.g. https://reactjs.org"
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          />
          <button
            onClick={handleCreate}
            disabled={creating}
            className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white font-semibold py-2.5 rounded-lg text-sm transition-colors"
          >
            {creating ? "Adding..." : "Add Resource"}
          </button>
        </div>
        {error && <p className="text-red-500 text-xs mt-2">{error}</p>}
        {success && <p className="text-green-600 text-xs mt-2">{success}</p>}
      </div>

      {/* resources list */}
      <div className="space-y-4">
        {isLoading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div
                key={i}
                className="h-24 bg-white rounded-xl border border-gray-200 animate-pulse"
              />
            ))}
          </div>
        ) : resources?.length === 0 ? (
          <div className="bg-white rounded-xl border border-gray-200 p-12 text-center text-gray-400 text-sm">
            No resources yet.
          </div>
        ) : (
          resources?.map((resource) => (
            <div
              key={resource.id}
              className="bg-white rounded-xl border border-gray-200 p-5"
            >
              <p className="text-sm font-semibold text-gray-800 mb-3">
                {resource.skill}
              </p>
              <ul className="space-y-2">
                {resource.resources?.map((url) => (
                  <li key={url} className="flex items-center gap-2">
                    {editingResource?.id === resource.id &&
                    editingResource?.oldUrl === url ? (
                      <div className="flex flex-1 gap-2">
                        <input
                          type="url"
                          value={editUrl}
                          onChange={(e) => setEditUrl(e.target.value)}
                          className="flex-1 border border-gray-300 rounded-lg px-3 py-1.5 text-xs focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        />
                        <button
                          onClick={() => handleUpdate(resource.id)}
                          disabled={updating}
                          className="text-xs bg-indigo-600 text-white px-3 py-1.5 rounded-lg hover:bg-indigo-700 transition-colors whitespace-nowrap"
                        >
                          {updating ? "Saving..." : "Save"}
                        </button>
                        <button
                          onClick={() => setEditingResource(null)}
                          className="text-xs text-gray-400 hover:text-gray-600 px-2"
                        >
                          Cancel
                        </button>
                      </div>
                    ) : (
                      <>
                        
                        <a  href={url}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="flex-1 text-xs text-indigo-600 hover:underline truncate"
                        >
                          {url}
                        </a>
                        <button
                          onClick={() => {
                            setEditingResource({ id: resource.id, oldUrl: url });
                            setEditUrl(url);
                          }}
                          className="text-xs text-gray-400 hover:text-gray-600 transition-colors whitespace-nowrap"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleDelete(resource.id, url)}
                          className="text-xs text-red-400 hover:text-red-600 transition-colors whitespace-nowrap"
                        >
                          Delete
                        </button>
                      </>
                    )}
                  </li>
                ))}
              </ul>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Resources;