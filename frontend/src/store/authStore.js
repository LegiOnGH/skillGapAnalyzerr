import { create } from "zustand";

const useAuthStore = create((set) => ({
  token: localStorage.getItem("token"),
  role: localStorage.getItem("role"),
  userId: localStorage.getItem("userId"),

  login: ({ accessToken, role, userId }) => {
    localStorage.setItem("token", accessToken);
    localStorage.setItem("role", role);
    localStorage.setItem("userId", userId);
    set({ token: accessToken, role, userId });
  },

  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("userId");
    set({ token: null, role: null, userId: null });
  },
}));

export default useAuthStore;