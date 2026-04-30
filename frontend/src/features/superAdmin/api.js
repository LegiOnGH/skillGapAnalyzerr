import api from "../../services/api";

export const getAllUsers = (page, size) =>
  api.get(`/super-admin/users?page=${page}&size=${size}`);

export const updateUserRole = (userId, role) =>
  api.patch(`/super-admin/users/${userId}/role`, {role});

export const deleteUser = (userId) =>
  api.delete(`/super-admin/users/${userId}`);