import api from "../../services/api";

export const getCategories = () => api.get("/skills/categories");

export const getRoles = (category) =>
  api.get(`/skills/roles?category=${category}`);

export const getRoleByName = (roleName) =>
  api.get(`/skills/roles/${roleName}`);

export const getAllSkills = () => 
  api.get("/skills/all");