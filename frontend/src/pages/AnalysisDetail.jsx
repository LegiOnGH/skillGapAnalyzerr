import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAnalysisById } from "../features/analysis/hooks";
import { useDeleteAnalysis } from "../features/analysis/hooks";
import { formatDateTime } from "../utils/formatDate";
import { getErrorMessage } from "../utils/errorHandler";

const AnalysisDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const { data: analysis, isLoading } = useAnalysisById(id);
  const { mutate: deleteAnalysis, isPending: deleting } = useDeleteAnalysis();

  const handleDelete = () => {
    if (confirm("Delete this analysis?")) {
      deleteAnalysis(id, {
        onSuccess: () => navigate("/analysis"),
        onError: (err) => setError(getErrorMessage(err)),
      });
    }
  };

  if (isLoading) {
    return (
      <div className="max-w-4xl mx-auto space-y-4">
        {[1, 2, 3].map((i) => (
          <div
            key={i}
            className="bg-white rounded-xl border border-gray-200 p-6 animate-pulse h-32"
          />
        ))}
      </div>
    );
  }

  if (!analysis) {
    return (
      <div className="max-w-4xl mx-auto text-center py-16">
        <p className="text-gray-400">Analysis not found.</p>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto">
      {/* header */}
      <div className="flex items-start justify-between mb-8">
        <div>
          <button
            onClick={() => navigate("/analysis")}
            className="text-sm text-gray-400 hover:text-gray-600 mb-2 block transition-colors"
          >
            ← Back to History
          </button>
          <h1 className="text-2xl font-bold text-gray-800">
            {analysis.targetRole}
          </h1>
          <p className="text-gray-400 text-sm mt-1">
            {formatDateTime(analysis.createdAt)}
          </p>
        </div>
        <button
          onClick={handleDelete}
          disabled={deleting}
          className="bg-red-50 hover:bg-red-100 text-red-600 font-medium px-4 py-2 rounded-lg text-sm transition-colors"
        >
          {deleting ? "Deleting..." : "Delete"}
        </button>
      </div>

      {error && (
        <div className="bg-red-50 text-red-600 text-sm rounded-lg p-3 mb-4">
            {error}
        </div>
      )}

      {/* match score */}
      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-6">
        <h2 className="text-lg font-semibold text-gray-800 mb-4">
          Match Score
        </h2>
        <div className="flex items-center gap-4">
          <div className="flex-1 bg-gray-100 rounded-full h-4">
            <div
              className="bg-indigo-500 h-4 rounded-full transition-all"
              style={{ width: `${analysis.progress}%` }}
            />
          </div>
          <span className="text-2xl font-bold text-indigo-600">
            {analysis.progress}%
          </span>
        </div>
      </div>

      {/* your skills */}
      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-6">
        <h3 className="font-semibold text-gray-800 mb-3">
          Your Skills ({analysis.userSkills?.length ?? 0})
        </h3>
        {analysis.userSkills?.length === 0 ? (
          <p className="text-gray-400 text-sm">No skills provided.</p>
        ) : (
          <div className="flex flex-wrap gap-2">
            {analysis.userSkills?.map((skill) => (
              <span
                key={skill}
                className="bg-indigo-100 text-indigo-700 text-xs font-medium px-3 py-1 rounded-full"
              >
                {skill}
              </span>
            ))}
          </div>
        )}
      </div>

      {/* matched + missing */}
      <div className="grid grid-cols-1 gap-6 md:grid-cols-2 mb-6">
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <h3 className="font-semibold text-gray-800 mb-3">
            ✅ Matched Skills ({analysis.matchedSkills?.length ?? 0})
          </h3>
          {analysis.matchedSkills?.length === 0 ? (
            <p className="text-gray-400 text-sm">No matched skills.</p>
          ) : (
            <div className="flex flex-wrap gap-2">
              {analysis.matchedSkills?.map((skill) => (
                <span
                  key={skill}
                  className="bg-green-100 text-green-700 text-xs font-medium px-3 py-1 rounded-full"
                >
                  {skill}
                </span>
              ))}
            </div>
          )}
        </div>

        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <h3 className="font-semibold text-gray-800 mb-3">
            ❌ Missing Skills ({analysis.missingSkills?.length ?? 0})
          </h3>
          {analysis.missingSkills?.length === 0 ? (
            <p className="text-gray-400 text-sm">You had all the skills!</p>
          ) : (
            <div className="flex flex-wrap gap-2">
              {analysis.missingSkills?.map((skill) => (
                <span
                  key={skill}
                  className="bg-red-100 text-red-700 text-xs font-medium px-3 py-1 rounded-full"
                >
                  {skill}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* resources */}
      {analysis.resourcesBySkill &&
        Object.keys(analysis.resourcesBySkill).length > 0 && (
          <div className="bg-white rounded-xl border border-gray-200 p-6">
            <h3 className="font-semibold text-gray-800 mb-4">
              📚 Learning Resources
            </h3>
            <div className="space-y-4">
              {Object.entries(analysis.resourcesBySkill).map(
                ([skill, resources]) => (
                  <div key={skill}>
                    <p className="text-sm font-semibold text-gray-700 mb-2">
                      {skill}
                    </p>
                    <ul className="space-y-1">
                      {resources.map((url) => (
                        <li key={url}>
                          <a
                            href={url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-indigo-600 hover:underline text-sm truncate block"
                          >
                            {url}
                          </a>
                        </li>
                      ))}
                    </ul>
                  </div>
                )
              )}
            </div>
          </div>
        )}
        {/* repos */}
      {analysis.reposBySkill &&
        Object.keys(analysis.reposBySkill).length > 0 && (
          <div className="bg-white rounded-xl border border-gray-200 p-6 mt-6">
            <h3 className="font-semibold text-gray-800 mb-4">
              ⌥ GitHub Repos
            </h3>
            <div className="space-y-4">
              {Object.entries(analysis.reposBySkill).map(([skill, repos]) => (
                <div key={skill}>
                  <p className="text-sm font-semibold text-gray-700 mb-2">
                    {skill}
                  </p>
                  {repos.length === 0 ? (
                    <p className="text-gray-400 text-xs">
                      No repositories found for this skill.
                    </p>
                  ) : (
                    <div className="space-y-2">
                      {repos.map((repo) => (
                        <a
                          key={repo.url}
                          href={repo.url}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="block border border-gray-200 rounded-lg p-3 hover:border-indigo-300 transition-colors"
                        >
                          <div className="flex items-center justify-between">
                            <p className="text-sm font-medium text-gray-800">
                              {repo.name}
                            </p>
                            <span className="text-xs text-gray-400">
                              ★ {repo.stars}
                            </span>
                          </div>
                          <p className="text-xs text-gray-500 mt-1 truncate">
                            {repo.description}
                          </p>
                        </a>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}
    </div>
  );
};

export default AnalysisDetail;