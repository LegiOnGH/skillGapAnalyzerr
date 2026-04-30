import { useState } from "react";
import { Link } from "react-router-dom";
import { useAnalysisHistory } from "../features/analysis/hooks";
import { useDeleteAnalysis } from "../features/analysis/hooks";
import { formatDate } from "../utils/formatDate";

const AnalysisHistory = () => {
  const [page, setPage] = useState(0);
  const size = 9;

  const { data, isLoading } = useAnalysisHistory(page, size);
  const { mutate: deleteAnalysis } = useDeleteAnalysis();

  const analyses = data?.content ?? [];
  const totalPages = data?.totalPages ?? 0;

  const handleDelete = (e, id) => {
    e.preventDefault(); // prevent navigating to detail
    if (confirm("Delete this analysis?")) {
      deleteAnalysis(id);
    }
  };

  return (
    <div className="max-w-5xl mx-auto">
      {/* header */}
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Analysis History</h1>
        <p className="text-gray-500 mt-1">
          All your past skill gap analyses.
        </p>
      </div>

      {isLoading ? (
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          {[1, 2, 3, 4, 5, 6].map((i) => (
            <div
              key={i}
              className="bg-white rounded-xl border border-gray-200 p-5 animate-pulse h-32"
            />
          ))}
        </div>
      ) : analyses.length === 0 ? (
        <div className="bg-white rounded-xl border border-gray-200 p-16 text-center">
          <p className="text-gray-400 mb-4">No analyses yet.</p>
          <Link
            to="/skills/analyze"
            className="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-5 py-2.5 rounded-lg text-sm transition-colors"
          >
            Run your first analysis
          </Link>
        </div>
      ) : (
        <>
          <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
            {analyses.map((analysis) => (
              <Link
                key={analysis.id}
                to={`/analysis/${analysis.id}`}
                className="bg-white rounded-xl border border-gray-200 p-5 hover:border-indigo-300 transition-colors block relative group"
              >
                {/* delete button */}
                <button
                  onClick={(e) => handleDelete(e, analysis.id)}
                  className="absolute top-3 right-3 text-gray-300 hover:text-red-500 transition-colors opacity-0 group-hover:opacity-100 text-lg"
                >
                  ×
                </button>

                <p className="font-semibold text-gray-800 pr-6 truncate">
                  {analysis.targetRole}
                </p>
                <p className="text-sm text-gray-400 mt-1">
                  {formatDate(analysis.createdAt)}
                </p>

                {/* progress bar */}
                <div className="mt-4">
                  <div className="flex justify-between text-xs text-gray-500 mb-1">
                    <span>Match</span>
                    <span className="font-semibold text-indigo-600">
                      {analysis.progress}%
                    </span>
                  </div>
                  <div className="w-full bg-gray-100 rounded-full h-2">
                    <div
                      className="bg-indigo-500 h-2 rounded-full"
                      style={{ width: `${analysis.progress}%` }}
                    />
                  </div>
                </div>
              </Link>
            ))}
          </div>

          {/* pagination */}
          {totalPages > 1 && (
            <div className="flex items-center justify-center gap-2 mt-8">
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
        </>
      )}
    </div>
  );
};

export default AnalysisHistory;