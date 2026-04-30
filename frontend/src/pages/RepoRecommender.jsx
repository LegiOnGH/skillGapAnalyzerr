import { useState } from "react";
import { useAllSkills } from "../features/skills/hooks";
import { useRepoRecommendation } from "../features/repos/hooks";
import { getErrorMessage } from "../utils/errorHandler";

const EXPERIENCE_LEVELS = ["BEGINNER", "INTERMEDIATE", "ADVANCED"];

const RepoRecommender = () => {
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [experienceLevel, setExperienceLevel] = useState("BEGINNER");
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  const { data: skillsByCategory, isLoading, error: skillsError } = useAllSkills();
  const { mutate: recommend, isPending } = useRepoRecommendation();

  const toggleSkill = (skill) => {
    setSelectedSkills((prev) =>
      prev.includes(skill)
        ? prev.filter((s) => s !== skill)
        : [...prev, skill]
    );
    setError("");
  };

  const handleSubmit = () => {
    if (selectedSkills.length === 0) {
      setError("Please select at least one skill.");
      return;
    }
    recommend(
      { skills: selectedSkills, experienceLevel },
      {
        onSuccess: (res) => {
          setResult(res.data.reposBySkill);
          setError("");
        },
        onError: (err) => setError(getErrorMessage(err)),
      }
    );
  };

  if (skillsError) {
    return (
      <div className="max-w-4xl mx-auto">
        <div className="bg-red-50 text-red-600 text-sm rounded-lg p-4">
          Failed to load skills. Please refresh the page.
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Repo Finder</h1>
        <p className="text-gray-500 mt-1">
          Select skills and experience level to find relevant GitHub repositories.
        </p>
      </div>

      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-6">

        {/* skills by category */}
        <div className="mb-6">
          <label className="block text-sm font-semibold text-gray-700 mb-3">
            1. Select Skills{" "}
            <span className="text-gray-400 font-normal">
              ({selectedSkills.length} selected)
            </span>
          </label>

          {isLoading ? (
            <div className="space-y-3">
              {[1, 2, 3].map((i) => (
                <div
                  key={i}
                  className="h-16 bg-gray-100 rounded-lg animate-pulse"
                />
              ))}
            </div>
          ) : !skillsByCategory || Object.keys(skillsByCategory).length === 0 ? (
            <p className="text-gray-400 text-sm">
              No skills available. Ask an admin to add some.
            </p>
          ) : (
            <div className="space-y-5">
              {Object.entries(skillsByCategory).map(([category, skills]) => (
                <div key={category}>
                  <p className="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">
                    {category}
                  </p>
                  <div className="flex flex-wrap gap-2">
                    {skills.map((skill) => (
                      <button
                        key={skill}
                        type="button"
                        onClick={() => toggleSkill(skill)}
                        className={`px-3 py-1.5 rounded-full text-sm font-medium border transition-colors ${
                          selectedSkills.includes(skill)
                            ? "bg-indigo-600 text-white border-indigo-600"
                            : "bg-white text-gray-600 border-gray-300 hover:border-indigo-400"
                        }`}
                      >
                        {skill}
                      </button>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* experience level */}
        <div className="mb-6">
          <label className="block text-sm font-semibold text-gray-700 mb-2">
            2. Experience Level
          </label>
          <div className="flex gap-3 flex-wrap">
            {EXPERIENCE_LEVELS.map((level) => (
              <button
                key={level}
                type="button"
                onClick={() => setExperienceLevel(level)}
                className={`px-4 py-2 rounded-lg text-sm font-medium border transition-colors ${
                  experienceLevel === level
                    ? "bg-indigo-600 text-white border-indigo-600"
                    : "bg-white text-gray-600 border-gray-300 hover:border-indigo-400"
                }`}
              >
                {level.charAt(0) + level.slice(1).toLowerCase()}
              </button>
            ))}
          </div>
        </div>

        {error && (
          <p className="text-red-500 text-sm mb-4">{error}</p>
        )}

        <button
          onClick={handleSubmit}
          disabled={isPending}
          className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white font-semibold py-3 rounded-lg text-sm transition-colors"
        >
          {isPending ? "Finding Repos..." : "Find Repos"}
        </button>
      </div>

      {/* results */}
      {result && (
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <h2 className="text-lg font-semibold text-gray-800 mb-4">Results</h2>
          {Object.keys(result).length === 0 ? (
            <p className="text-gray-400 text-sm">No repositories found.</p>
          ) : (
            <div className="space-y-6">
              {Object.entries(result).map(([skill, repos]) => (
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
                            <span className="text-xs text-gray-400 shrink-0 ml-2">
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
          )}
        </div>
      )}
    </div>
  );
};

export default RepoRecommender;