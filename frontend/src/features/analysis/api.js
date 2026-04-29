import api from "../../services/api";

export const analyzeAndSave = (data) => api.post("/analysis", data);

export const getAnalysisHistory = (page, size) =>
  api.get(`/analysis?page=${page}&size=${size}`);

export const getAnalysisById = (id) => api.get(`/analysis/${id}`);

export const deleteAnalysis = (id) => api.delete(`/analysis/${id}`);