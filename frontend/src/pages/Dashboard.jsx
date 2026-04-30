import { Link } from "react-router-dom";
import { useAnalysisHistory } from "../features/analysis/hooks";
import { formatDate } from "../utils/formatDate";

const Dashboard = () => {
  const { data, isLoading, error} = useAnalysisHistory(0, 3);

  const analyses = data?.content ?? [];
  const totalAnalyses = data?.totalElements ?? 0;

  const bestMatch = analyses.length
    ? Math.max(...analyses.map((a) => a.progress))
    : 0;

  return (
    <div className="max-w-5xl mx-auto">
      {/* header */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Dashboard</h1>
        <p className="text-gray-500 mt-1">
          Welcome back! Here's your skill progress overview.
        </p>
      </div>

      {/* stats */}
      <div className="grid grid-cols-1 gap-6 md:grid-cols-3 mb-8">
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <p className="text-sm text-gray-500 mb-1">Total Analyses</p>
          <p className="text-3xl font-bold text-gray-800">
            {isLoading ? "—" : totalAnalyses}
          </p>
        </div>
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <p className="text-sm text-gray-500 mb-1">Latest Role</p>
          <p className="text-xl font-bold text-gray-800 truncate">
            {isLoading ? "—" : analyses[0]?.targetRole ?? "None yet"}
          </p>
        </div>
        <div className="bg-white rounded-xl border border-gray-200 p-6">
          <p className="text-sm text-gray-500 mb-1">Best Match</p>
          <p className="text-3xl font-bold text-indigo-600">
            {isLoading ? "—" : `${bestMatch}%`}
          </p>
        </div>
      </div>

      {/* quick actions */}
      <div className="flex gap-4 mb-8">
        <Link
          to="/skills/analyze"
          className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-5 py-2.5 rounded-lg text-sm transition-colors"
        >
          + New Analysis
        </Link>
        <Link
          to="/repos"
          className="bg-white hover:bg-gray-50 text-gray-700 font-semibold px-5 py-2.5 rounded-lg text-sm border border-gray-200 transition-colors"
        >
          Find Repos
        </Link>
      </div>

      {error && (
        <div className="bg-red-50 text-red-600 text-sm rounded-lg p-3 mb-4">
          Failed to load recent analyses.
        </div>
      )}

      {/* recent analyses */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-800">
            Recent Analyses
          </h2>
          {totalAnalyses > 3 && (
            <Link
              to="/analysis"
              className="text-sm text-indigo-600 hover:underline"
            >
              View all →
            </Link>
          )}
        </div>

        {isLoading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <div
                key={i}
                className="bg-white rounded-xl border border-gray-200 p-5 animate-pulse h-20"
              />
            ))}
          </div>
        ) : analyses.length === 0 ? (
          <div className="bg-white rounded-xl border border-gray-200 p-12 text-center">
            <p className="text-gray-400 text-sm mb-4">No analyses yet.</p>
            <Link
              to="/skills/analyze"
              className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-5 py-2.5 rounded-lg text-sm transition-colors whitespace-nowrap"
            >
              Run your first analysis
            </Link>
          </div>
        ) : (
          <div className="space-y-3">
            {analyses.map((analysis) => (
              <Link
                key={analysis.id}
                to={`/analysis/${analysis.id}`}
                className="bg-white rounded-xl border border-gray-200 p-5 flex items-center justify-between hover:border-indigo-300 transition-colors"
              >
                <div>
                  <p className="font-semibold text-gray-800">
                    {analysis.targetRole}
                  </p>
                  <p className="text-sm text-gray-400 mt-0.5">
                    {formatDate(analysis.createdAt)}
                  </p>
                </div>
                <div className="flex items-center gap-3">
                  <div className="w-24 bg-gray-100 rounded-full h-2">
                    <div
                      className="bg-indigo-500 h-2 rounded-full"
                      style={{ width: `${analysis.progress}%` }}
                    />
                  </div>
                  <span className="text-sm font-semibold text-indigo-600 w-10 text-right">
                    {analysis.progress}%
                  </span>
                </div>
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;