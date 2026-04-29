import { useState } from "react";
import { useCategories, useRoles } from "../features/skills/hooks";
import { useAnalyzeAndSave } from "../features/analysis/hooks";

const EXPERIENCE_LEVELS = ["BEGINNER", "INTERMEDIATE", "ADVANCED"];

const SkillAnalyzer = () => {
  const [selectedCategory, setSelectedCategory] = useState("");
  const [selectedRole, setSelectedRole] = useState("");
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [experienceLevel, setExperienceLevel] = useState("BEGINNER");
  const [includeRepos, setIncludeRepos] = useState(false);
  const [result, setResult] = useState(null);

  const { data: categories, isLoading: loadingCategories } = useCategories();
  const { data: roles, isLoading: loadingRoles } = useRoles(selectedCategory);
  const { mutate: analyze, isPending } = useAnalyzeAndSave();

  // get skills for selected role
  const roleSkills =
    roles?.find((r) => r.roleName === selectedRole)?.skills ?? [];

  const handleCategoryChange = (e) => {
    setSelectedCategory(e.target.value);
    setSelectedRole("");
    setSelectedSkills([]);
    setResult(null);
  };

  const handleRoleChange = (e) => {
    setSelectedRole(e.target.value);
    setSelectedSkills([]);
    setResult(null);
  };

  const toggleSkill = (skill) => {
    setSelectedSkills((prev) =>
      prev.includes(skill)
        ? prev.filter((s) => s !== skill)
        : [...prev, skill]
    );
  };

  const handleSubmit = () => {
    if (!selectedRole || selectedSkills.length === 0) return;

    analyze(
      {
        userSkills: selectedSkills,
        targetRole: selectedRole,
        experienceLevel,
        includeRepos,
      },
      {
        onSuccess: (res) => setResult(res.data),
        onError: () => alert("Analysis failed. Please try again."),
      }
    );
  };

  return (
    <div className="max-w-4xl mx-auto">
      {/* header */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Skill Analyzer</h1>
        <p className="text-gray-500 mt-1">
          Select your target role and skills to see your gap analysis.
        </p>
      </div>

      {/* form card */}
      <div className="bg-white rounded-xl border border-gray-200 p-6 mb-6">

        {/* step 1 - category */}
        <div className="mb-6">
          <label className="block text-sm font-semibold text-gray-700 mb-2">
            1. Select Category
          </label>
          <select
            value={selectedCategory}
            onChange={handleCategoryChange}
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">-- Choose a category --</option>
            {loadingCategories ? (
              <option disabled>Loading...</option>
            ) : (
              categories?.map((cat) => (
                <option key={cat.id} value={cat.name}>
                  {cat.name}
                </option>
              ))
            )}
          </select>
        </div>

        {/* step 2 - role */}
        <div className="mb-6">
          <label className="block text-sm font-semibold text-gray-700 mb-2">
            2. Select Role
          </label>
          <select
            value={selectedRole}
            onChange={handleRoleChange}
            disabled={!selectedCategory}
            className="w-full border border-gray-300 rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:text-gray-400"
          >
            <option value="">-- Choose a role --</option>
            {loadingRoles ? (
              <option disabled>Loading...</option>
            ) : (
              roles?.map((role) => (
                <option key={role.id} value={role.roleName}>
                  {role.roleName}
                </option>
              ))
            )}
          </select>
        </div>

        {/* step 3 - skills */}
        {selectedRole && roleSkills.length > 0 && (
          <div className="mb-6">
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              3. Select Your Skills{" "}
              <span className="text-gray-400 font-normal">
                ({selectedSkills.length} selected)
              </span>
            </label>
            <div className="flex flex-wrap gap-2">
              {roleSkills.map((skill) => (
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
        )}

        {/* step 4 - experience */}
        <div className="mb-6">
          <label className="block text-sm font-semibold text-gray-700 mb-2">
            4. Experience Level
          </label>
          <div className="flex gap-3">
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

        {/* step 5 - include repos toggle */}
        <div className="mb-6 flex items-center gap-3">
          <button
            type="button"
            onClick={() => setIncludeRepos((prev) => !prev)}
            className={`w-11 h-6 rounded-full transition-colors relative ${
              includeRepos ? "bg-indigo-600" : "bg-gray-300"
            }`}
          >
            <span
              className={`absolute top-0.5 left-0.5 w-5 h-5 bg-white rounded-full shadow transition-transform ${
                includeRepos ? "translate-x-5" : "translate-x-0"
              }`}
            />
          </button>
          <label className="text-sm font-medium text-gray-700">
            Include GitHub repo recommendations
          </label>
        </div>

        {/* analyze button */}
        <button
          onClick={handleSubmit}
          disabled={!selectedRole || selectedSkills.length === 0 || isPending}
          className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-300 text-white font-semibold py-3 rounded-lg text-sm transition-colors"
        >
          {isPending ? "Analyzing..." : "Analyze Skills"}
        </button>
      </div>

      {/* results */}
      {result && (
        <div className="space-y-6">

          {/* progress */}
          <div className="bg-white rounded-xl border border-gray-200 p-6">
            <h2 className="text-lg font-semibold text-gray-800 mb-4">
              Your Match Score
            </h2>
            <div className="flex items-center gap-4">
              <div className="flex-1 bg-gray-100 rounded-full h-4">
                <div
                  className="bg-indigo-500 h-4 rounded-full transition-all"
                  style={{ width: `${result.progress}%` }}
                />
              </div>
              <span className="text-2xl font-bold text-indigo-600">
                {result.progress}%
              </span>
            </div>
          </div>

          {/* matched + missing skills */}
          <div className="grid grid-cols-1 gap-6 md:grid-cols-2">
            {/* matched */}
            <div className="bg-white rounded-xl border border-gray-200 p-6">
              <h3 className="font-semibold text-gray-800 mb-3">
                ✅ Matched Skills ({result.matchedSkills?.length ?? 0})
              </h3>
              {result.matchedSkills?.length === 0 ? (
                <p className="text-gray-400 text-sm">No matched skills.</p>
              ) : (
                <div className="flex flex-wrap gap-2">
                  {result.matchedSkills?.map((skill) => (
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

            {/* missing */}
            <div className="bg-white rounded-xl border border-gray-200 p-6">
              <h3 className="font-semibold text-gray-800 mb-3">
                ❌ Missing Skills ({result.missingSkills?.length ?? 0})
              </h3>
              {result.missingSkills?.length === 0 ? (
                <p className="text-gray-400 text-sm">
                  You have all the skills!
                </p>
              ) : (
                <div className="flex flex-wrap gap-2">
                  {result.missingSkills?.map((skill) => (
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
          {result.resourcesBySkill &&
            Object.keys(result.resourcesBySkill).length > 0 && (
              <div className="bg-white rounded-xl border border-gray-200 p-6">
                <h3 className="font-semibold text-gray-800 mb-4">
                  📚 Learning Resources
                </h3>
                <div className="space-y-4">
                  {Object.entries(result.resourcesBySkill).map(
                    ([skill, resources]) => (
                      <div key={skill}>
                        <p className="text-sm font-semibold text-gray-700 mb-2 capitalize">
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
          {result.reposBySkill &&
            Object.keys(result.reposBySkill).length > 0 && (
              <div className="bg-white rounded-xl border border-gray-200 p-6">
                <h3 className="font-semibold text-gray-800 mb-4">
                  ⌥ GitHub Repos
                </h3>
                <div className="space-y-4">
                  {Object.entries(result.reposBySkill).map(
                    ([skill, repos]) => (
                      <div key={skill}>
                        <p className="text-sm font-semibold text-gray-700 mb-2 capitalize">
                          {skill}
                        </p>
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
                      </div>
                    )
                  )}
                </div>
              </div>
            )}
        </div>
      )}
    </div>
  );
};

export default SkillAnalyzer;