import api from "../../services/api";

export const recommendRepos = (data) => api.post("/repos/recommend", data);