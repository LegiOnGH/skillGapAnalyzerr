import api from "../../services/api";

// categories
export const createCategory = (data) => api.post("/admin/categories", data);

// roles
export const createRole = (data) => api.post("/admin/roles", data);
export const deleteRole = (id) => api.delete(`/admin/roles/${id}`);
export const updateRole = (id, data) => api.put(`/admin/roles/${id}`, data);

// resources
export const createResource = (data) => api.post("/admin/resources", data);
export const getAllResources = () => api.get("/admin/resources");
export const updateResource = (id, data) => api.patch(`/admin/resources/${id}`, data);
export const deleteResource = (id, resource) =>
  api.delete(`/admin/resources/${id}?resource=${encodeURIComponent(resource)}`);