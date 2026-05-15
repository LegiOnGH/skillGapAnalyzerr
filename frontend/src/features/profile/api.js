import api from "../../services/api";

export const getMe = () => api.get("/users/me");
export const updateMe = (data) => api.patch("/users/me", data);