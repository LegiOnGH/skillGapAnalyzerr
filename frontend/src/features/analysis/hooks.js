import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { getAnalysisHistory, getAnalysisById, deleteAnalysis, analyzeAndSave } from "./api";
import { QUERY_KEYS } from "../../constants/queryKeys";
import { unwrapPage } from "../../utils/pagination";

export const useAnalysisHistory = (page, size) => {
  return useQuery({
    queryKey: QUERY_KEYS.ANALYSIS_HISTORY(page, size),
    queryFn: () => getAnalysisHistory(page, size).then((res) => res.data),
    select: (data) => unwrapPage(data),
  });
};

export const useAnalysisById = (id) => {
  return useQuery({
    queryKey: QUERY_KEYS.ANALYSIS_BY_ID(id),
    queryFn: () => getAnalysisById(id).then((res) => res.data),
    enabled: !!id, // only fetch if id exists
  });
};

export const useDeleteAnalysis = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteAnalysis,
    onSuccess: () => {
      // refresh history after delete
      queryClient.invalidateQueries({ queryKey: ["analysis"] });
    },
  });
};

export const useAnalyzeAndSave = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: analyzeAndSave,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["analysis"] });
    },
  });
};